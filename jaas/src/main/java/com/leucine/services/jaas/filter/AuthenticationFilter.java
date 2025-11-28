package com.leucine.services.jaas.filter;

import com.leucine.services.jaas.constant.ErrorCode;
import com.leucine.services.jaas.constant.Misc;
import com.leucine.services.jaas.constant.Type;
import com.leucine.services.jaas.dto.partial.PartialRoleDto;
import com.leucine.services.jaas.exception.*;
import com.leucine.services.jaas.model.helper.PrincipalUser;
import com.leucine.services.jaas.repository.IClientCredentialRepository;
import com.leucine.services.jaas.service.ITokenService;
import com.leucine.services.jaas.util.Utility;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {
  private static final String AUTHORIZATION = "Authorization";
  private static final String SERVICE_ID = "X-Service-Id";
  private static final String BEARER_TOKEN_PREFIX = "Bearer ";

  private final ITokenService tokenService;
  private final JaasExceptionHandler exceptionHandler;
  private final IClientCredentialRepository clientCredentialRepository;

  public AuthenticationFilter(ITokenService tokenService, IClientCredentialRepository clientCredentialRepository) {
    this.tokenService = tokenService;
    this.clientCredentialRepository = clientCredentialRepository;
    exceptionHandler = new JaasExceptionHandler();
  }

  private String obtainToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TOKEN_PREFIX)) {
      return bearerToken.substring(7);
    }
    return null;
  }

  private String obtainServiceId(HttpServletRequest request) {
    String serviceId = request.getHeader(SERVICE_ID);
    String path = request.getServletPath();
    if (Utility.isEmpty(serviceId) && !path.contains("/api-docs") && !path.contains("/swagger-ui/") && !path.contains("/version.json")) {
      throw new JaasException(ErrorCode.INVALID_SERVICE_ID);
    }
    return serviceId;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    log.trace("[doFilterInternal] Incoming request... request path [{}], request method [{}]", request.getServletPath(), request.getMethod());
    String token = null;
    try {
      String serviceId = obtainServiceId(request);
      log.trace("[doFilterInternal] Request continued... serviceId [{}]", serviceId);
      request.setAttribute(Misc.INTERCEPTOR_SERVICES_ID, serviceId);
      token = obtainToken(request);
      if (!Utility.isEmpty(token)) {
        if (tokenService.isAuthorized(token, Type.Token.ACCESS, request)) {
          PrincipalUser principalUser = tokenService.getPrincipalUser(token);
          log.trace("[doFilterInternal] Request continued... principalUser [{}]", principalUser);
          Set<SimpleGrantedAuthority> authorities = new HashSet<>();
          for (PartialRoleDto role : principalUser.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName() + ""));
          }
          //TODO: create a separate ClientCredentialAuthenticationToken class extending AbstractAuthenticationToken for client credentials authentication
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principalUser, null, authorities);
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
          throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }
      }
    } catch (AccessDeniedException | JaasException | TokenRevokedException | ForbiddenException ex) {
      exceptionHandler.handleException(response, ex);
      return;
    } catch (InvalidTokenException ex) {
      // Authorise the logout request even if the token has expired
      if (ex.getErrorCode().code() == ErrorCode.JWT_ACCESS_TOKEN_EXPIRED.code() && request.getServletPath().equals(Misc.LOGOUT_ENDPOINT)) {
        PrincipalUser principalUser = tokenService.getPrincipalUser(token, ((ExpiredJwtException) ex.getCause()).getClaims());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principalUser, null, null);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } else {
        exceptionHandler.handleException(response, ex);
        return;
      }
    } catch (Exception ex) {
      exceptionHandler.handleException(response, ex);
      return;
    }
    filterChain.doFilter(request, response);
  }
}
