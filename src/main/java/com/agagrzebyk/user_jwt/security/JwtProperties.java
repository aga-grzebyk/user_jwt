package com.agagrzebyk.user_jwt.security;

public class JwtProperties {

    public static final String SECRET = "mojetajnehaslo";
    public static final int EXPIRATION_TIME = 864000000;   // 10 dni
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
