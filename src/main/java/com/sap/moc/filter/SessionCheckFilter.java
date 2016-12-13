package com.sap.moc.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.moc.utils.ConstantReader;

/**
* Servlet Filter implementation class SessionCheckFilter
*/
@WebFilter(urlPatterns={"/mobile/vendor/*",
		                "/mobile/employee/dailyrecords.html",
		                "/mobile/pagemapping/*"})  //page mapping to skip buffer
public class SessionCheckFilter implements Filter {

    /**
     * Default constructor.
     */
    public SessionCheckFilter() { 
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
      
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String url = ConstantReader.readByKey("AUTHORIZTION_URL");
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		Object attribute = httpRequest.getSession().getAttribute("UserId");
		response.setContentType("text/html;charset=UTF-8");
		if (attribute != null && attribute.toString().length() > 0) {
			chain.doFilter(request, response);
		} else {
			String corpId = ConstantReader.readSysParaByKey("CORPID");
			String requestedURL = httpRequest.getRequestURL().toString();
			String servlet_path = httpRequest.getServletPath();
			String authorize_url = requestedURL.replaceAll(servlet_path, "/authorization");
			@SuppressWarnings("deprecation")
			Object[] placeStr = { corpId, URLEncoder.encode(authorize_url), URLEncoder.encode(servlet_path) };
			String finalURL = MessageFormat.format(url, placeStr);
			// redirect to OAuth URL
			((HttpServletResponse) response).sendRedirect(finalURL);
		}
	}

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        
    }

}
