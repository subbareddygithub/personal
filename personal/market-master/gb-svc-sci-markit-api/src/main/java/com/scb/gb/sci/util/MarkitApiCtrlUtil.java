package com.scb.gb.sci.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.scb.gb.sci.exception.BusinessException;
import com.scb.gb.sci.publisher.KafkaPublisher;

@Component
public class MarkitApiCtrlUtil {

	@Autowired
	private KafkaPublisher kafkaPublisher;

	public String fetchMarkitApiData(String str, JSONObject json, int i) {

		String jsonObj = null;
		HttpsURLConnection con = null;
		boolean flag = true;

		try {
			URL url = new URL(str);

			con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("Accept", "application/jspon");

			if (null != json.get("access_token")) {
				con.setRequestProperty("Authorization", "Bearer" + json.get("access_token"));
				if (con.getResponseCode() != 200) {
					while (i < 3) {
						try {
							i = i + 1;
							Thread.sleep(30 * 1000);
							jsonObj = fetchMarkitApiData(str, json, i);
							if (null != jsonObj) {
								flag = false;
								return jsonObj;
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
							throw new BusinessException("Http error code" + con.getResponseCode());
						}
					}
				}
			}

			if (flag == true) {
				try (BufferedReader br = new BufferedReader(
						new InputStreamReader(con.getInputStream(), Charset.defaultCharset()))) {
					String output;
					while ((output = br.readLine()) != null) {
						jsonObj = output;
					}
				} catch (IOException e) {

				}

				if (null != jsonObj) {

					kafkaPublisher.vdmPublish(jsonObj);
				}
			}
		} catch (Exception e) {

		} finally {
			if (null != con)
				con.disconnect();
		}

		return jsonObj;
	}
}
