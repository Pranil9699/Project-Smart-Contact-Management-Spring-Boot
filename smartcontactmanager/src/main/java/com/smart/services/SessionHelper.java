package com.smart.services;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Component
public class SessionHelper {

	public void removeSessionAttribute(String var) {
		try {
			
			HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
					.getSession();
			session.removeAttribute(var);
			System.out.println("Remove the Session Variable : "+var);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
