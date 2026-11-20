package poly.edu.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import poly.edu.entity.ConsultationPackage;
import poly.edu.service.ConsultationService;

@Controller
@RequestMapping("/consultation")
@RequiredArgsConstructor
public class ConsultationController {

    private static final int RELATED_LIMIT = 3;

    private final ConsultationService consultationService;

    @GetMapping("/list")
    public String list(Model model) {
        return renderList(model, "consultation.title.all",
                consultationService.findActivePackages(), "", null, null, null);
    }

    @GetMapping("/search")
    public String search(@RequestParam(defaultValue = "") String keyword, Model model) {
        String trimmed = keyword.trim();
        return renderList(model, "consultation.title.search",
                consultationService.searchByKeyword(trimmed), trimmed, null, null, null);
    }

    @GetMapping("/category/{id}")
    public String category(@PathVariable String id, Model model) {
        return renderList(model, "consultation.title.category",
                consultationService.findByCategory(id), "", id, null, null);
    }

    @GetMapping("/search-price")
    public String searchByPrice(@RequestParam Optional<Double> min,
                                @RequestParam Optional<Double> max,
                                Model model) {
        Double minPrice = min.orElse(null);
        Double maxPrice = max.orElse(null);
        return renderList(model, "consultation.title.price",
                consultationService.findByPriceRange(minPrice, maxPrice), "", null, minPrice, maxPrice);
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        ConsultationPackage pkg = consultationService.findPackageById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy gói tư vấn"));
        model.addAttribute("pkg", pkg);
        model.addAttribute("consultant", consultationService.findConsultantById(pkg.getConsultantId()).orElse(null));
        model.addAttribute("related",
                consultationService.findRelatedPackages(pkg.getCategoryId(), pkg.getId(), RELATED_LIMIT));
        model.addAttribute("categoryNames", consultationService.findCategoryNames());
        model.addAttribute("activeMenu", "consultation");
        return "consultation/detail";
    }

    private String renderList(Model model, String pageTitle, List<ConsultationPackage> packages,
                              String keyword, String categoryId, Double minPrice, Double maxPrice) {
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("packages", packages);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("categories", consultationService.findCategories());
        model.addAttribute("categoryNames", consultationService.findCategoryNames());
        model.addAttribute("activeMenu", "consultation");
        return "consultation/list";
    }
}
