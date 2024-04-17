package TubeSlice.tubeSlice;

import TubeSlice.tubeSlice.global.filter.JwtAuthenticationFilter;
import TubeSlice.tubeSlice.domain.oauth.OAuth2SuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;

@Configurable
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final DefaultOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity)throws Exception{
        httpSecurity
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource())
                )
                .csrf(CsrfConfigurer::disable) //사이트 요청에 대한 처리 하지 않음.
                .httpBasic(HttpBasicConfigurer::disable) //유저 이름과 비밀번호를 입력하여 인증하는 방식 사용 안함. -> bearerToken 사용할거임.
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
//                .authorizeHttpRequests(request -> request
//                        .requestMatchers("/","/api/v1/auth/**", "/oauth2/**").permitAll()
//                        .requestMatchers("/api/v1/user/**").hasRole("USER")
//                        .requestMatchers("/api/v1/user/**").hasRole("ADMIN")
//                        .anyRequest().authenticated()
//                )
                //oauth 로그인 관련 부분
                .oauth2Login(oauth2 -> oauth2
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/login/oauth2/code/*"))
                        .userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService))  //로그인 완료 후 회원 정보 받기.
                        .successHandler(oAuth2SuccessHandler)   //로그인 완료후 jwt 토큰 발급.
                )
//                .exceptionHandling(exceptionHandling -> exceptionHandling
//                        .authenticationEntryPoint(new FailedAuthenticationEntryPoint())
//                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return httpSecurity.build();
    }

    //웹 애플리케이션이 다른 도메인에서 리소스를 요청할 수 있는 권한을 부여
    private CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/v1/**", corsConfiguration);

        return source;

    }

    //인증 진입 지점에서 인증 실패 시 처리를 담당
    class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint{
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"code\":\"NP\", \"message\":\"No Permission\"}");
            //"{"code":"NP", "message":"No Permission"}"
            log.info("response: {}", response);
        }
    }



}
