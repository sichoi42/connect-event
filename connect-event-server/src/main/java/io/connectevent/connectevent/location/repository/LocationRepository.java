package io.connectevent.connectevent.location.repository;

import io.connectevent.connectevent.location.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationRepository extends JpaRepository<Location, Long> {
}
