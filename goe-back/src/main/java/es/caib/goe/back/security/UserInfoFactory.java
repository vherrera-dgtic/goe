package es.caib.goe.back.security;

import javax.servlet.http.HttpServletRequest;

public interface UserInfoFactory {

    UserInfo createUserInfo(HttpServletRequest request);

}
