package poly.edu.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import poly.edu.entity.Consultant;
import poly.edu.entity.ConsultationPackage;
import poly.edu.service.ConsultationService;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConsultationService consultationService;

    @Test
    void indexRendersDataFromService() throws Exception {
        ConsultationPackage pkg = ConsultationPackage.builder()
                .id(2).name("Định hướng nghề IT").description("Lộ trình nghề nghiệp")
                .price(500000.0).duration(90).active(true).consultantId(2).build();
        Consultant consultant = Consultant.builder()
                .id(2).fullName("Trần Quốc Huy").specialty("Lập trình").active(true).build();

        when(consultationService.findFeaturedPackages(anyInt())).thenReturn(List.of(pkg));
        when(consultationService.findFeaturedConsultants(anyInt())).thenReturn(List.of(consultant));
        when(consultationService.countActivePackages()).thenReturn(6L);
        when(consultationService.countBookings()).thenReturn(17L);

        mockMvc.perform(get("/home/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("home/index"))
                .andExpect(model().attribute("totalPackages", 6L))
                .andExpect(model().attribute("totalBookings", 17L))
                .andExpect(content().string(containsString("Định hướng nghề IT")))
                .andExpect(content().string(containsString("Trần Quốc Huy")))
                .andExpect(content().string(containsString("CareerPath")));
    }

    @Test
    void indexShowsEmptyMessageWhenNoData() throws Exception {
        mockMvc.perform(get("/home/index"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hiện chưa có gói tư vấn nào.")));
    }

    @Test
    void rootPathRendersHomeIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home/index"));
    }

    @Test
    void aboutPageRenders() throws Exception {
        mockMvc.perform(get("/home/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("home/about"))
                .andExpect(content().string(containsString("Giới thiệu")));
    }

    @Test
    void helpPageRenders() throws Exception {
        mockMvc.perform(get("/home/help"))
                .andExpect(status().isOk())
                .andExpect(view().name("home/help"))
                .andExpect(content().string(containsString("Trợ giúp")));
    }
}
