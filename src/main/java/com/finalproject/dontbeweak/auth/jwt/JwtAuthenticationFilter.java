package com.finalproject.dontbeweak.auth.jwt;

import com.finalproject.dontbeweak.exception.CustomException;
import com.finalproject.dontbeweak.exception.ErrorCode;
import com.finalproject.dontbeweak.jwtwithredis.Response;
import com.finalproject.dontbeweak.jwtwithredis.UserResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.reactor.DebugAgentEnvironmentPostProcessor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;


// 클라이언트 요청 시 JWT 인증을 하기 위해 설치하는 Custom Filter로
// UsernamePasswordAuthenticationFilter 이전에 실행
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "Bearer";

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    private final Response response;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException, CustomException {

        // Request Header 에서 JWT 토큰 추출
        String accessToken = resolveToken((HttpServletRequest) servletRequest);
        System.out.println("==== 1. 액세스 토큰 추출 완료 from REQUEST HEADER : " + accessToken + " ====");

        // Access Token 유효성 검증
        // 헤더에 토큰이 존재하고 유효한 토큰일 때 통과
        if (accessToken != null && jwtTokenProvider.validateAccessToken(accessToken)) {
            System.out.println("==== 2. [PASS] 유효성 검증 통과 : 올바른 토큰입니다. ====");

            // 토큰이 만료되지 않았을 경우 통과
            if (jwtTokenProvider.checkExpiredToken(accessToken)) {
                System.out.println("==== 3. [PASS] 만료되지 않은 토큰입니다. ====");

                // Redis 에 해당 accessToken logout 여부 확인 (로그아웃 시 RT가 삭제되고 AT가 Redis에 저장됨)
                String isLoggedOut = (String) redisTemplate.opsForValue().get(accessToken);

                System.out.println("==== BLACKLIST : Redis에서 토큰 찾기 ====");

                if (!ObjectUtils.isEmpty(isLoggedOut)) {
                    log.error(ErrorCode.LOGGED_OUT_TOKEN.getMessage(), ErrorCode.LOGGED_OUT_TOKEN.getStatus());
                    response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);

                    return;
                }

                System.out.println("==== 4. [PASS] 로그인 상태의 토큰입니다. ====");

                // 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                System.out.println("==== 5. [PASS] SecurityContext 저장 ====");

            } else {
                // 토큰이 만료된 경우,
                System.out.println("==== 3-1. [FAIL] 만료된 토큰입니다 ====");

                // Redis에서 이전에 만료되어 재발급에 사용된 적 있는 토큰인지 찾기
                String isExpired = (String) redisTemplate.opsForValue().get(accessToken);

                System.out.println("==== 3-2. BLACKLIST : Redis에서 토큰 찾기 ====");

                if (!ObjectUtils.isEmpty(isExpired)) {
                    log.error(ErrorCode.USED_EXPIRED_TOKEN.getMessage(), ErrorCode.USED_EXPIRED_TOKEN.getStatus());
                    response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);

                    return;
                }

//                System.out.println("==== 3-3. Redis에서 영구삭제되어 폐기된 토큰이 아닌지 확인 ====");
//                if (!jwtTokenProvider.getExpiredAccessTokenlifeSpan(accessToken)) {
//                    log.error(ErrorCode.INVALIED_EXPIRED_TOKEN.getMessage(), ErrorCode.INVALIED_EXPIRED_TOKEN.getStatus());
//
//                    return;
//                }

                System.out.println("==== 3-4. [PASS] 사용될 수 있는 만료 토큰입니다. ====");

                jwtTokenProvider.regenerateAccessTokenProcess((HttpServletResponse) servletResponse, accessToken, response);

                return;
            }
        }
        chain.doFilter(servletRequest, servletResponse);
        System.out.println("==== 6. [PASS] chain.dofilter : 1 ====");
    }


    private boolean findTokenInBlackList(String accessToken) {

        String blackListToken = (String) redisTemplate.opsForValue().get(accessToken);

        System.out.println("==== BLACKLIST : Redis에서 토큰 찾기 ====");

        if (!ObjectUtils.isEmpty(blackListToken)) {
            log.error(ErrorCode.TOKEN_IN_BLACKLIST.getMessage(), ErrorCode.TOKEN_IN_BLACKLIST.getStatus());
        }
        return true;
    }

    // Request Header에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
