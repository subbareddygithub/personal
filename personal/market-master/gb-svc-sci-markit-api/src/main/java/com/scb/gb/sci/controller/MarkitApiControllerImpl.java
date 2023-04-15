package com.scb.gb.sci.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import com.scb.gb.sci.MarkitApiControllerService;
import com.scb.gb.sci.constants.MarkitApiConstants;
import com.scb.gb.sci.util.AuthenticationUtil;
import com.scb.gb.sci.util.TimeStampUtility;

public class MarkitApiControllerImpl implements MarkitApiControllerService{
	
	private static final Logger log = LogManager.getLogger(MarkitApiControllerImpl.class);
	
	@Autowired
	private MarkitApiProtocolImpl mktProtocolImpl;
	
	@Autowired
	private TimeStampUtility timeStampUtil;
	
	@Autowired
	private AuthenticationUtil authenticationUtil;
	
	@Value("${spring.sci.workbench-url}")
	private String url;
	
	@Value("${spring.sci.flag-value}")
	private String flag;
	

	@Value("${spring.sci.cron-timeZone}")
	private String timeZone;
	
	@Scheduled(cron = "${spring.sci.cron-daily}")
	//@Scheduled(cron = "0 34 09 * * ?")
	public void getMarkitApiData() {
		
		if(flag.equals("true")) {
			
			String previousDate = timeStampUtil.previousDayDate();
			String timeStamp = previousDate + "T00:00:00.000Z";
			
			getMarkitDataUtil(timeStamp);
		}else {
			log.error("Flag Value ---->"+ flag);
		}
		
	}
	
	@Scheduled(cron = "${spring.sci.cron-weekly}")
	public void getMarkitDataWeekEnd() {
		
		if(flag.equals("true")) {
			String previousWeek = timeStampUtil.previousWeekDate();
			String timeStamp = previousWeek + "T00:00:00.000Z";
			
			getMarkitDataUtil(timeStamp);
			
		}else {
			log.error("Flag value---->"+ flag);
		}
		
	}
	
	private void getMarkitDataUtil(String timeStamp) {
		
		JSONObject json = authenticationUtil.getOauthDetails();
		String url1 = String.format(url,MarkitApiConstants.P1, timeStamp);
		String url2 = String.format(url,MarkitApiConstants.P2, timeStamp);
		
		mktProtocolImpl.getMarkitApiProtocolData(MarkitApiConstants.P1, url1, json);
		mktProtocolImpl.getMarkitApiProtocolData(MarkitApiConstants.P2, url2, json);
	}

}
