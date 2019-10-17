package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.Cluster;
import co.arctern.api.provider.dto.request.ClusterRequestDto;
import co.arctern.api.provider.dto.response.projection.Clusters;
import co.arctern.api.provider.util.MessageUtil;
import com.amazonaws.adapters.types.StringToByteBufferAdapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClusterService extends MessageUtil {

    public Cluster fetchById(Long id);

    public Page<Clusters> fetchAll(Pageable pageable);

    public StringBuilder createClusters(List<ClusterRequestDto> dtos);


}
