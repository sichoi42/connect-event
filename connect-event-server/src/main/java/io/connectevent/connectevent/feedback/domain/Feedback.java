package io.connectevent.connectevent.feedback.domain;

import io.connectevent.connectevent.event.domain.Event;
import io.connectevent.connectevent.participant.domain.Participant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Feedbacks")
@Getter
@Setter
public class Feedback {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @OneToOne
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant participant;

    public static Feedback of(Event event, Participant participant, int rating, String comment, LocalDateTime createdAt) {
        Feedback feedback = new Feedback();
        feedback.setEvent(event);
        feedback.setParticipant(participant);
        feedback.setRating(rating);
        feedback.setComment(comment);
        feedback.setCreatedAt(createdAt);
        return feedback;
    }

    public void update(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }
}
