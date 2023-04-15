package com.scb.gb.sci.util;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthenticationUtil {

	@Value("${spring.sci.wb-token-url}")
	private String url;

	@Value("${spring.token.clientid}")
	private String clientId;

	@Value("${spring.token.clientSecret}")
	private String clientSecret;

	@Value("${spring.token.credType}")
	private String credType;

	@Value("${spring.token.consumerid}")
	private String consumerid;

	public JSONObject getOauthDetails() {

		JSONObject json = new JSONObject();

		try {
			doTrustToCertificates();
			HttpHeaders headers = new HttpHeaders();

			headers.setContentType(MediaType.APPLICATION_JSON);
			RestTemplate restTemplate = new RestTemplate();

			json.put("clientid", clientId);
			json.put("clientSecret", clientSecret);
			json.put("credType", credType);
			json.put("consumerid", consumerid);

			HttpEntity<String> request = new HttpEntity<String>(json.toString(), headers);

			json = restTemplate.postForObject(url, request, JSONObject.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}
	
	
	public static void doTrustToCertificates() {
		 TrustManager[] trustAllCerts = new TrustManager[] {
			    new X509TrustManager() {
			        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			            return null;
			        }
			        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			            //No need to implement. 
			        }
			        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			            //No need to implement. 
			        }
			    }
			};
			// Install the all-trusting trust manager
			try {
			    SSLContext sc = SSLContext.getInstance("SSL");
			    sc.init(null, trustAllCerts, new java.security.SecureRandom());
			    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			} catch (Exception e) {
			    System.out.println(e);
			}
	}
}
