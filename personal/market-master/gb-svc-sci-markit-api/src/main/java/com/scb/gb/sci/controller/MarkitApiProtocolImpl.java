package com.scb.gb.sci.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.scb.gb.sci.util.MarkitApiCtrlUtil;
import com.scb.gb.sci.util.MarkitApiJSONUtil;

@Component
public class MarkitApiProtocolImpl {
	
	private static final Logger log = LogManager.getLogger(MarkitApiProtocolImpl.class);
	
	@Autowired
	private MarkitApiCtrlUtil markitApiCtrlutil;
	
	@Autowired
	private MarkitApiJSONUtil markitApiJsonUtil;
	
	@Value("${spring.sci.workbench-nextreq}")
	private String nextReqUrl;
	
	public void getMarkitApiProtocolData(String ptr, String url, JSONObject json) {
		
		int i= 0;
		String jsonObj = markitApiCtrlutil.fetchMarkitApiData(url, json, i);
		
		if(null != jsonObj) {
			
			if(!jsonObj.equals("{}")) {
				
				Map<String, String> map = markitApiJsonUtil.getMarkitJson(jsonObj);
				String nextRequest = null;
				String timeStamp = null;
				
				if(null != map.get("fromTimeStamp")) {
					
					timeStamp = map.get("fromTimeStamp");
				}
				
				if(null != map.get("next")) {
					nextRequest = map.get("next");
				}
				
				if(nextRequest != null) {
					String url1 = String.format(nextReqUrl, ptr, timeStamp, nextRequest);
					getMarkitApiNxtElementData(nextRequest, url1, ptr, json);
				}
			}
		}
	}
	
	private void getMarkitApiNxtElementData(String nextRequest, String url, String ptr, JSONObject json) {
		
		if(null != nextRequest) {
			int i =0;
			String jsonObj = markitApiCtrlutil.fetchMarkitApiData(url, json, i);
			
			if(null != jsonObj) {
				
				if(!jsonObj.equals("{}")) {
				
					Map<String, String> map = markitApiJsonUtil.getMarkitJson(jsonObj);
					String tStamp = null;
					
					if(null != map.get("fromTimeStamp")) {
						tStamp = map.get("fromTimeStamp");
					}
					
					if(null != map.get("next")) {
						String nextReq = map.get("next");
						
						String url1 = String.format(nextReqUrl, ptr, tStamp, nextReq);
						getMarkitApiNxtElementData(nextReq, url1, ptr, json);
					}
				}
			}
			
		}else {
			return;
		}
	}

}
