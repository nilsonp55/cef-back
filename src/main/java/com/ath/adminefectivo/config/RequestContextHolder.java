package com.ath.adminefectivo.config;

import javax.servlet.http.HttpServletRequest;

public class RequestContextHolder {
  
  private static final ThreadLocal<HttpServletRequest> currentRequest = new ThreadLocal<>();
  
  public static void setRequest(HttpServletRequest request) {
    currentRequest.set(request);
  }

  public static HttpServletRequest getRequest() {
    return currentRequest.get();
  }
  
  public static void clear() {
    currentRequest.remove();
  }
}
