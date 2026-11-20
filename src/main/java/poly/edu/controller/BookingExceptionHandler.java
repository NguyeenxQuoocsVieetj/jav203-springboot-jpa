package poly.edu.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import lombok.RequiredArgsConstructor;
import poly.edu.dto.BookingForm;
import poly.edu.service.ConsultationService;

@ControllerAdvice(assignableTypes = BookingController.class)
@RequiredArgsConstructor
public class BookingExceptionHandler {

    private final ConsultationService consultationService;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleFileTooLarge(Model model) {
        model.addAttribute("pageTitle", "booking.title.form");
        model.addAttribute("bookingForm", new BookingForm());
        model.addAttribute("packages", consultationService.findActivePackages());
        model.addAttribute("consultants", consultationService.findActiveConsultants());
        model.addAttribute("timeSlots", consultationService.findTimeSlots());
        model.addAttribute("activeMenu", "booking");
        model.addAttribute("uploadTooLarge", true);
        return "booking/form";
    }
}
