package poly.edu.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import poly.edu.service.ConsultationService;

@WebMvcTest(ConsultationController.class)
@Import(ConsultationService.class)
class ConsultationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listShowsOnlyActivePackages() throws Exception {
        mockMvc.perform(get("/consultation/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("consultation/list"))
                .andExpect(content().string(containsString("Định hướng nghề IT")))
                .andExpect(content().string(not(containsString("Gói cũ đã ngừng"))));
    }

    @Test
    void detailShowsPackageAndRelatedPackages() throws Exception {
        mockMvc.perform(get("/consultation/detail/2"))
                .andExpect(status().isOk())
                .andExpect(view().name("consultation/detail"))
                .andExpect(content().string(containsString("Định hướng nghề IT")))
                .andExpect(content().string(containsString("Định hướng du học")));
    }

    @Test
    void detailReturnsNotFoundForUnknownPackage() throws Exception {
        mockMvc.perform(get("/consultation/detail/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void detailReturnsNotFoundForInactivePackage() throws Exception {
        mockMvc.perform(get("/consultation/detail/7"))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchFiltersByKeyword() throws Exception {
        mockMvc.perform(get("/consultation/search").param("keyword", "cv"))
                .andExpect(status().isOk())
                .andExpect(view().name("consultation/list"))
                .andExpect(content().string(containsString("Chuẩn bị CV và phỏng vấn")))
                .andExpect(content().string(not(containsString("Định hướng nghề IT"))));
    }

    @Test
    void categoryFiltersByCategoryId() throws Exception {
        mockMvc.perform(get("/consultation/category/SKILL"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Xây dựng thương hiệu cá nhân")))
                .andExpect(content().string(not(containsString("Định hướng nghề IT"))));
    }

    @Test
    void priceFilterUsesInclusiveRange() throws Exception {
        mockMvc.perform(get("/consultation/search-price").param("min", "300000").param("max", "400000"))
                .andExpect(status().isOk())
                .andExpect(view().name("consultation/list"))
                .andExpect(content().string(containsString("Tư vấn chọn ngành học")))
                .andExpect(content().string(containsString("Chuẩn bị CV và phỏng vấn")))
                .andExpect(content().string(not(containsString("Định hướng du học"))));
    }

    @Test
    void priceFilterAcceptsMissingBound() throws Exception {
        mockMvc.perform(get("/consultation/search-price").param("min", "700000"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Định hướng du học")))
                .andExpect(content().string(not(containsString("Tư vấn chọn ngành học"))));
    }

    @Test
    void emptyResultShowsMessage() throws Exception {
        mockMvc.perform(get("/consultation/search").param("keyword", "không-tồn-tại"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Không tìm thấy gói tư vấn phù hợp.")));
    }
}
