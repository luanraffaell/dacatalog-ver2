package com.devsuperior.dscatalog.components;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;
//Classe com finalizade de acrescentar dados ao token
@Component
public class JwtTokenEnhancer implements TokenEnhancer {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		User user = userRepository.findByEmail(authentication.getName());//getName vai retornar o nome usado na autenticação que no nosso caso é o email.
		
		Map<String, Object> map = new HashMap<>();
		map.put("userFirstName", user.getFirstName());
		map.put("userId", user.getId());
		
		//devemos inserir os dados acima no accessToken porem o mesmo não possui tal método para isso
		//então usar outro objeto que é o mesmo do parametro com Default afrente fazendo um downcast:
		
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
		
		token.setAdditionalInformation(map);
		return accessToken;
	}
	
}
