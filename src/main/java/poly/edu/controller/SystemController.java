package poly.edu.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import poly.edu.service.ConsultationService;

@Controller
@RequiredArgsConstructor
public class SystemController {

    private final ConsultationService consultationService;

    @GetMapping("/system/status")
    @ResponseBody
    public Map<String, Object> status() {
        return Map.of(
                "status", "UP",
                "time", LocalDateTime.now().toString(),
                "totalPackages", consultationService.countActivePackages(),
                "totalBookings", consultationService.countBookings());
    }
}
