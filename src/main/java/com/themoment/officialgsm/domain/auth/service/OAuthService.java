package com.themoment.officialgsm.domain.auth.service;

import com.themoment.officialgsm.domain.auth.entity.token.RefreshToken;
import com.themoment.officialgsm.domain.auth.entity.user.User;
import com.themoment.officialgsm.domain.auth.repository.RefreshTokenRepository;
import com.themoment.officialgsm.domain.auth.repository.UserRepository;
import com.themoment.officialgsm.domain.auth.dto.OAuthAttributes;
import com.themoment.officialgsm.domain.auth.dto.UserProfile;
import com.themoment.officialgsm.global.security.jwt.JwtTokenProvider;
import com.themoment.officialgsm.global.util.ClientIpUtil;
import com.themoment.officialgsm.global.util.ConstantsUtil;
import com.themoment.officialgsm.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final ClientIpUtil ipUtil;
    private final HttpServletRequest request;
    private final JwtTokenProvider jwtTokenProvider;
    private final HttpServletResponse httpServletResponse;
    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        ipUtil.getIp(request);

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        UserProfile userProfile = OAuthAttributes.extract(registrationId, attributes);

        User user = saveOrUpdate(userProfile);

        String accessToken = jwtTokenProvider.generatedAccessToken(user.getOauthId());
        String refreshToken = jwtTokenProvider.generatedRefreshToken(user.getOauthId());
        CookieUtil.addTokenCookie(httpServletResponse, ConstantsUtil.accessToken, accessToken, jwtTokenProvider.getACCESS_TOKEN_EXPIRE_TIME(), true);
        CookieUtil.addTokenCookie(httpServletResponse, ConstantsUtil.refreshToken, refreshToken, jwtTokenProvider.getREFRESH_TOKEN_EXPIRE_TIME(), true);
        RefreshToken entityToRedis = new RefreshToken(user.getOauthId(),refreshToken, jwtTokenProvider.getREFRESH_TOKEN_EXPIRE_TIME());
        refreshTokenRepository.save(entityToRedis);

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())), attributes, userNameAttributeName);
    }

    private User saveOrUpdate(UserProfile userProfile) {
        User user = userRepository.findByOauthId(userProfile.getOauthId())
                .map(m -> m.updateEmail(userProfile.getEmail()))
                .orElse(userProfile.toEntity());
        return userRepository.save(user);
    }
}
