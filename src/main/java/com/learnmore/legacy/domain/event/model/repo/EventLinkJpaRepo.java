package com.learnmore.legacy.domain.event.model.repo;

import com.learnmore.legacy.domain.event.model.Event;
import com.learnmore.legacy.domain.event.model.EventLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventLinkJpaRepo extends JpaRepository<EventLink, Long> {
    List<EventLink> findByEvent(Event event);
}
