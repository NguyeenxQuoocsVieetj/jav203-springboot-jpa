package poly.edu.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import poly.edu.dto.BookingForm;
import poly.edu.service.ConsultationService;
import poly.edu.service.FileStorageService;

@Controller
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final ConsultationService consultationService;
    private final FileStorageService fileStorageService;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("packages", consultationService.findActivePackages());
        model.addAttribute("consultants", consultationService.findActiveConsultants());
        model.addAttribute("timeSlots", consultationService.findTimeSlots());
        model.addAttribute("activeMenu", "booking");
        model.addAttribute("uploadTooLarge", false);
    }

    @GetMapping("/form")
    public String form(@RequestParam(required = false) Integer packageId, Model model) {
        BookingForm bookingForm = new BookingForm();
        bookingForm.setConsultationPackageId(packageId);
        model.addAttribute("pageTitle", "booking.title.form");
        model.addAttribute("bookingForm", bookingForm);
        return "booking/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("bookingForm") BookingForm bookingForm,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes,
                       Model model) throws IOException {
        validateSelections(bookingForm, bindingResult);
        validateAttachment(bookingForm, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "booking.title.form");
            return "booking/form";
        }

        String storedName = null;
        String originalName = null;
        MultipartFile attachment = bookingForm.getAttachment();
        if (attachment != null && !attachment.isEmpty()) {
            storedName = fileStorageService.store(attachment);
            originalName = attachment.getOriginalFilename();
        }
        consultationService.saveBooking(bookingForm, storedName, originalName);
        redirectAttributes.addFlashAttribute("messageKey", "booking.save.success");
        return "redirect:/booking/list";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("pageTitle", "booking.title.list");
        model.addAttribute("bookings", consultationService.findAllBookings());
        model.addAttribute("packageNames", consultationService.findPackageNames());
        model.addAttribute("consultantNames", consultationService.findConsultantNames());
        return "booking/list";
    }

    private void validateSelections(BookingForm form, BindingResult bindingResult) {
        if (form.getConsultationPackageId() != null
                && !consultationService.isActivePackage(form.getConsultationPackageId())) {
            bindingResult.rejectValue("consultationPackageId", "booking.package.inactive");
        }
        if (form.getConsultantId() != null
                && !consultationService.isActiveConsultant(form.getConsultantId())) {
            bindingResult.rejectValue("consultantId", "booking.consultant.inactive");
        }
        if (form.getPreferredTime() != null
                && !consultationService.findTimeSlots().contains(form.getPreferredTime())) {
            bindingResult.rejectValue("preferredTime", "booking.time.invalid");
        }
    }

    private void validateAttachment(BookingForm form, BindingResult bindingResult) {
        MultipartFile attachment = form.getAttachment();
        if (attachment != null && !attachment.isEmpty() && !fileStorageService.isAllowed(attachment)) {
            bindingResult.rejectValue("attachment", "booking.attachment.type");
        }
    }
}
