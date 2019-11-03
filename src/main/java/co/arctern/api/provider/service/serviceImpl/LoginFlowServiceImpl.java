package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.LoginState;
import co.arctern.api.provider.dao.LoginFlowDao;
import co.arctern.api.provider.domain.Login;
import co.arctern.api.provider.domain.LoginFlow;
import co.arctern.api.provider.service.LoginFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginFlowServiceImpl implements LoginFlowService {

    private final LoginFlowDao loginFlowDao;

    @Autowired
    public LoginFlowServiceImpl(LoginFlowDao loginFlowDao) {
        this.loginFlowDao = loginFlowDao;
    }

    @Override
    public LoginFlow create(Login login, LoginState state) {
        LoginFlow loginFlow = new LoginFlow();
        loginFlow.setLogin(login);
        loginFlow.setLoginState(state);
        return loginFlowDao.save(loginFlow);
    }

}
