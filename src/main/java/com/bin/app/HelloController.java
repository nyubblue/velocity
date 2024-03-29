package com.bin.app;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Controller
public class HelloController {
	
	private RestTemplate restTemplate;

	@Autowired
	public HelloController() {
	    this.restTemplate = new RestTemplate();
	}

	private final String SITE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
	private final String SECRET = "";

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String defaultPage(HttpServletRequest request, ModelMap model) {
	//TODO
		return "home";
	}

	@RequestMapping(value = "/captcha-disp", method = RequestMethod.GET)
	public String displayCaptcha(HttpServletRequest request, ModelMap model) {
	//TODO
		return "captcha_view";
	}
	
	private String getRemoteIp(HttpServletRequest request) {
		String remoteAddr = "";
		if (request != null) {
			remoteAddr = request.getHeader("X-FORWARDED-FOR");
			if (remoteAddr == null || "".equals(remoteAddr)) {
				remoteAddr = request.getRemoteAddr();
			}
		}

		return remoteAddr;
	}

	@RequestMapping(value = "/valid", method = RequestMethod.POST)
	public String validate(HttpServletRequest request, ModelMap model,
			@RequestParam(name = "g-recaptcha-response") String response) {
		try {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("secret", SECRET);
			map.add("remoteip", getRemoteIp(request));
			map.add("response", response);
			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);
			ResponseEntity<?> responseent = restTemplate.postForEntity(SITE_VERIFY_URL, requestEntity, String.class);

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonElement element = gson.fromJson (responseent.getBody().toString(), JsonElement.class);
			JsonObject jsonObj = element.getAsJsonObject();
			@SuppressWarnings("unchecked")
			Map<String,Object> resultMap = new Gson().fromJson(jsonObj, Map.class);
			if (resultMap.get("success") != null && true == (Boolean)resultMap.get("success")) {
				model.addAttribute("captchaHtml", "SUCESS: " + resultMap.get("hostname"));
			} else {
				model.addAttribute("captchaHtml",
						"FAILED: " + resultMap.get("hostname") + " - " + resultMap.get("error-codes"));
			}
		} catch (Exception e) {
			model.addAttribute("captchaHtml", "ERROR SYSTEM");
		}
		return "home";
	}
}
