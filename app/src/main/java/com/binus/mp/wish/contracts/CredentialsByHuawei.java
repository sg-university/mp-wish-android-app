package com.binus.mp.wish.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CredentialsByHuawei {
    @JsonProperty("union_id")
    private String unionId;
    @JsonProperty("open_id")
    private String openId;
    @JsonProperty("authorization_code")
    private String authorizationCode;
    @JsonProperty("access_token")
    private String accessToken;

    @Override
    public String toString() {
        return "CredentialsByHuawei{" +
                "unionId='" + unionId + '\'' +
                ", openId='" + openId + '\'' +
                ", authorizationCode='" + authorizationCode + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }

    public CredentialsByHuawei() {
    }

    public CredentialsByHuawei(String unionId, String openId, String authorizationCode, String accessToken) {
        this.unionId = unionId;
        this.openId = openId;
        this.authorizationCode = authorizationCode;
        this.accessToken = accessToken;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
