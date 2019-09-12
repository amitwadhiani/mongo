package co.arctern.api.provider.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * response for admin homepage.
 */
@Data
@NoArgsConstructor
public class HomePageResponseForAdmin {

    PaginatedResponse tasks;
}
