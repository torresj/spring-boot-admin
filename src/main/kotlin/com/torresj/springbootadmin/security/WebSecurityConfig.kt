package com.torresj.springbootadmin.security

import de.codecentric.boot.admin.server.config.AdminServerProperties
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.util.*

@Configuration
class WebSecurityConfig(val adminServer: AdminServerProperties): WebSecurityConfigurerAdapter() {
    protected override fun configure(http: HttpSecurity?) {
        val successHandler = SavedRequestAwareAuthenticationSuccessHandler()
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminServer.contextPath + "/");
        http
            ?.authorizeRequests()
                ?.antMatchers(adminServer.contextPath + "/assets/**")?.permitAll()
                ?.antMatchers(adminServer.contextPath + "/login")?.permitAll()
                ?.anyRequest()?.authenticated()
                ?.and()
            ?.formLogin()
                ?.loginPage(adminServer.contextPath + "/login")
                ?.successHandler(successHandler)
                ?.and()
            ?.logout()
                ?.logoutUrl(adminServer.contextPath + "/logout")
                ?.and()
            ?.httpBasic()
                ?.and()
            ?.csrf()
                ?.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                ?.ignoringRequestMatchers(
                        AntPathRequestMatcher(
                                adminServer.contextPath + "/instances", HttpMethod.POST.toString()),
                        AntPathRequestMatcher(
                                adminServer.contextPath + "/instances/*", HttpMethod.DELETE.toString()),
                        AntPathRequestMatcher(adminServer.contextPath + "/actuator/**"))
                ?.and()
            ?.rememberMe()
                ?.key(UUID.randomUUID().toString())
                ?.tokenValiditySeconds(1209600);
    }
}