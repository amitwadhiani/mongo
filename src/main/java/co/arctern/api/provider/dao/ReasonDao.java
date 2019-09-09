package co.arctern.api.provider.dao;

import co.arctern.api.provider.domain.Reason;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface ReasonDao extends PagingAndSortingRepository<Reason, Long> {

    List<Reason> findByIdIn(List<Long> reasonIds);
}
