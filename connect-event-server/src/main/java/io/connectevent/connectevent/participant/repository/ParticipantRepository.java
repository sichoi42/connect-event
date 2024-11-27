package io.connectevent.connectevent.participant.repository;

import io.connectevent.connectevent.participant.domain.Participant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ParticipantRepository extends JpaRepository<Participant, Long> {

	@Query("SELECT p FROM Participant p WHERE p.member.id = :memberId AND p.event.id = :eventId")
	Optional<Participant> findByMemberIdAndEventId(Long memberId, Long eventId);

	@Query("SELECT p FROM Participant p WHERE p.event.id = :eventId")
	List<Participant> findByEventId(Long eventId);

	@Query("SELECT p FROM Participant p JOIN FETCH p.member WHERE p.event.id = :eventId")
	List<Participant> findByEventIdWithMember(Long eventId);
}
