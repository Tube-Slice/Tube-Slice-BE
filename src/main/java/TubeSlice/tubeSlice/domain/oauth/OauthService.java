package TubeSlice.tubeSlice.domain.oauth;

import TubeSlice.tubeSlice.domain.oauth.dto.response.KakaoLoginDto;
import TubeSlice.tubeSlice.domain.oauth.dto.response.LoginResponseDto;
import TubeSlice.tubeSlice.domain.oauth.dto.response.NaverLoginDto;
import TubeSlice.tubeSlice.domain.user.Status;
import TubeSlice.tubeSlice.domain.user.User;
import TubeSlice.tubeSlice.domain.user.UserRepository;
import TubeSlice.tubeSlice.global.Jwt.JwtProvider;
import TubeSlice.tubeSlice.global.response.code.resultCode.ErrorStatus;
import TubeSlice.tubeSlice.global.response.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OauthService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final JwtProvider jwtProvider;
    private boolean isUser;
    private String username;

    @Transactional
    public LoginResponseDto getJwtTokenAndUserId(String access_token, String socialType){
        String loginId = null;

        if (socialType.equals("naver")) {
            loginId = loginByNaver(access_token);
        }
        if (socialType.equals("kakao")) {
            loginId = loginByKakao(access_token);
        }
        if (loginId==null)   {
            //naver또는 kakao에서 유저에 관한 정보를 불러오지 못한 경우 에러 발생.
            throw new UserHandler(ErrorStatus.USER_NOT_FOUND);
        }

        User findUser = userRepository.findByLoginId(loginId);
        Long userId = findUser.getId(); //db에 저장된 유저의 id 값.
        String userLoginId = findUser.getLoginId();

        String token = jwtProvider.create(userId, userLoginId);

        return new LoginResponseDto(userId, username, token, isUser);
    }

    @Transactional
    public String loginByNaver (String access_token) {
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + access_token);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<NaverLoginDto> response =
                restTemplate.exchange("https://openapi.naver.com/v1/nid/me", HttpMethod.GET, request, NaverLoginDto.class);

        User user = null;

        String email = response.getBody().getResponse().getEmail();
        String loginId = response.getBody().getResponse().getId();
        String name = response.getBody().getResponse().getName();
        username = name;

        User findUser = userRepository.findByLoginId(loginId);

        if(findUser == null){
            user = User.builder()
                    .loginId(loginId)
                    .name(name)
                    .nickname(name)
                    .email(email)
                    .socialType("naver")
                    .loginStatus(Status.ACTIVATE)
                    .role("ROLE_USER")
                    .build();

            userRepository.save(user);
            isUser = false;

        } else {
            isUser = true;
        }

        log.info("naver loginId: {}", loginId);

        return loginId;
    }

    @Transactional
    public String loginByKakao (String access_token) {
        HttpHeaders headers = new HttpHeaders();


        headers.set("Authorization", "Bearer " + access_token);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoLoginDto> response =
                restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.GET, request, KakaoLoginDto.class);

        User user = null;

        String email = null;
        String loginId = response.getBody().getId();
        String name = response.getBody().getProperties().getNickname();
        username = name;

        User findUser = userRepository.findByLoginId(loginId);

        if(findUser == null){
            user = User.builder()
                    .loginId(loginId)
                    .name(name)
                    .nickname(name)
                    .email(email)
                    .socialType("kakao")
                    .loginStatus(Status.ACTIVATE)
                    .role("ROLE_USER")
                    .build();

            userRepository.save(user);
            isUser = false;

        } else {
            isUser = true;
        }

        log.info("kakao loginId: {}", loginId);

        return loginId;
    }
}
