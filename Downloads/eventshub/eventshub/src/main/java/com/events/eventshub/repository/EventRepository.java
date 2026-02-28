package com.events.eventshub.repository;

import com.events.eventshub.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByDepartment(String department);
    List<Event> findByTitleContainingIgnoreCase(String keyword);
    List<Event> findByApprovedFalse();
}


