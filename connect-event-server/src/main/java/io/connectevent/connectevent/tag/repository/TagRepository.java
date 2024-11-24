package io.connectevent.connectevent.tag.repository;

import io.connectevent.connectevent.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TagRepository extends JpaRepository<Tag, Long> {
}
