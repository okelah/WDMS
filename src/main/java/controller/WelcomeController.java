package controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WelcomeController {
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView welcome(HttpServletRequest httpServletRequest){
		
		return new ModelAndView("index");
	}
	
	@RequestMapping(value="/", method=RequestMethod.POST)
	public ModelAndView welcomePost(HttpServletRequest httpServletRequest){
		
		return new ModelAndView("index");
	}

}
