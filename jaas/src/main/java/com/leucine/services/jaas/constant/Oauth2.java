package com.leucine.services.jaas.constant;

public final class Oauth2 {
  private Oauth2() {
  }

  public static final String AUTHORIZATION_GRANT_TYPE = "authorization_code";

  public static class Okta {
    public static final String USER_FILTER = "(profile.lastName Sw \"%s\" or profile.firstName Sw \"%s\" or profile.email Sw \"%s\" or profile.employeeNumber Sw \"%s\") and (status Sw \"ACTIVE\")";
    public static final String DEFAULT_USER_FILTER = "(status Sw \"ACTIVE\")";
    public static final String USERS_SORT_BY_FIRST_NAME = "profile.firstName";
    public static final String OKTA_USER_READ_SCOPE = "okta.users.read";
    public static final String RESPONSE_TYPE = "response_type";
    public static final String RESPONSE_TYPE_CODE = "code";
    public static final String OPENID_SCOPE = "openid";
    public static final String PROMPT = "prompt";
    public static final String PROMPT_LOGIN = "login";
    public static final String GRANT_TYPE = "grant_type";
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";
    public static final String CODE = "code";
    public static final String REDIRECT_URI = "redirect_uri";
    public static final String ID_TOKEN = "id_token";
    public static final String SCOPE = "scope";
    public static final String STATE = "state";

    public static final String AUTHORIZATION_ENDPOINT = "/v1/authorize";
    public static final String TOKEN_ENDPOINT = "/v1/token";
    public static final String LOGOUT_ENDPOINT = "/v1/logout?id_token_hint=%s&post_logout_redirect_uri=%s";

    public static final String SUBJECT_CLAIM = "sub";
    public enum State {
      LOGIN
    }

    private Okta() {
    }

  }

  public static class Azure {
    public static final String RESPONSE_TYPE = "response_type";
    public static final String OPENID_SCOPE = "openid";
    public static final String PROMPT = "prompt"; // parameter can be used to control the authorization server's interaction with the resource owner (end-user)
    public static final String PROMPT_LOGIN = "login"; // Ask for login password always other supported values are none and consent for silent login and select_account
    public static final String GRANT_TYPE = "grant_type";
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";
    public static final String CODE = "code"; // authorization grant should be returned from the authorization endpoint
    public static final String REDIRECT_URI = "redirect_uri";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String SCOPE = "scope"; // to specify what kind of access or permissions an application is requesting from a user
    public static final String STATE = "state";
    public static final String OID_CLAIM = "oid";
    public static final String LOGOUT_ENDPOINT = "/oauth2/v2.0/logout";
    public static final String AUTHORIZATION_ENDPOINT = "/oauth2/v2.0/authorize";
    public static final String TOKEN_ENDPOINT = "/oauth2/v2.0/token";
    public static final String RESPONSE_MODE = "response_mode";
    public static final String QUERY = "query";


    public enum State {
      LOGIN
    }
  }
}
