package nextstep.subway.path.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathAnalysisKey;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationDto;
import nextstep.subway.path.dto.ShortestPathInfo;
import nextstep.subway.path.infrastructure.PathAnalysis;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse searchShortestPath(Long sourceStationId, Long targetStationId) {
        vaildateShortestPath(sourceStationId, targetStationId);

        Station source = stationService.findById(sourceStationId);
        Station target = stationService.findById(targetStationId);

        PathAnalysis pathAnalysis = PathAnalysis.of(lineService.findAllSections());

        ShortestPathInfo shortestPathInfo = pathAnalysis.findShortestPaths(source, target);
        
        return createPathResponse(shortestPathInfo);
    }

    private void vaildateShortestPath(Long sourceStationId, Long targetStationId) {
        if (sourceStationId.equals(targetStationId)) {
            throw new IllegalArgumentException("시작역과 도착역이 같습니다.");
        }
    }

    private PathResponse createPathResponse(ShortestPathInfo shortestPathInfo) {
        List<Long> stationIds = convertPathAnalysisKeyToStationKey(shortestPathInfo);

        List<Station> stations = new ArrayList<>();
        
        for (Long stationId : stationIds) {
            stations.add(stationService.findById(stationId));
        }
        

        List<PathStationDto> pathStationDtos = convertPathAnaylysisKeyToPathStaionDto(stations);

        return new PathResponse(pathStationDtos, shortestPathInfo.getDistance().value());
    }

    private List<PathStationDto> convertPathAnaylysisKeyToPathStaionDto(List<Station> stations) {
        return stations.stream()
                        .map(PathStationDto::of)
                        .collect(Collectors.toList());
    }

    private List<Long> convertPathAnalysisKeyToStationKey(ShortestPathInfo shortestPathInfo) {
        return shortestPathInfo.getPathAnalysisKeys().stream()
                                .map(PathAnalysisKey::getStationId)
                                .collect(Collectors.toList());
    }
}
