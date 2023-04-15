package com.scb.gb.sci.util;

import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Service
public class MarkitApiJSONUtil {

	JSONParser parser = new JSONParser();

	public Map<String, String> getMarkitJson(String jsonObj) {

		Map<String, String> map = null;

		JSONObject obj;
		try {
			obj = (JSONObject) parser.parse(jsonObj);
			map = (Map<String, String>) obj.get("nextRequest");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return map;
	}

}
