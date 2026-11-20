package poly.edu.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookingForm {

    @NotBlank(message = "{validation.fullName.notBlank}")
    @Size(max = 100, message = "{validation.fullName.size}")
    private String fullName;

    @NotBlank(message = "{validation.email.notBlank}")
    @Email(message = "{validation.email.invalid}")
    private String email;

    @NotBlank(message = "{validation.phone.notBlank}")
    @Pattern(regexp = "^\\d{10,11}$", message = "{validation.phone.pattern}")
    private String phone;

    @NotNull(message = "{validation.package.notNull}")
    private Integer consultationPackageId;

    @NotNull(message = "{validation.consultant.notNull}")
    private Integer consultantId;

    @NotNull(message = "{validation.date.notNull}")
    @FutureOrPresent(message = "{validation.date.past}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate preferredDate;

    @NotBlank(message = "{validation.time.notBlank}")
    private String preferredTime;

    @Size(max = 255, message = "{validation.note.size}")
    private String note;

    private MultipartFile attachment;
}
