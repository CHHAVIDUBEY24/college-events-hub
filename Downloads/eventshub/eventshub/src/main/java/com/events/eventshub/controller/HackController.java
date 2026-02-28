package com.events.eventshub.controller;

import com.events.eventshub.entity.HackPost;
import com.events.eventshub.entity.User;
import com.events.eventshub.repository.HackPostRepository;
import com.events.eventshub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class HackController {

    @Autowired
    private HackPostRepository hackRepo;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/hackfinder")
    public String listPosts(@ModelAttribute("skill") Optional<String> skill,
                            @ModelAttribute("dept") Optional<String> dept,
                            Model model) {
        List<HackPost> posts;
        if (dept.isPresent()) {
            posts = hackRepo.findByDepartment(dept.get());
        } else if (skill.isPresent()) {
            posts = hackRepo.findBySkillsContainingIgnoreCase(skill.get());
        } else {
            posts = hackRepo.findByOpenTrue();
        }
        model.addAttribute("posts", posts);
        return "hackfinder";
    }

    @GetMapping("/hackfinder/new")
    public String newPost(Model model) {
        model.addAttribute("post", new HackPost());
        return "hackform";
    }

    @PostMapping("/hackfinder")
    public String createPost(@ModelAttribute HackPost post, Principal p) {
        User author = userRepository.findByEmail(p.getName()).orElseThrow();
        post.setAuthor(author);
        hackRepo.save(post);
        return "redirect:/hackfinder";
    }
}