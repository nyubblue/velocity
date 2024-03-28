package com.bin.app;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.captcha.botdetect.web.servlet.Captcha;

@Controller
public class HelloController {

    @RequestMapping(value ="/home", method = RequestMethod.GET)
    public String defaultPage(HttpServletRequest request, ModelMap model) {
    	Captcha captcha = Captcha.load(request, "exampleCaptcha");
    	captcha.setUserInputID("captchaCode");
    	String captchaHtml = captcha.getHtml();
    	model.addAttribute("captchaHtml", captchaHtml);
        return "home";
    }
}
