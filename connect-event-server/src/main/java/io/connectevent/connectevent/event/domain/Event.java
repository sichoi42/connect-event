package io.connectevent.connectevent.event.domain;

import io.connectevent.connectevent.location.domain.Location;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Events")
@Getter
@Setter
public class Event {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "\"description\"")
    private String description;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column(nullable = false)
    private LocalDateTime endedAt;

    @OneToOne(mappedBy = "event", fetch = FetchType.LAZY)
    private Location location;

    @OneToMany(mappedBy = "event")
    private Set<EventTag> eventTags;

}
