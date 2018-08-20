package stackjava.com.springmvchello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/page2")
public class Page2 {
	
	@RequestMapping(method = RequestMethod.GET)
	public String page2() {
		return "page2";
	}
	
	
	@RequestMapping(value ="/page3" , method = RequestMethod.GET)
	public String page3() {
		return "page3";
	}
	
	@RequestMapping(value ="/page4" , method = RequestMethod.GET)
	public String page4() {
		return "page4";
	}
	
	

}
