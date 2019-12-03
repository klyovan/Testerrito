package com.netcracker.testerritto.security;

public class JwtProperties {
  public static final String SECRET = "testerittoNC2019";
  public static final int EXPIRATION_TIME = 10_800_000; // 3 hours
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String HEADER_STRING = "Authorization";
}
