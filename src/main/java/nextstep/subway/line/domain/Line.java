package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.policy.domain.Price;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "color"}))
@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    private String color;

    @Embedded
    private Sections sections = new Sections();

    @Embedded
    private Price extraFare;

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Price extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public Line(String name, String color, Station upStation, Station downStation, Distance distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public Line(String name, String color, Station upStation, Station downStation, Distance distance, Price extraFare) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
        this.extraFare = extraFare;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Sections getSections() {
        return this.sections;
    }

    public Price getExtreFare() {
        if (this.extraFare == null) {
            return Price.of(0);
        }

        return this.extraFare;
    }

    public boolean addSection(Section section) {
        return this.sections.add(section);
    }

    public List<Station> findStations() {
        return this.sections.findStations();
    }

    public void deleteStation(Station station) {
        this.sections.deleteStation(station);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Line)) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }
}
