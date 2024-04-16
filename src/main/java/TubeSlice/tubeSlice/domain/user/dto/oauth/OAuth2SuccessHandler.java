package TubeSlice.tubeSlice.domain.user.dto.oauth;

import TubeSlice.tubeSlice.domain.user.dto.oauth.CustomOauth2User;
import TubeSlice.tubeSlice.domain.user.UserRepository;
import TubeSlice.tubeSlice.global.provider.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOauth2User oAuth2User = (CustomOauth2User) authentication.getPrincipal();

        String userId = oAuth2User.getName();
        String socialType = oAuth2User.getProvider();   //naver or kakao
        String token = jwtProvider.create(userId);  //네이버 로그인 성공시 jwt 토큰 반환.

        log.info("OAuth2SuccessHandler 실행 userId : {}", userId);

        response.sendRedirect("http://localhost:8080/oauth2/" + socialType + "/" + token);

    }
}
