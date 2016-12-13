package com.sap.moc.controller.advice;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ModelAndView handleException(Exception exception, HttpServletRequest request, HttpServletResponse response) throws IOException {

		
		/*if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
			PrintWriter writer = response.getWriter();  
            writer.write(exception.getMessage());  
            writer.flush();  
			return null;
		}
		else{
			ModelAndView mv = new ModelAndView();

			mv.setViewName("errorpage");
			mv.getModel().put("errorMessage", exception.getMessage());
			return mv;
		}*/
		
		ModelAndView mv = new ModelAndView();

		mv.setViewName("errorpage");
		mv.getModel().put("errorMessage", exception.getMessage());
		return mv;
		
	}

}
