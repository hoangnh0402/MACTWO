package com.hit.userservice.util.constant;

public class UrlConstant {
    private UrlConstant() {}

    public static final class Auth {
        private Auth() {}
        private static final String PRE_FIX = "/auth";
        public static final String LOGIN = PRE_FIX + "/login";
        public static final String LOGOUT = PRE_FIX + "/logout";
        public static final String SIGNUP = PRE_FIX + "/signup";
        public static final String VERIFY_SIGNUP = SIGNUP + "/verify";
        public static final String FORGOT_PASS = PRE_FIX + "/forgot-password";
        public static final String VERIFY_FORGOT_PASS = FORGOT_PASS + "/verify";
    }

    public static final class User {
        private User() {}
        private static final String PRE_FIX = "/users";
        public static final String GET_ALL = PRE_FIX;
        public static final String GET_BY_ID = PRE_FIX + "/{id}";
        public static final String UPDATE = PRE_FIX + "/{id}";
        public static final String DELETE = PRE_FIX + "/{id}";
    }
}
