package com.ath.adminefectivo.config;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.log4j.Log4j2;

/**
 * Obtener el usuario autenticado desde el contexto
 * 
 * @author prv_nparra
 * @since 2025
 */
@Log4j2
public class AuditorAwareImpl implements AuditorAware<String> {

  private static final String NAME_HEADER = "Authorization";
  private static final String BEARER_STRING = "Bearer ";
  private static final String USER_DEFAULT = "System";
  private static final String NAME_CLAIM = "name";
  
  @Override
  public @NonNull Optional<String> getCurrentAuditor() {
    Optional<String> userToken = Optional.of(USER_DEFAULT);
    HttpServletRequest request = RequestContextHolder.getRequest();
    Optional<String> headerAuth = Optional.ofNullable(request.getHeader(NAME_HEADER));

    if (headerAuth.isEmpty()) {
      log.error("No hay header presente en peticion: {}", NAME_HEADER);
      return userToken;
    }

    String tokenAuth = headerAuth.get();
    String[] partsToken = tokenAuth.replace(BEARER_STRING, "").split("\\.");
    if(partsToken.length < 2) {
      log.error("No hay token valido en peticion: {}", NAME_HEADER);
      return userToken;
    }
    tokenAuth = partsToken[0].concat(".").concat(partsToken[1]).concat(".");
    try {
      DecodedJWT jwt = JWT.decode(tokenAuth);
      userToken = Optional.of(jwt.getClaims().get(NAME_CLAIM).asString());
    } catch (Exception e) {
      log.error("Exception en currentAuditor: {}", e.getMessage());
    }
    return userToken;
  }
}
