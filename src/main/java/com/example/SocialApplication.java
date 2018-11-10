package com.example;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.trace.http.HttpTrace.Principal;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CompositeFilter;

@SpringBootApplication
@EnableOAuth2Sso
@RestController
public class SocialApplication extends WebSecurityConfigurerAdapter{
	
	private static final Logger log = LoggerFactory.getLogger(SocialApplication.class);
	
	@Autowired
	OAuth2ClientContext oauth2ClientContext;
	 
	public SocialApplication() {}
	
	public static void main (String[] args) throws Exception {
		SpringApplication.run(SocialApplication.class, args);
	}
	
	  @RequestMapping("/user")
	  public Principal user(Principal principal) {
	    return principal;
	  }
	  
	  // @formatter:off
	  @Override
	  protected void configure(HttpSecurity http) throws Exception {
	  http.antMatcher("/**")
	  .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
	  .authorizeRequests()
	  .antMatchers("/", "/connect**", "/webjars/**")
	  .permitAll()
	  .anyRequest()
	  .authenticated()
	  .and()
	  .logout()
	      .logoutSuccessUrl("/").permitAll().and().csrf()
	  .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	  }
	  // @formatter:on
	  
	  private Filter ssoFilter() {
		  
		  CompositeFilter filter = new CompositeFilter();
		  List filters = new ArrayList<>();
		  
		  OAuth2ClientAuthenticationProcessingFilter linkedInFilter = new OAuth2ClientAuthenticationProcessingFilter(
				  "/connect/linkedIn");
				  OAuth2RestTemplate linkedInTemplate = new OAuth2RestTemplate(linkedIn(), oauth2ClientContext);
				  linkedInFilter.setRestTemplate(linkedInTemplate);
				  UserInfoTokenServices tokenServices = new UserInfoTokenServices(linkedInResource().getUserInfoUri(), linkedIn().getClientId());
				  tokenServices.setRestTemplate(linkedInTemplate);
				  linkedInFilter.setTokenServices(tokenServices);
				  
				  filters.add(linkedInFilter);
				  
				  filter.setFilters(filters);
				  return filter;
	  }
	  
	  @Bean
	  public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
	  FilterRegistrationBean registration = new FilterRegistrationBean();
	  registration.setFilter(filter);
	  registration.setOrder(-100);
	  return registration;
	  }
	  
	  @Primary
	  @Bean
	  @ConfigurationProperties("linkedIn.client")
	  public AuthorizationCodeResourceDetails linkedIn() {
	  return new AuthorizationCodeResourceDetails();
	  }
	  
	  @Bean
	  @ConfigurationProperties("linkedIn.resource")
	  public ResourceServerProperties linkedInResource() {
	  return new ResourceServerProperties();
	  }
	
//	@Bean
//	public OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext,
//	        OAuth2ProtectedResourceDetails details) {
//	    return new OAuth2RestTemplate(details, oauth2ClientContext);
//	}
//	
//	  @Override
//	  protected void configure(HttpSecurity http) throws Exception {
//	    http
//	      .antMatcher("/**")
//	      .authorizeRequests()
//	        .antMatchers("/", "/login**", "/webjars/**", "/error**")
//	        .and().logout().logoutSuccessUrl("/").permitAll()
//		    .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//	      .anyRequest()
//	        .authenticated();
//	  }
	
	  
	
//	@Bean
//	public RestTemplate restTemplate(RestTemplateBuilder builder) {
//		return builder.build();
//	}
//	
//	@Bean
//	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
//		return args -> {
//			Person person = restTemplate.getForObject(
//					"https://api.linkedin.com/v1/people/~", Person.class);
//			log.info(person.toString());
//		};
//	}

}
