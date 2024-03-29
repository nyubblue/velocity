package com.bin.app;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.captcha.botdetect.web.servlet.Captcha;

@Controller
public class HelloController {

    @RequestMapping(value ="/home", method = RequestMethod.GET)
    public String defaultPage(HttpServletRequest request, ModelMap model) {
    	Captcha captcha = Captcha.load(request, "exampleCaptcha");
    	captcha.setUserInputID("captchaCode");
    	captcha.setHelpLinkEnabled(false);
    	captcha.setCodeLength(6);
    	captcha.setSoundEnabled(true);
    	String captchaHtml = captcha.getHtml();
    	String str = "<a href=\"//captcha.org/captcha.html?java\" title=\"What is BotDetect Java CAPTCHA Library?\" style=\"display: block !important; height: 10px !important; margin: 0 !important; padding: 0 !important; font-size: 9px !important; line-height: 10px !important; visibility: visible !important; font-family: Verdana, DejaVu Sans, Bitstream Vera Sans, Verdana Ref, sans-serif !important; vertical-align: middle !important; text-align: center !important; text-decoration: none !important; background-color: #f8f8f8 !important; color: #606060 !important;\">What is BotDetect Java CAPTCHA Library?</a>sss";
//    	captchaHtml = str.replaceAll("<[^\\P{Graph}>]+(?: [^>]*)?>", "");
    	Pattern pattern = Pattern.compile("(<a(.*)>).*(</a>)");
    	  // Replace all HTML tags with an empty string
    	captchaHtml=  pattern.matcher(captchaHtml).replaceFirst("");
    	model.addAttribute("captchaHtml", captchaHtml);
    	model.addAttribute("instanceId", captcha.getCurrentInstanceId());
        return "home";
    }
    
    @RequestMapping(value ="/valid", method = RequestMethod.POST)
    public String validate(HttpServletRequest request, ModelMap model,
    		@RequestParam(name = "captchaCode") String captchaCode,
    		@RequestParam(name = "instanceId") String instanceId) {
    	String code = request.getParameter("code");
    	
//    	Captcha captcha = Captcha.load(request, "exampleCaptcha");
//    	 boolean isHuman = captcha.validate(captchaCode, instanceId);
    	 boolean isHuman = Captcha.validate(request, "exampleCaptcha", captchaCode, instanceId);
    	String captchaHtml = isHuman ? "YES": "NO";
    	model.addAttribute("captchaHtml", captchaHtml);
        return "home";
    }
}
