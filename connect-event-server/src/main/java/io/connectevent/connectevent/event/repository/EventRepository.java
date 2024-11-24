package io.connectevent.connectevent.event.repository;

import io.connectevent.connectevent.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventRepository extends JpaRepository<Event, Long> {
}
