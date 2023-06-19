package com.themoment.officialgsm.domain.auth.service;

import com.themoment.officialgsm.domain.auth.entity.token.RefreshToken;
import com.themoment.officialgsm.domain.auth.entity.user.Role;
import com.themoment.officialgsm.domain.auth.entity.user.User;
import com.themoment.officialgsm.domain.auth.repository.RefreshTokenRepository;
import com.themoment.officialgsm.domain.auth.repository.UserRepository;
import com.themoment.officialgsm.domain.auth.dto.OAuthAttributes;
import com.themoment.officialgsm.domain.auth.dto.UserProfile;
import com.themoment.officialgsm.global.security.jwt.JwtTokenProvider;
import com.themoment.officialgsm.global.util.ConstantsUtil;
import com.themoment.officialgsm.global.util.CookieUtil;
import com.themoment.officialgsm.global.util.EmailUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static com.themoment.officialgsm.domain.auth.entity.user.Role.ADMIN;
import static com.themoment.officialgsm.domain.auth.entity.user.Role.UNAPPROVED;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final HttpServletResponse httpServletResponse;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieUtil cookieUtil;
    @Value("${domain}")
    private String schoolDomain;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        UserProfile userProfile = OAuthAttributes.extract(registrationId, attributes);

        String email = EmailUtil.getEmailDomain(userProfile.getEmail());
        if (!email.equals(schoolDomain)){
            throw new OAuth2AuthenticationException("학교 이메일이 아닙니다.");
        }

        User user = saveOrUpdate(userProfile);

        String accessToken = jwtTokenProvider.generatedAccessToken(user.getOauthId());
        String refreshToken = jwtTokenProvider.generatedRefreshToken(user.getOauthId());
        cookieUtil.addTokenCookie(httpServletResponse, ConstantsUtil.accessToken, accessToken, jwtTokenProvider.getACCESS_TOKEN_EXPIRE_TIME(), true);
        cookieUtil.addTokenCookie(httpServletResponse, ConstantsUtil.refreshToken, refreshToken, jwtTokenProvider.getREFRESH_TOKEN_EXPIRE_TIME(), true);
        RefreshToken entityToRedis = new RefreshToken(user.getOauthId(), refreshToken, jwtTokenProvider.getREFRESH_TOKEN_EXPIRE_TIME());
        refreshTokenRepository.save(entityToRedis);
        
        redirectUser(user);

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())), attributes, userNameAttributeName);
    }
    
    private void redirectUser(User user){
        Role role = user.getRole();
        String userName = user.getUserName();

        if (role == UNAPPROVED && userName != null) {
            try {
                httpServletResponse.sendRedirect("https://admin-official.hellogsm.kr/auth/signup/pending");
            } catch (IOException e) {
                log.error("https://admin-official.hellogsm.kr/auth/signup/pending 페이지로 redirect 도중 에러가 발생했습니다.");
            }
        } else if (role == ADMIN) {
            try {
                httpServletResponse.sendRedirect("https://admin-official.hellogsm.kr");
            } catch (IOException e) {
                log.error("https://admin-official.hellogsm.kr 페이지로 redirect 도중 에러가 발생했습니다.");
            }
        } else {
            try {
                httpServletResponse.sendRedirect("https://admin-official.hellogsm.kr/auth/signup");
            } catch (IOException e) {
                log.error("https://admin-official.hellogsm.kr/auth//signup 페이지로 redirect 도중 에러가 발생했습니다.");
            }
        }
    }

    private User saveOrUpdate(UserProfile userProfile) {
        User user = userRepository.findByOauthId(userProfile.getOauthId())
                .map(m -> m.updateEmail(userProfile.getEmail()))
                .orElse(userProfile.toEntity());
        return userRepository.save(user);
    }
}
