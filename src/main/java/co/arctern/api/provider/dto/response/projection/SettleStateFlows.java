package co.arctern.api.provider.dto.response.projection;

import co.arctern.api.provider.constant.SettleState;
import co.arctern.api.provider.domain.SettleStateFlow;
import org.springframework.data.rest.core.config.Projection;

import java.sql.Timestamp;

/**
 * SettleStateFlow response body.
 */
@Projection(types = {SettleStateFlow.class})
public interface SettleStateFlows {

    Long getId();

    Timestamp getCreatedAt();

    Timestamp getLastModifiedAt();

    SettleState getState();

}
