package co.arctern.api.provider.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * cluster create/edit dto.
 */
@Data
@NoArgsConstructor
public class ClusterRequestDto {

    private String clusterName;
    private Long clusterId;
    private Boolean isActive;
    private List<AreaRequestDto> areas;
    private List<Long> areaIds;
    private List<String> pinCodes;
    private Boolean isEdit;
}
