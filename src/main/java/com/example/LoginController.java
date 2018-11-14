//package com.example;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class LoginController<ClientRegistrationRepository> {
//	
//	private static String authorizationRequestBaseUri
//    	= "oauth2/authorization";
//	Map<String, String> oauth2AuthenticationUrls
//    	= new HashMap<>();
//
//	@Autowired
//	private ClientRegistrationRepository clientRegistrationRepository;
//  
//	@GetMapping("/oauth_login")
//	public String getLoginPage(Model model) {
//      // ...
//
//		return "http://google.com";
//	}
//}
