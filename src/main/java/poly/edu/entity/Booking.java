package poly.edu.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.edu.entity.Enum.BookingStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private Integer id;
    private Integer packageId;
    private Integer consultantId;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate preferredDate;
    private String preferredTime;
    private String note;
    private BookingStatus status;
    private String attachmentFileName;
    private String attachmentOriginalName;
}
