package com.events.eventshub.controller;

import com.events.eventshub.entity.Event;
import com.events.eventshub.entity.User;
import com.events.eventshub.repository.EventRepository;
import com.events.eventshub.repository.UserRepository;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EventController {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/events")
    public String listEvents(Model model) {
        model.addAttribute("events", eventRepository.findAll());
        return "events";
    }

    @GetMapping("/admin/create-event")
    public String showEventForm(Model model) {
        model.addAttribute("event", new Event());
        return "create-event";
    }

    @PostMapping("/admin/create-event")
    public String createEvent(@ModelAttribute Event event) {
        eventRepository.save(event);
        return "redirect:/dashboard";
    }
    @PostMapping("/events/register/{id}")
    public String registerForEvent(@PathVariable Long id,
                                   Authentication authentication) {

        String email = authentication.name();

        User user = userRepository.findByEmail(email).orElseThrow();
        Event event = eventRepository.findById(id).orElseThrow();

        if (!user.getRegisteredEvents().contains(event)) {
            user.getRegisteredEvents().add(event);
        }

        userRepository.save(user);

        return "redirect:/events";
    }

}