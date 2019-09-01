package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.LoginStateFlowDao;
import co.arctern.api.provider.domain.Login;
import co.arctern.api.provider.domain.LoginStateFlow;
import co.arctern.api.provider.service.LoginStateFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class LoginStateFlowServiceImpl implements LoginStateFlowService {

    @Autowired
    LoginStateFlowDao loginStateFlowDao;

    @Transactional
    public LoginStateFlow createLoginStateFlow(Login login, Boolean status) {
        LoginStateFlow loginStateFlow = new LoginStateFlow();
        loginStateFlow.setLogin(login);
        loginStateFlow.setLoginState(status);
        return loginStateFlowDao.save(loginStateFlow);
    }
}
