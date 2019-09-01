package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.Login;
import co.arctern.api.provider.domain.LoginStateFlow;
import co.arctern.api.provider.util.MessageUtil;

public interface LoginStateFlowService extends MessageUtil {

    public LoginStateFlow createLoginStateFlow(Login login, Boolean status);
}
