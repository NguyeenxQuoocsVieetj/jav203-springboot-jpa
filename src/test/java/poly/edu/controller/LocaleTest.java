package poly.edu.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import poly.edu.service.ConsultationService;

@WebMvcTest(HomeController.class)
@Import(ConsultationService.class)
class LocaleTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void defaultLanguageIsVietnamese() throws Exception {
        mockMvc.perform(get("/home/index"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Gói tư vấn nổi bật")));
    }

    @Test
    void langParameterSwitchesToEnglish() throws Exception {
        mockMvc.perform(get("/home/index").param("lang", "en"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Featured consultation packages")))
                .andExpect(cookie().value("lang", "en"));
    }
}
