package co.arctern.api.provider.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HomePageResponseForAdmin {

    PaginatedResponse tasks;
}
