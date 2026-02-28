package com.events.eventshub.controller;

import com.events.eventshub.entity.Event;
import com.events.eventshub.entity.User;
import com.events.eventshub.repository.EventRepository;
import com.events.eventshub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class EventController {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/events")
    public String listEvents(@RequestParam Optional<String> dept,
                             @RequestParam Optional<String> q,
                             Model model) {
        List<Event> events;
        if (dept.isPresent()) {
            events = eventRepository.findByDepartment(dept.get());
        } else if (q.isPresent()) {
            events = eventRepository.findByTitleContainingIgnoreCase(q.get());
        } else {
            events = eventRepository.findAll();
        }
        // only show approved events for non-admins
        events.removeIf(e -> !e.isApproved());
        model.addAttribute("events", events);
        return "events";
    }

    @GetMapping("/admin/create-event")
    public String showEventForm(Model model) {
        model.addAttribute("event", new Event());
        return "create-event";
    }

    @PostMapping("/admin/create-event")
    public String createEvent(@ModelAttribute Event event) {
        event.setApproved(false);
        eventRepository.save(event);
        return "redirect:/dashboard";
    }
    @PostMapping("/events/register/{id}")
    public String registerForEvent(@PathVariable Long id,
                                   Principal principal) {

        String email = principal.getName();

        User user = userRepository.findByEmail(email).orElseThrow();
        Event event = eventRepository.findById(id).orElseThrow();

        if (!user.getRegisteredEvents().contains(event)) {
            user.getRegisteredEvents().add(event);
        }

        userRepository.save(user);

        return "redirect:/events";
    }

}