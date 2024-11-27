package io.connectevent.connectevent.event.domain;

import io.connectevent.connectevent.location.domain.Location;
import io.connectevent.connectevent.member.domain.Member;
import io.connectevent.connectevent.participant.domain.Participant;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventTag> eventTags;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants;

    public static Event of(Member member, String title, String description, LocalDateTime startedAt,
            LocalDateTime endedAt, Location location) {
        Event event = new Event();
        event.title = title;
        event.description = description;
        event.startedAt = startedAt;
        event.endedAt = endedAt;
        event.location = location;
        return event;
    }
}
