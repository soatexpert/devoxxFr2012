/*
 * Copyright (c) 2012 Aurélien VIALE
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package fr.soat.devoxx.game.webmvc.services;

import java.util.List;

import fr.soat.devoxx.game.business.admin.AdminUserService;
import fr.soat.devoxx.game.business.exception.InvalidUserException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationToken;

import fr.soat.devoxx.game.pojo.UserRequestDto;
import fr.soat.devoxx.game.pojo.UserResponseDto;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;

/**
 * @author aurelien
 * 
 */
public class OpenIdUserDetailsService implements UserDetailsService, AuthenticationUserDetailsService<OpenIDAuthenticationToken> {


    @Inject
    AdminUserService adminUserService;
    
	private static Logger LOGGER = LoggerFactory.getLogger(OpenIdUserDetailsService.class);
	private static final List<GrantedAuthority> DEFAULT_AUTHORITIES = AuthorityUtils.createAuthorityList("ROLE_USER");

	@Override
	public UserDetails loadUserByUsername(String urlId) throws UsernameNotFoundException {
//		try {
	        return getUserWS(urlId);
//        } catch (HttpRestException e) {
//            LOGGER.debug("UserId : " + urlId + " doesn't exist", e);
//	        throw new UsernameNotFoundException("UserId : " + urlId + " doesn't exist in database", e);
//        }
	}

	/**
	 * Implementation of {@code AuthenticationUserDetailsService} which allows
	 * full access to the submitted {@code Authentication} object. Used by the
	 * OpenIDAuthenticationProvider.
	 */
	@Override
	public UserDetails loadUserDetails(OpenIDAuthenticationToken token) throws UsernameNotFoundException {
		
		String urlId = token.getIdentityUrl();
        LOGGER.info("urlId=" + urlId);
		
		try {
	        return getUserWS(urlId);
        } catch (WebApplicationException e) {
            LOGGER.debug("UserId : " + urlId + " doesn't exist in database", e);
        }

		String email = null;
		String firstName = null;
		String lastName = null;
		String fullName = null;

		List<OpenIDAttribute> attributes = token.getAttributes();
        LOGGER.info("attributes=" + attributes);

		for (OpenIDAttribute attribute : attributes) {
			if (attribute.getName().equals("email")) {
				email = attribute.getValues().get(0);
			}
			if (attribute.getName().equals("firstname")) {
				firstName = attribute.getValues().get(0);
			}
			if (attribute.getName().equals("fullname")) {
				fullName = attribute.getValues().get(0);
			}
		}
		
		if (StringUtils.isEmpty(fullName) && (!StringUtils.isEmpty(firstName) || !StringUtils.isEmpty(lastName))) {
			fullName = firstName + " " + lastName;
		}
		
		OpenIdUserDetails user = new OpenIdUserDetails(urlId, DEFAULT_AUTHORITIES);
        user.setEmail(email);
        user.setName(fullName);
        
//        try {
	        registerUserWS(user);
//        } catch (HttpRestException e) {
//            LOGGER.error("UserId : " + urlId + " persist error", e);
//        }

		return user;
	}
	
	private OpenIdUserDetails getUserWS(String urlId) {

        UserResponseDto userResp = adminUserService.getUser(Long.valueOf(urlId));
        
//		RequesterDelegate ws = new RequesterDelegate("/admin/user/" + urlId);
//        UserResponseDto userResp = ws.get(UserResponseDto.class);
        
        //TODO User Get Role !
        OpenIdUserDetails user = new OpenIdUserDetails(urlId, DEFAULT_AUTHORITIES);
        user.setEmail(userResp.getMail());
        user.setName(userResp.getName());
        user.setNewUser(false);
        
        return user;  
	}
	
	private void registerUserWS(OpenIdUserDetails userOpenid) {
		//TODO getName size !
		UserRequestDto userReq = new UserRequestDto(userOpenid.getName(), userOpenid.getEmail());

        try {
            adminUserService.createUser(userReq);
        } catch (InvalidUserException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

//        RequesterDelegate ws = new RequesterDelegate("/admin/user");
//        ws.post(userReq);
	}

}
