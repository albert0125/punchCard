package com.smi.punch.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import com.smi.punch.IContent;
import com.smi.punch.utils.MyCookieManager;



@Controller
public class LoginOutPageController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * @param session
	 * @param model
	 * @param req
	 * @param res
	 * @return
	 */
	@GetMapping("login_page.html")
	public String login_page(HttpSession session,Model model, HttpServletRequest req, HttpServletResponse res) {

		String dateStr = req.getParameter("dateStr");
		MyCookieManager mcm = new MyCookieManager(req, res);
		Map<String,String> info = mcm.readLoginCookie();
		if(!info.isEmpty()){
			model.addAttribute("username", info.get("username"));
			model.addAttribute("password", info.get("password"));		
		}
		if(StringUtils.isNotBlank(dateStr)){
			session.setAttribute(IContent._session_date_req, dateStr);
		}
		session.setAttribute("_LOGIN_TYPE", "FORMBASE");
		return "login";
		
	}

	@GetMapping("logout_page.html")
	public String logout_page(HttpSession session) {
		logger.info("_LOGIN_TYPE:{}",session.getAttribute("_LOGIN_TYPE"));
		return "logout";
	}
	
}
