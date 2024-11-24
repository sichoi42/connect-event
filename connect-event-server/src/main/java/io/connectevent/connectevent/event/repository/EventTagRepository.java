package io.connectevent.connectevent.event.repository;

import io.connectevent.connectevent.event.domain.EventTag;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventTagRepository extends JpaRepository<EventTag, Long> {
}
