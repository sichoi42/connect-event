package io.connectevent.connectevent.tag.repository;

import io.connectevent.connectevent.tag.domain.Tag;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface TagRepository extends JpaRepository<Tag, Long> {

	@Query("SELECT t FROM Tag t WHERE t.name LIKE %:query%")
	Page<Tag> findAllByNameContaining(String query, Pageable pageable);

	@Query("SELECT t FROM Tag t WHERE t.name = :tagName")
	Optional<Tag> findByName(String tagName);
}
