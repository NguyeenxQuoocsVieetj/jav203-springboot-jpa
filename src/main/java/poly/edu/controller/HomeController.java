package poly.edu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import poly.edu.service.ConsultationService;

@Controller
@RequestMapping({"/home", "/"})
@RequiredArgsConstructor
public class HomeController {

    private static final String SYSTEM_NAME = "CareerPath Booking Portal";
    private static final int FEATURED_LIMIT = 3;

    private final ConsultationService consultationService;

    @GetMapping({"", "/index"})
    public String index(Model model) {
        model.addAttribute("systemName", SYSTEM_NAME);
        model.addAttribute("activeMenu", "index");
        model.addAttribute("featuredPackages", consultationService.findFeaturedPackages(FEATURED_LIMIT));
        model.addAttribute("featuredConsultants", consultationService.findFeaturedConsultants(FEATURED_LIMIT));
        model.addAttribute("totalPackages", consultationService.countActivePackages());
        model.addAttribute("totalBookings", consultationService.countBookings());
        return "home/index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("systemName", SYSTEM_NAME);
        model.addAttribute("activeMenu", "about");
        return "home/about";
    }

    @GetMapping("/help")
    public String help(Model model) {
        model.addAttribute("systemName", SYSTEM_NAME);
        model.addAttribute("activeMenu", "help");
        return "home/help";
    }
}
