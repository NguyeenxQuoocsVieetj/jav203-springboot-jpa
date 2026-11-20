package poly.edu.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import poly.edu.service.ConsultationService;

@WebMvcTest(SystemController.class)
@Import(ConsultationService.class)
class SystemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void statusReturnsJsonWithCounts() throws Exception {
        mockMvc.perform(get("/system/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.totalPackages").value(6))
                .andExpect(jsonPath("$.totalBookings").value(17));
    }
}
