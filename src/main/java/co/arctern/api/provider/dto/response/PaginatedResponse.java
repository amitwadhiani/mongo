package co.arctern.api.provider.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
public class PaginatedResponse {

    public List<?> content;
    public Page<?> pageContent;
    public Integer page;
    public Integer size;
    public Integer totalPages;
    public Long totalElements;
}
