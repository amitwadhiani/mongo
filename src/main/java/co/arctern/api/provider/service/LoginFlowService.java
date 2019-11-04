package co.arctern.api.provider.service;

import co.arctern.api.provider.constant.LoginState;
import co.arctern.api.provider.domain.Login;
import co.arctern.api.provider.domain.LoginFlow;
import co.arctern.api.provider.util.MessageUtil;

public interface LoginFlowService extends MessageUtil {

    public LoginFlow create(Login login, LoginState state);

}
