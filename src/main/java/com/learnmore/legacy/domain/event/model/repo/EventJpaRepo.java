package com.learnmore.legacy.domain.event.model.repo;

import com.learnmore.legacy.domain.event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventJpaRepo extends JpaRepository<Event, Long> {
}
