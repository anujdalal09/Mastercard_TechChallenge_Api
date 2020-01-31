package com.mastercard.account.api;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties to interact with Mastercard currency conversion api 
 * 
 * @author Anuj Dalal 01/27/20
 */

@Component
@ConfigurationProperties(prefix = "account.currency-conversion-api-config")
public class ApiConfigProperties {
	private String consumerKey;
	private String keyAlias;
	private String keyPassword;
	private String privateKeyPath;
	public String getConsumerKey() {
		return consumerKey;
	}
	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}
	public String getKeyAlias() {
		return keyAlias;
	}
	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}
	public String getKeyPassword() {
		return keyPassword;
	}
	public void setKeyPassword(String keyPassword) {
		this.keyPassword = keyPassword;
	}
	public String getPrivateKeyPath() {
		return privateKeyPath;
	}
	public void setPrivateKeyPath(String privateKeyPath) {
		this.privateKeyPath = privateKeyPath;
	}
	
}
