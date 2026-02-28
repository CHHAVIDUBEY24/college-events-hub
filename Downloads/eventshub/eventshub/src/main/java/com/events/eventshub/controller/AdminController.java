package com.events.eventshub.controller;

import com.events.eventshub.entity.Event;
import com.events.eventshub.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@Secured("ROLE_ADMIN")
public class AdminController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/queue")
    public String moderationQueue(Model model) {
        model.addAttribute("events", eventRepository.findByApprovedFalse());
        return "admin/queue";
    }

    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id) {
        Event e = eventRepository.findById(id).orElseThrow();
        e.setApproved(true);
        eventRepository.save(e);
        return "redirect:/admin/queue";
    }
}