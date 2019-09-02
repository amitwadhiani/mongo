package co.arctern.api.provider.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleRequestDto {

    private Long id;
    private String role;
    private String description;
}
