package com.events.eventshub.repository;

import com.events.eventshub.entity.HackPost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HackPostRepository extends JpaRepository<HackPost, Long> {
    List<HackPost> findByDepartment(String department);
    List<HackPost> findBySkillsContainingIgnoreCase(String skill);
    List<HackPost> findByOpenTrue();
}