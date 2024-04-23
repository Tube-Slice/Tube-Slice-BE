package TubeSlice.tubeSlice.domain.oauth;



import TubeSlice.tubeSlice.domain.oauth.dto.KakaoUserDto;
import TubeSlice.tubeSlice.domain.oauth.dto.NaverUserDto;
import TubeSlice.tubeSlice.domain.user.Status;
import TubeSlice.tubeSlice.domain.user.User;
import TubeSlice.tubeSlice.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String accessToken = userRequest.getAccessToken().getTokenValue();

        log.info("OAuth2User loadUser access_token: {}", accessToken);

        String provider = userRequest.getClientRegistration().getRegistrationId();  //naver/kakao

        OAuth2UserInfo oAuth2UserInfo = null;

        try {
            log.info("getAttributes: {}",oAuth2User.getAttributes());

        }catch (Exception e){
            e.printStackTrace();
        }

        String socialType = null;

        if (provider.equals("naver")){
            Map<String, Object> responseMap = oAuth2User.getAttributes();

            socialType = "naver";
            oAuth2UserInfo = new NaverUserDto(responseMap);
        }

        if (provider.equals("kakao")){
            Map<String, Object> responseMap = oAuth2User.getAttributes();

            socialType = "kakao";
            oAuth2UserInfo = new KakaoUserDto(responseMap);
        }

        User user = null;
        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String loginId = providerId;
        String name = oAuth2UserInfo.getName();


        User findUser = userRepository.findByLoginId(loginId);

        if(findUser == null){
            user = User.builder()
                    .loginId(loginId)
                    .name(name)
                    .email(email)
                    .socialType(socialType)
                    .loginStatus(Status.ACTIVATE)
                    .role("ROLE_USER")
                    .build();

            userRepository.save(user);
        } else {
            user = findUser;
        }

        return new CustomOauth2User(user, oAuth2User.getAttributes());
    }

}