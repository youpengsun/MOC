package com.sap.moc.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * get object from session
 * @author I322244
 *
 */
public class SessionUtil {
    
    /**
     * get userId from session
     * @param request
     * @return
     */
    public static String getUserInfo(HttpServletRequest request){
        Object userId = request.getSession().getAttribute("userId");
        if (userId==null) {
            return null;
        }
        return userId.toString();
    }

}
