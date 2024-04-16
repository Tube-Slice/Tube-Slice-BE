package TubeSlice.tubeSlice.domain.user;

import TubeSlice.tubeSlice.domain.user.dto.response.KakaoLoginDto;
import TubeSlice.tubeSlice.domain.user.dto.response.LoginResponseDto;
import TubeSlice.tubeSlice.domain.user.dto.response.NaverLoginDto;
import TubeSlice.tubeSlice.global.provider.JwtProvider;
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
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final JwtProvider jwtProvider;




    public LoginResponseDto getJwtTokenAndUserId(String accessToken, String socialType){
        String loginId = null;

        if (socialType.equals("naver")) {
            loginId = loginByNaver(accessToken);
        }
        if (socialType.equals("kakao")) {
            loginId = loginByKakao(accessToken);
        }
        if (loginId==null)   {
            throw new RuntimeException("userId 없음.");
        }

        User findUser = userRepository.findByLoginId(loginId);
        Long userId = findUser.getId(); //db에 저장된 유저의 id 값.

        String token = jwtProvider.create(loginId);  //네이버 로그인 성공시 jwt 토큰 반환.

        return new LoginResponseDto(userId, token);
    }

    private String loginByNaver (String accessToken) {
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<NaverLoginDto> response =
                restTemplate.exchange("https://openapi.naver.com/v1/nid/me", HttpMethod.GET, request, NaverLoginDto.class);

        User user = null;

        String email = response.getBody().getResponse().getEmail();
        String loginId = response.getBody().getResponse().getId();
        String name = response.getBody().getResponse().getName();

        log.info("email : {}",email );
        log.info("loginId : {}", loginId);
        log.info("name : {}",name);

        User findUser = userRepository.findByLoginId(loginId);

        if(findUser == null){
            user = User.builder()
                    .loginId(loginId)
                    .name(name)
                    .email(email)
                    .socialType("naver")
                    .loginStatus(Status.ACTIVATE)
                    .role("ROLE_USER")
                    .build();

            userRepository.save(user);
        } else {
            user = findUser;
        }

        log.info("naver loginId: {}", loginId);

        return loginId;
    }

    private String loginByKakao (String accessToken) {
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoLoginDto> response =
                restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.GET, request, KakaoLoginDto.class);

        User user = null;

        String email = null;
        String loginId = response.getBody().getId();
        String name = response.getBody().getProperties().getNickname();

        log.info("email : {}",email );
        log.info("loginId : {}", loginId);
        log.info("name : {}",name);

        User findUser = userRepository.findByLoginId(loginId);

        if(findUser == null){
            user = User.builder()
                    .loginId(loginId)
                    .name(name)
                    .email(email)
                    .socialType("kakao")
                    .loginStatus(Status.ACTIVATE)
                    .role("ROLE_USER")
                    .build();

            userRepository.save(user);
        } else {
            user = findUser;
        }

        log.info("kakao loginId: {}", loginId);

        return loginId;
    }

}
