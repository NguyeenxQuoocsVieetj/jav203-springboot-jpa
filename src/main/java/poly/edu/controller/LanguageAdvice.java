package poly.edu.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@ControllerAdvice
public class LanguageAdvice {

    @ModelAttribute("viUrl")
    public String viUrl() {
        return switchLanguageUrl("vi");
    }

    @ModelAttribute("enUrl")
    public String enUrl() {
        return switchLanguageUrl("en");
    }

    private String switchLanguageUrl(String lang) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .replaceQueryParam("lang", lang)
                .build()
                .toUriString();
    }
}
