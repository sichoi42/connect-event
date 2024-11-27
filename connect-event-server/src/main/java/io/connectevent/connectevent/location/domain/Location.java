package io.connectevent.connectevent.location.domain;

import io.connectevent.connectevent.event.domain.Event;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Locations")
@Getter
@Setter
public class Location {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private Integer capacity;

    @Column
    private String address;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events;

    public static Location of(String name, int capacity, String address) {
        Location location = new Location();
        location.name = name;
        location.capacity = capacity;
        location.address = address;
        return location;
    }
}
