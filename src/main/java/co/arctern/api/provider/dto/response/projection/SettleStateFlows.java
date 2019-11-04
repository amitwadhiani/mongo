package co.arctern.api.provider.dto.response.projection;

import co.arctern.api.provider.domain.SettleStateFlow;
import org.springframework.data.rest.core.config.Projection;

@Projection(types = {SettleStateFlow.class})
public interface SettleStateFlows {
}
