package co.arctern.api.provider.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * generic paginated response.
 */
@Data
@NoArgsConstructor
public class PaginatedResponse {

    private List<?> content;
    private Page<?> pageContent;
    private Integer page;
    private Integer size;
    private Integer totalPages;
    private Long totalElements;
}
