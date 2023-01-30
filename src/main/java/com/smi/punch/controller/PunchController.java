package com.smi.punch.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smi.punch.IContent;
import com.smi.punch.service.AuthorizeServiceClient;
import com.smi.punch.service.OGServiceClient;
import com.smi.punch.service.PunchServiceClient;
import com.smi.punch.utils.MyUtils;
import com.smi.punch.vo.EhrEmpVO;
import com.smi.punch.vo.PunchRecordVO;

@Controller
public class PunchController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private AuthorizeServiceClient authorizeClient;

	@Autowired
	private PunchServiceClient punchService;

	@RequestMapping(value = { "", "/index.html" },method = RequestMethod.GET)
	public String index(HttpSession session, Model model, @RequestParam(required = false) String calDate,HttpServletResponse res)
			throws JsonProcessingException, ParseException {
		logger.info("_LOGIN_TYPE:{}",session.getAttribute(IContent._login_type));
		Calendar cal = Calendar.getInstance();
		Calendar todayCal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm");
		Date targetDate = cal.getTime();

		if(null!=session.getAttribute(IContent._session_date_req) && null == calDate ) {
			calDate = (String)session.getAttribute(IContent._session_date_req);
		}
		if (StringUtils.isNotBlank(calDate)) {
			targetDate = sdf.parse(calDate);
			cal.setTime(targetDate);
			calDate = StringUtils.replace(calDate, "/", "-")  ;
			session.setAttribute(IContent._session_date_req,calDate);
		} else {
			calDate = sdf.format(targetDate);
			session.setAttribute(IContent._session_date_req,calDate);
		}
		model.addAttribute("eWeekDay",IContent._e_weekday[cal.get(Calendar.DAY_OF_WEEK)-1]);
		model.addAttribute("cWeekDay",IContent._c_weekday[cal.get(Calendar.DAY_OF_WEEK)-1]);
		model.addAttribute("isToday", cal.get(Calendar.DAY_OF_YEAR) == todayCal.get(Calendar.DAY_OF_YEAR));
		
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		logger.info("[principal] principal={}",principal);
		String username = null;
		EhrEmpVO empVO = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		if (!"".equals(username) && null == session.getAttribute("_USER")) {
			empVO = authorizeClient.getEmpVO(username);
			session.setAttribute("_USER", empVO);
		} else {
			empVO = (EhrEmpVO) session.getAttribute("_USER");
		}

		model.addAttribute("user", empVO);
		model.addAttribute("loginType", session.getAttribute(IContent._login_type));
		model.addAttribute("calDate", calDate);
		return "indexA";
	}

	@RequestMapping(value = {"/otherReason.html" },method = RequestMethod.GET)
	public String otherReason(HttpSession session, Model model, @RequestParam(required = false) String calDate)
			throws JsonProcessingException, ParseException {
		logger.info("_LOGIN_TYPE:{}",session.getAttribute(IContent._login_type));
		Calendar cal = Calendar.getInstance();
		Calendar todayCal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date targetDate = cal.getTime();
		
		if(null!=session.getAttribute(IContent._session_date_req) && null == calDate )  {
			calDate = (String)session.getAttribute(IContent._session_date_req);
		}
		if (StringUtils.isNotBlank(calDate)) {
			targetDate = sdf.parse(calDate);
			cal.setTime(targetDate);
			calDate = StringUtils.replace(calDate, "/", "-")  ;
			session.setAttribute(IContent._session_date_req,calDate);
		} else {
			calDate = sdf.format(targetDate);
			session.setAttribute(IContent._session_date_req,calDate);
		}
		
		
		model.addAttribute("loginType", session.getAttribute(IContent._login_type));
		model.addAttribute("eWeekDay",IContent._e_weekday[cal.get(Calendar.DAY_OF_WEEK)-1]);
		model.addAttribute("cWeekDay",IContent._c_weekday[cal.get(Calendar.DAY_OF_WEEK)-1]);
		model.addAttribute("isToday", cal.get(Calendar.DAY_OF_YEAR) == todayCal.get(Calendar.DAY_OF_YEAR));
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		EhrEmpVO empVO = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		if (!"".equals(username) && null == session.getAttribute("_USER")) {
			empVO = authorizeClient.getEmpVO(username);
			session.setAttribute("_USER", empVO);
		} else {
			empVO = (EhrEmpVO) session.getAttribute("_USER");
		}
		model.addAttribute("user", empVO);
		model.addAttribute("calDate", calDate);
		return "otherReason";
	}

	@RequestMapping(value = { "/addOthersReason.html" },method = RequestMethod.POST)
	public String addOtherReason(HttpSession session, HttpServletRequest req, Model model)
			throws ParseException, IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		EhrEmpVO empVO = null;
		List<String> msgs = new ArrayList<String>();
		String hidTxtActName = req.getParameter("hidTxtActName");
		String calDate = "";
		Calendar cal = Calendar.getInstance();
		Calendar todayCal = Calendar.getInstance();
		String txtOtherDesc = req.getParameter("txtOtherDesc");
		try {
			empVO = (EhrEmpVO) session.getAttribute("_USER");
			if (StringUtils.isBlank(req.getParameter("calDate"))) {
				msgs.add("日期不可為空白!");
			} else {
				calDate = req.getParameter("calDate");
				cal.setTime(sdf.parse(calDate));
				model.addAttribute("eWeekDay",IContent._e_weekday[cal.get(Calendar.DAY_OF_WEEK)-1]);
				model.addAttribute("cWeekDay",IContent._c_weekday[cal.get(Calendar.DAY_OF_WEEK)-1]);
				model.addAttribute("isToday", cal.get(Calendar.DAY_OF_YEAR) == todayCal.get(Calendar.DAY_OF_YEAR));
			}
			
			
			if (StringUtils.isBlank(req.getParameter("hidTxtActName")) && StringUtils.isBlank(txtOtherDesc)) {
				msgs.add("需要選擇一項原因或填寫其他欄位說明!");
			}
			if("".equals(hidTxtActName) && StringUtils.isNotBlank(txtOtherDesc)) {
				hidTxtActName = "others";
			}
			if (msgs.isEmpty()) {
				Date punchDate = timeSdf.parse(calDate + " 00:00");
				long punchDateTime = punchDate.getTime();
				boolean hasOtherRecords = punchService.hasOtherRecordsAtSameDate(empVO.getEmpNo(), calDate, hidTxtActName);
				if(hasOtherRecords) {
					msgs.add("當日已經有「打上下班資料」，無法填寫無出勤原因!");
				}
				boolean hasDepulicateRecords = punchService.hasDepuiclateRecords(empVO.getEmpNo(), calDate, punchDateTime);
				if(hasDepulicateRecords) {
					msgs.add("當日已經相同時間的資料!請刪除相同時間資料後再新增!");
				}
			}
			
			if (msgs.isEmpty()) {
				
				Date punchDate = sdf.parse(calDate);
				long punchDateTime = punchDate.getTime();
				if ("".contentEquals(hidTxtActName) || StringUtils.isNotBlank(txtOtherDesc)) {
					hidTxtActName = IContent._act_others;
				}
				PunchRecordVO recordVO = new PunchRecordVO();
				recordVO.setDeptName(empVO.getDepName());
				recordVO.setDeptNo(empVO.getDepID());
				recordVO.setEmpNo(empVO.getEmpNo());
				recordVO.setName(empVO.getEname() + " " + empVO.getCname());
				recordVO.setPunchType(hidTxtActName);
				recordVO.setPunchDateTime(punchDateTime);
				recordVO.setOtherDesc(txtOtherDesc);
				logger.info("Add punch record : {} ", recordVO);
				punchService.addPunchData(recordVO);
				model.addAttribute("user", empVO);
				return "otherReasonSuccess.html";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return "redirect:otherReason.html";
		}

		model.addAttribute("user", empVO);
		model.addAttribute("msgs", msgs);
		model.addAttribute("calDate", calDate);
		model.addAttribute("hidTxtActName", hidTxtActName);
		model.addAttribute("txtOtherDesc", txtOtherDesc);
		
		
		
		return "otherReason";
	}

	@RequestMapping(value = { "/addPunch.html" },method = RequestMethod.POST)
	public String addRecord(HttpSession session, HttpServletRequest req, Model model)
			throws ParseException, IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		EhrEmpVO empVO = null;
		List<String> msgs = new ArrayList<String>();
		String txtPunchTime = "";
		String hidTxtActName = "";
		String calDate = "";
		String txtOtherReason = "";
		
		try {
			empVO = (EhrEmpVO) session.getAttribute("_USER");
			if (StringUtils.isBlank(req.getParameter("calDate"))) {
				msgs.add("日期不可為空白!");
			} else {
				calDate = req.getParameter("calDate");
			}

			if (StringUtils.isBlank(req.getParameter("hidTxtActName"))) {
				msgs.add("需是打卡或是無刷卡!");
			} else {
				hidTxtActName = req.getParameter("hidTxtActName");
			}
			if (StringUtils.equalsIgnoreCase(req.getParameter("hidTxtActName"), IContent._act_punch_in)) {
				if (StringUtils.isBlank(req.getParameter("txtPunchInTime"))) {
					msgs.add("上班時間不可以空白!");
				} else {
					txtPunchTime = req.getParameter("txtPunchInTime");
				}
			}
			if (StringUtils.equalsIgnoreCase(req.getParameter("hidTxtActName"), IContent._act_punch_out)) {
				if (StringUtils.isBlank(req.getParameter("txtPunchOutTime"))) {
					msgs.add("下班時間不可以空白!");
				} else {
					txtPunchTime = req.getParameter("txtPunchOutTime");
				}
			}
			if (msgs.isEmpty()) {
				Date punchDate = sdf.parse(calDate + " " + txtPunchTime);
				long punchDateTime = punchDate.getTime();
				boolean hasOtherRecords = punchService.hasOtherRecordsAtSameDate(empVO.getEmpNo(), calDate, hidTxtActName);
				if(hasOtherRecords) {
					msgs.add("當日已經有「無出勤原因」資料，請刪除後再打卡上下班時間!");
				}
				boolean hasDepulicateRecords = punchService.hasDepuiclateRecords(empVO.getEmpNo(), calDate, punchDateTime);
				if(hasDepulicateRecords) {
					msgs.add("當日已經相同時間的資料!請刪除相同時間資料後再新增!");
				}
			}
			if (msgs.isEmpty()) {
				Date punchDate = sdf.parse(calDate + " " + txtPunchTime);
				long punchDateTime = punchDate.getTime();
				String successMsg = "上班打卡成功";
				PunchRecordVO recordVO = new PunchRecordVO();
				recordVO.setDeptName(empVO.getDepName());
				recordVO.setDeptNo(empVO.getDepID());
				recordVO.setEmpNo(empVO.getEmpNo());
				recordVO.setName(empVO.getEname() + " " + empVO.getCname());
				recordVO.setPunchType(hidTxtActName);
				recordVO.setPunchDateTime(punchDateTime);
				recordVO.setOtherDesc(txtOtherReason);
				if(IContent._act_punch_in.contentEquals(hidTxtActName)) {
					successMsg = "上班打卡成功";
				}
				if(IContent._act_punch_out.contentEquals(hidTxtActName)) {
					successMsg = "下班打卡成功";
				}
				logger.info("Add punch record : {} ", recordVO);
				boolean exeResult = punchService.addPunchData(recordVO);
				if(!exeResult) {
					throw new Exception("新增記錄失敗!");
				}
				model.addAttribute("user", empVO);
				model.addAttribute("successMsg",successMsg);
				model.addAttribute("calDate", calDate);
				return "punchSuccess";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return "redirect:index.html";
		}

		model.addAttribute("user", empVO);
		model.addAttribute("msgs", msgs);
		model.addAttribute("calDate", calDate);
		model.addAttribute("hidTxtActName", hidTxtActName);
		model.addAttribute("txtPunchOutTime", txtPunchTime);
		model.addAttribute("txtPunchInTime", txtPunchTime);
		return "indexA";
	}

	@RequestMapping(value = { "/punchRecords.html" },method = {RequestMethod.POST,RequestMethod.GET})
	public String listRecord(HttpSession session, HttpServletRequest req, Model model) throws UnsupportedEncodingException {
		logger.info("_LOGIN_TYPE:{}",session.getAttribute(IContent._login_type));
		Calendar todayCal = Calendar.getInstance();
		Calendar yesdayCal = Calendar.getInstance();
		yesdayCal.add(Calendar.DAY_OF_YEAR, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sDate = req.getParameter("calSDate");
		String eDate = req.getParameter("calEDate");

		String sDateTime = "";
		String eDateTime = "";
	
		List<String> msgs = new ArrayList<String>();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		EhrEmpVO empVO = (EhrEmpVO) session.getAttribute("_USER");
		if (null == empVO) {
			if (principal instanceof UserDetails) {
				username = ((UserDetails) principal).getUsername();
			} else {
				username = principal.toString();
			}
			if (!"".equals(username) && null == session.getAttribute("_USER")) {
				empVO = authorizeClient.getEmpVO(username);
				session.setAttribute("_USER", empVO);
			}
		}
		String empNo = empVO== null ? "" : empVO.getEmpNo();
		if("".equals(empNo)) {
			logger.error("empNo is empty!");
		}
		
		if (StringUtils.isBlank(sDate) && StringUtils.isBlank(eDate)) {
			sDateTime = sdf.format(yesdayCal.getTime());
			eDateTime = sdf.format(todayCal.getTime());
			sDate = sdf.format(yesdayCal.getTime());
			eDate = sdf.format(todayCal.getTime());
		} else {
			sDateTime = sDate;
			eDateTime = eDate;
		}
		//sDateTime = StringUtils.replace(sDateTime, "/", "-");
		//eDateTime = StringUtils.replace(eDateTime, "/", "-");
		logger.info("[listRecord] empNo={}, sDate={}, eDate={}", MyUtils.filterCRLF(empNo), MyUtils.filterCRLF(sDateTime), MyUtils.filterCRLF(eDateTime) );
		List<PunchRecordVO> vos = punchService.listRecord(empNo, sDateTime, eDateTime);

		model.addAttribute("calSDate", sDate);
		model.addAttribute("calEDate", eDate);
		model.addAttribute("user", empVO);
		model.addAttribute("msgs", msgs);
		model.addAttribute("records", vos);
		model.addAttribute("loginType", session.getAttribute(IContent._login_type));
		return "punchRecords";
	}
	
	
	@RequestMapping(value = { "/sso.html" },method = RequestMethod.GET)
	public String ssoLogin(HttpSession session, HttpServletRequest req, Model model) {
		return "redirect:index.html";
	}

}
