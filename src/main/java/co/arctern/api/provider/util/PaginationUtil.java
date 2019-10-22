package co.arctern.api.provider.util;

import co.arctern.api.provider.dto.response.PaginatedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Pagination utility class for making list calls -> paginated.
 */
public interface PaginationUtil {

    /**
     * to paginate a list and return list in return.
     *
     * @param sourceList
     * @param page
     * @param pageSize
     * @param <T>
     * @return
     */
    public static <T> List<T> paginateList(List<T> sourceList, int page, int pageSize) {
        if (pageSize <= 0 || page < 0) {
            throw new IllegalArgumentException("Invalid page size/number ");
        }
        int fromIndex = (page) * pageSize;
        if (sourceList == null || sourceList.size() < fromIndex) {
            return Collections.emptyList();
        }
        return sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size()));
    }

    /**
     * return paginated response from list.
     *
     * @param sourceList
     * @param page
     * @param pageSize
     * @return
     */
    public static PaginatedResponse returnPaginatedBody(List<?> sourceList, Integer page, Integer pageSize, Integer totalElements) {
        PaginatedResponse response = new PaginatedResponse();
        response.setContent(paginateList(sourceList, page, pageSize));
        response.setPage(page);
        response.setSize(pageSize);
        response.setTotalElements(Long.valueOf(totalElements));
        response.setTotalPages((int) Math.ceil((double) totalElements / pageSize));
        return response;
    }

    /**
     * creating a paginated response from repo page call.
     *
     * @param page
     * @param pageable
     * @return
     */
    public static PaginatedResponse returnPaginatedBody(Page<?> page, Pageable pageable) {
        PaginatedResponse response = new PaginatedResponse();
        response.setContent(page.getContent());
        response.setPage(pageable.getPageNumber());
        response.setSize(pageable.getPageSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        return response;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
