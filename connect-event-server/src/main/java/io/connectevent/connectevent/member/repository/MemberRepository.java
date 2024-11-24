package io.connectevent.connectevent.member.repository;

import io.connectevent.connectevent.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByEmail(String email);
}
