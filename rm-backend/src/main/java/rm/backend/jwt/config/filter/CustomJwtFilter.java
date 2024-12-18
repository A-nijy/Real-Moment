package rm.backend.jwt.config.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import rm.backend.jwt.config.auth.CustomUserDetailsService;
import rm.backend.jwt.config.util.JWTProperties;
import rm.backend.jwt.config.util.JwtFunction;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomJwtFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtFunction jwtFunction;
    private final JWTProperties jwtProperties;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 0. 헤더에 "Authorization"와 "Refresh_Token"에 담긴 값을 가져온다. (토큰)
        String header_access = request.getHeader(jwtProperties.getACCESS_STRING());
        String header_refresh = request.getHeader(jwtProperties.getREFRESH_STRING());

        // 1. 헤더에 토큰이 존재하는지 검사 (Authorization에 값이 있고, 그 값이 "Bearer "로 시작하는가? + Refresh_Token에는 값이 없는가) = access 토큰만 요청 받았는가?
        if(header_access != null && header_access.startsWith(jwtProperties.getTOKEN_PREFIX()) && header_refresh == null){

            // 1-1. 앞에 "Bearer " 문자열을 빼고 온전히 토큰만 가져온다.
            String jwtToken = header_access.replace("Bearer ", "");

            // 2. JWT 토큰 유효성 검사
            if(jwtFunction.validateAccessToken(jwtToken)){

                // 3. JWT 토큰을 복호화해서 Claim에 있는 loginId를 가져온다.
                String loginId = jwtFunction.getLoginId(jwtToken);

                // 3-1. Member에 토큰의 정보에 알맞는 Member가 있으면 userDetails 생성
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginId);

                // 토큰의 회원 id와 URI의 id가 일치한지 검증
                String str = request.getRequestURI();
                log.info("{}", str);
                if(str.startsWith("/member")){
                    // 토큰에서 회원 id 추출
                    Long memberId = jwtFunction.getMemberId(jwtToken);
                    // uri에서 id 추출과 동시에 id 비교
                    getIdFromRequest(str, memberId);
                }

                // 3-2. 잘 가져왔나 확인
                if(userDetails != null){

                    // 사용자 식별자(userDetails)와 접근권한 인증용 토큰 생성 (JWT 토큰 아님!!) [ 정확히는 사용자의 인증 정보를 가지고 있는 객체 생성 ]
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    // 스프링 시큐리티에서 현재 사용자의 인증 정보를 설정하는 코드로
                    // 사용자가 인증되었음을 시스템에게 알려준다. (인증된 상태로 세션을 유지 = 시큐리티가 현재 사용자를 식별하고 보호가능)
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                }
            }
        }



        // 1. access 토큰이 만료되어 refresh 토큰을 재요청한 상태 / 헤더에 access와 refresh 토큰 모두 가지고 있는가?
        if(header_refresh != null && header_refresh.startsWith(jwtProperties.getTOKEN_PREFIX()) && header_access == null){

            // 1-1. 앞에 "Bearer " 문자열을 빼고 온전히 토큰만 가져온다.
            String jwtToken = header_refresh.replace("Bearer ", "");

            // 2. JWT 토큰 유효성 검사
            jwtFunction.validateRefreshToken(jwtToken);
        }


        filterChain.doFilter(request, response);
    }



    // URI에 작성한 id와 토큰에서 id 추출하여 같은지 비교하기
    private void getIdFromRequest(String uri, Long id){

        String[] str = uri.split("/");
        log.info("{}", str[2]);
        String tokenId = id.toString();

        if (!str[2].equals(tokenId)){
            throw new IllegalArgumentException("인증된 회원 id와 다른 id로 접근하였습니다.");
        }
    }
}
