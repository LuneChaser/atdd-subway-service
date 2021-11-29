package nextstep.subway.station.dto;

import nextstep.subway.common.SubwayUtil;
import nextstep.subway.station.domain.Station;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class StationResponse {
    private Long id;
    private String name;
    private String createdDate;
    private String modifiedDate;

    public static StationResponse of(Station station) {
        return new StationResponse(station.getId(), station.getName(), station.getCreatedDate(), station.getModifiedDate());
    }

    public StationResponse() {
    }

    public StationResponse(Long id, String name, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        
        this.createdDate = SubwayUtil.convertLocalDateTime(createdDate);
        this.modifiedDate = SubwayUtil.convertLocalDateTime(modifiedDate);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof StationResponse)) {
            return false;
        }
        StationResponse stationResponse = (StationResponse) o;
        return Objects.equals(id, stationResponse.id) && Objects.equals(name, stationResponse.name) && Objects.equals(createdDate, stationResponse.createdDate) && Objects.equals(modifiedDate, stationResponse.modifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, createdDate, modifiedDate);
    }
}
