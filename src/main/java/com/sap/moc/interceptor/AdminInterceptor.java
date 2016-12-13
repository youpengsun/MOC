package com.sap.moc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sap.moc.utils.ConstantReader;
import com.sap.moc.utils.UserLoginUtil;

public class AdminInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String loginPage = ConstantReader.readByKey("USER_LOGIN_PAGE");
		// for temp testing variables


		if (request.getServletPath().startsWith(loginPage)) {
			return true;
		}
		String username = (String) request.getSession().getAttribute("username");
		String role = (String) request.getSession().getAttribute("role");
		if (username != null && UserLoginUtil.LOGIN_ROLE_ADMIN.equalsIgnoreCase(role)) {
			return true;
		} else {
			
			
			response.sendRedirect(request.getContextPath() + loginPage);
//			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
/*			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);*/
			return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
