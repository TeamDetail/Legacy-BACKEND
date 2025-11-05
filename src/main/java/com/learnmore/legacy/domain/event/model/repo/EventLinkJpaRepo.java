package com.learnmore.legacy.domain.event.model.repo;

import com.learnmore.legacy.domain.event.model.EventLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventLinkJpaRepo extends JpaRepository<EventLink, Long> {
}
