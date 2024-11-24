package io.connectevent.connectevent.feedback.repository;

import io.connectevent.connectevent.feedback.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
