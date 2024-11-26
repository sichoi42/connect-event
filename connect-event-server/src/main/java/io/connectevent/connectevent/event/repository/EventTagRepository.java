package io.connectevent.connectevent.event.repository;

import io.connectevent.connectevent.event.domain.EventTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface EventTagRepository extends JpaRepository<EventTag, Long> {

	@Query("SELECT et FROM EventTag et JOIN FETCH et.tag t WHERE et.event.id = :eventId")
	List<EventTag> findByEventId(Long eventId);
}
