package poly.edu.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import poly.edu.service.ConsultationService;
import poly.edu.service.FileStorageService;

@TestPropertySource(properties = "app.upload-dir=target/test-uploads")
@WebMvcTest(BookingController.class)
@Import({ConsultationService.class, FileStorageService.class})
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static String futureDate() {
        return LocalDate.now().plusDays(7).toString();
    }

    private static MockHttpServletRequestBuilder validBooking(MockHttpServletRequestBuilder request) {
        return request
                .param("fullName", "Nguyễn Văn A")
                .param("email", "a@example.com")
                .param("phone", "0912345678")
                .param("consultationPackageId", "2")
                .param("consultantId", "2")
                .param("preferredDate", futureDate())
                .param("preferredTime", "08:00 - 10:00");
    }

    private static MockMultipartHttpServletRequestBuilder validBooking(MockMultipartHttpServletRequestBuilder request) {
        return request
                .param("fullName", "Nguyễn Văn A")
                .param("email", "a@example.com")
                .param("phone", "0912345678")
                .param("consultationPackageId", "2")
                .param("consultantId", "2")
                .param("preferredDate", futureDate())
                .param("preferredTime", "08:00 - 10:00");
    }

    @Test
    void formPreselectsPackageFromQuery() throws Exception {
        mockMvc.perform(get("/booking/form").param("packageId", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name("booking/form"))
                .andExpect(content().string(containsString("Đặt lịch tư vấn")));
    }

    @Test
    void saveStoresBookingAndRedirectsToList() throws Exception {
        mockMvc.perform(validBooking(post("/booking/save")).param("note", "Ghi chú"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/booking/list"))
                .andExpect(flash().attribute("messageKey", "booking.save.success"));

        mockMvc.perform(get("/booking/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("booking/list"))
                .andExpect(content().string(containsString("Nguyễn Văn A")))
                .andExpect(content().string(containsString("Mới")));
    }

    @Test
    void saveStoresAttachmentWithGeneratedName() throws Exception {
        MockMultipartFile attachment = new MockMultipartFile(
                "attachment", "cv.pdf", "application/pdf", "noi-dung".getBytes());

        mockMvc.perform(validBooking(multipart("/booking/save").file(attachment)))
                .andExpect(redirectedUrl("/booking/list"));

        mockMvc.perform(get("/booking/list"))
                .andExpect(content().string(containsString("cv.pdf")));

        try (Stream<Path> files = Files.list(Path.of("target/test-uploads"))) {
            assertTrue(files.anyMatch(p -> p.getFileName().toString().endsWith(".pdf")));
        }
    }

    @Test
    void emptySubmitShowsFieldErrors() throws Exception {
        mockMvc.perform(post("/booking/save"))
                .andExpect(status().isOk())
                .andExpect(view().name("booking/form"))
                .andExpect(content().string(containsString("Họ tên không được để trống")))
                .andExpect(content().string(containsString("Email không được để trống")))
                .andExpect(content().string(containsString("Vui lòng kiểm tra lại thông tin bên dưới.")));
    }

    @Test
    void invalidInputKeepsEnteredValues() throws Exception {
        mockMvc.perform(post("/booking/save")
                        .param("fullName", "Nguyễn Văn C")
                        .param("email", "sai-email")
                        .param("phone", "123")
                        .param("consultationPackageId", "2")
                        .param("consultantId", "2")
                        .param("preferredDate", futureDate())
                        .param("preferredTime", "08:00 - 10:00"))
                .andExpect(status().isOk())
                .andExpect(view().name("booking/form"))
                .andExpect(content().string(containsString("Email không đúng định dạng")))
                .andExpect(content().string(containsString("Số điện thoại phải gồm 10-11 chữ số")))
                .andExpect(content().string(containsString("value=\"Nguyễn Văn C\"")));
    }

    @Test
    void pastDateIsRejected() throws Exception {
        mockMvc.perform(post("/booking/save")
                        .param("fullName", "Nguyễn Văn D")
                        .param("email", "d@example.com")
                        .param("phone", "0912345678")
                        .param("consultationPackageId", "2")
                        .param("consultantId", "2")
                        .param("preferredDate", LocalDate.now().minusDays(1).toString())
                        .param("preferredTime", "08:00 - 10:00"))
                .andExpect(view().name("booking/form"))
                .andExpect(content().string(containsString("Ngày mong muốn không được là ngày quá khứ")));
    }

    @Test
    void inactiveConsultantIsRejected() throws Exception {
        mockMvc.perform(post("/booking/save")
                        .param("fullName", "Nguyễn Văn E")
                        .param("email", "e@example.com")
                        .param("phone", "0912345678")
                        .param("consultationPackageId", "2")
                        .param("consultantId", "5")
                        .param("preferredDate", futureDate())
                        .param("preferredTime", "08:00 - 10:00"))
                .andExpect(view().name("booking/form"))
                .andExpect(content().string(containsString("Chuyên viên không khả dụng")));
    }

    @Test
    void noteOverLimitIsRejected() throws Exception {
        mockMvc.perform(validBooking(post("/booking/save")).param("note", "a".repeat(256)))
                .andExpect(view().name("booking/form"))
                .andExpect(content().string(containsString("Ghi chú tối đa 255 ký tự")));
    }

    @Test
    void unsupportedAttachmentIsRejected() throws Exception {
        MockMultipartFile attachment = new MockMultipartFile(
                "attachment", "malware.exe", "application/octet-stream", "noi-dung".getBytes());

        mockMvc.perform(validBooking(multipart("/booking/save").file(attachment)))
                .andExpect(view().name("booking/form"))
                .andExpect(content().string(containsString("Chỉ chấp nhận file pdf, jpg, jpeg, png, docx")));
    }
}
