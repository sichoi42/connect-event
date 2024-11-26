package io.connectevent.connectevent.event.repository;

import io.connectevent.connectevent.event.domain.Event;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface EventRepository extends JpaRepository<Event, Long> {

	@Query("SELECT e FROM Event e JOIN FETCH e.location l " +
			"JOIN FETCH e.eventTags et " +
			"JOIN FETCH et.tag t " +
			"WHERE e.id = :eventId")
	Optional<Event> findByIdWithLocationAndTags(Long eventId);
}
