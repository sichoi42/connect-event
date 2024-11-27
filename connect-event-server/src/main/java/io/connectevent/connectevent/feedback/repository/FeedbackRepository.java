package io.connectevent.connectevent.feedback.repository;

import io.connectevent.connectevent.feedback.domain.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

	@EntityGraph(attributePaths = {"participant", "participant.member"})
	@Query("SELECT f FROM Feedback f WHERE f.event.id = :eventId")
	Page<Feedback> findAllByEventId(Long eventId, Pageable pageable);
}
