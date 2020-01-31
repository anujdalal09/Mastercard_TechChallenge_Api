package com.mastercard.account.api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mastercard.api.core.ApiConfig;
import com.mastercard.api.core.exception.ApiException;
import com.mastercard.api.core.model.Environment;
import com.mastercard.api.core.model.RequestMap;
import com.mastercard.api.core.security.oauth.OAuthAuthentication;
import com.mastercard.api.currencyconversion.ConversionRate;

/**
 * Class to implement Mastercard currency conversion api usage methods
 * 
 * @author Anuj Dalal 01/27/20
 */

@Service
public class CurrencyConversionApiService {

	@Autowired
	ApiConfigProperties apiConfigProperties;

	/**
	 * Method that returns the converted amount from source account currency code to destination account currency code
	 * 
	 * @param fromCurrency, currency code of source account
	 * @param toCurrency, currency code of destination account
	 * @param amount, amount to be transferred from source to destination account
	 * @returns converted amount in destination currency in BigDecimal
	 * @throws FileNotFoundException, if there is any issue during parsing the private key for Mastercard api usage
	 * @throws ApiException, if there is any issue with the ApiRequest
	 * @throws ParseException, if there is an issue while setting environment parameters
	 */
	public BigDecimal getConvertedAmount(String fromCurrency, String toCurrency, BigDecimal amount) throws FileNotFoundException, ApiException, ParseException {
		// Setting up the authentication parameters to use mastercard currency conversion api
		InputStream is = new FileInputStream(apiConfigProperties.getPrivateKeyPath());
		ApiConfig.setAuthentication(new OAuthAuthentication(apiConfigProperties.getConsumerKey(), is, apiConfigProperties.getKeyAlias(), apiConfigProperties.getKeyPassword()));
		ApiConfig.setDebug(true);
		ApiConfig.setEnvironment(Environment.parse("sandbox_mtf"));
		// Building request for mastercard ConversionRate class
		RequestMap map = new RequestMap();
		String fxDate = LocalDate.now().minusDays(1).toString();
		map.set("fxDate", fxDate);
		map.set("transCurr", toCurrency);
		map.set("crdhldBillCurr", fromCurrency);
		map.set("transAmt", amount);
		// Executing the mastercard conversion rate api
		ConversionRate response = ConversionRate.query(map);
		BigDecimal amountInConvertedCurrency = new BigDecimal(response.get("data.crdhldBillAmt").toString());
		return amountInConvertedCurrency;
	}
}
