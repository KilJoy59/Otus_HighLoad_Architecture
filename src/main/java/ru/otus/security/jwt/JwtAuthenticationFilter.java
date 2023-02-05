package ru.otus.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import ru.otus.security.JwtUserDetailService;
import ru.otus.util.JwtTokenUtil;
import ru.otus.util.dto.UserSecurityDto;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
    private final JwtUserDetailService jwtUserDetailService;
    private final JwtTokenUtil jwtTokenUtil;

    private final static String OPTIONS = "OPTIONS";

    @Autowired
    public JwtAuthenticationFilter(JwtUserDetailService jwtUserDetailService, JwtTokenUtil jwtTokenUtil) {
        this.jwtUserDetailService = jwtUserDetailService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException {

        String header = req.getHeader(HEADER_STRING);
        String idUser = null;
        String authToken = null;
        if (!OPTIONS.equals(req.getMethod())) {
            if (header != null && header.startsWith(TOKEN_PREFIX)) {
                authToken = header.replace(TOKEN_PREFIX, "");
                try {
                    idUser = jwtTokenUtil.getIdFromToken(authToken);
                } catch (IllegalArgumentException e) {
                    logger.error("an error occured during getting id from token", e);
                } catch (ExpiredJwtException e) {
                    logger.warn("the token is expired and not valid anymore");
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                            "the token is expired and not valid anymore");
                } catch (Exception e) {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                }
            } else {
                logger.warn("couldn't find bearer string, will ignore the header");
            }
            if (idUser != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserSecurityDto user = jwtUserDetailService.getUserByIdUser(Long.parseLong(idUser));

                if (jwtTokenUtil.validateToken(authToken, user)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, "", new ArrayList<>());
                    logger.info("authenticated user " + idUser + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        try {
            chain.doFilter(req, res);
        } catch (Exception e) {
            resolver.resolveException(req, res, null, e);
        }
    }

}
