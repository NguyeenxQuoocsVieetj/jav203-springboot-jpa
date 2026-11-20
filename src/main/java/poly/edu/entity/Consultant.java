package poly.edu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Consultant {
    private Integer id;
    private String fullName;
    private String specialty;
    private Boolean active;
}
