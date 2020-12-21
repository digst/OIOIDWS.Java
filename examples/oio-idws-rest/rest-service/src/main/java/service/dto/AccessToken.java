package service.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

public class AccessToken {
	private String token;
	private String tokenType;
	private int expiresIn;

	@JsonGetter("access_token")
	public String getToken() {
		return token;
	}

	public void setToken(String accessToken) {
		this.token = accessToken;
	}

	@JsonGetter("token_type")
	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	@JsonGetter("expires_in")
	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
}
