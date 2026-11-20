package poly.edu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationPackage {
    private Integer id;
    private String name;
    private String categoryId;
    private String image;
    private Double price;
    private Integer duration;
    private String description;
    private Boolean active;
    private Integer consultantId;
}
