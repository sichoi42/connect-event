package io.connectevent.connectevent.participant.repository;

import io.connectevent.connectevent.participant.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
