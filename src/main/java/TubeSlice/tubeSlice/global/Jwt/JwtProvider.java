package TubeSlice.tubeSlice.global.Jwt;

import TubeSlice.tubeSlice.global.response.code.resultCode.ErrorStatus;
import TubeSlice.tubeSlice.global.response.exception.handler.GeneralHandler;
import TubeSlice.tubeSlice.global.response.exception.handler.UserHandler;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {

    private final Key key;

    private static final String AUTHORITIES_KEY = "auth";

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    //jwt토큰 생성
    public String create(Long userId, String loginId) {

        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + 1000 * 60 * 60 * 24 * 20);

        String jwt  = Jwts.builder()
                .claim(AUTHORITIES_KEY, "ROLE_USER")
                .claim("userId", userId)
                .setExpiration(accessTokenExpiresIn)
                .setSubject(loginId)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        log.info("jwt: {}", jwt);

        return jwt;
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            log.info("user의 authentication 정보가 존재하지않습니다.");
        }

        String username = claims.getSubject();
        log.info("username " + username);
        if(!StringUtils.hasText(username)) {
            log.info("username이 null입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(username, "", authorities);

        // 추출된 userId를 SecurityContextHolder에 저장
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, "", authorities));

        return SecurityContextHolder.getContext().getAuthentication();
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("토큰이 만료되었습니다.");
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("유효하지않은 access token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원하지않는 token입니다.");
        } catch (IllegalArgumentException e) {
            log.info("유효하지않은 token입니다.");
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
