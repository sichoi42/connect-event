package io.connectevent.connectevent.tag.domain;

import io.connectevent.connectevent.event.domain.EventTag;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Tags")
@Getter
@Setter
public class Tag {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "tag")
    private Set<EventTag> eventTags;

	public static Tag of(String tagName) {
		Tag tag = new Tag();
		tag.name = tagName;
		return tag;
	}
}
