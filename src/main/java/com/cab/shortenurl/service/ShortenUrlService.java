package com.cab.shortenurl.service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.cab.shortenurl.DTO.UrlDTO;

@Service
public class ShortenUrlService {

	private String charSetStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	private final int base = charSetStr.length();

	private AtomicInteger counter = new AtomicInteger(10);

	/**
	 * Method returns short url by performing following 
	 * 1. Check if the url entered by user is a valid 
	 * 2. Get a random number using AtomicInteger 
	 * 3. Perform Base62 encode on this number and set as short url
	 * 
	 * @param url
	 * @return
	 */
	/**
	 * @param url
	 * @return
	 */
	public UrlDTO generateShortURL(String url) {

		UrlDTO urlDTO = new UrlDTO();
		urlDTO.setOriginalURL(url);
		urlDTO.setShortenedURL("");

		if (isValidURL(url)) {
			String[] tempArray = url.split("://");
			String domain = null;
			if ( tempArray[1].contains("www.")) {
				domain = tempArray[1].split("\\.")[1];
			} else {
				domain = tempArray[1];
			}
			// get the random number
			final long nextNumber = getNextNumber();
			// convert the number to base62 and set it as shortUrl
			String shortUrl = convertAndGetBase62(nextNumber);
			urlDTO.setShortenedURL(tempArray[0] + "://" + domain.substring(0, 2) + "/" + shortUrl);
		}

		return urlDTO;
	}

	/**
	 * Check whether the url entered by user is a valid 
	 * @param url
	 * @return
	 */
	public boolean isValidURL(String url) {
		boolean isValid = Boolean.FALSE;
		try {
			URL u = new URL(url);
			HttpURLConnection huc = (HttpURLConnection) u.openConnection();
			//huc.setRequestMethod("GET");
			huc.connect();
			int code = huc.getResponseCode();
			System.out.println(code);
			// all 2XX and 3XX are considered to be success or redirection respectively
			// as per https://en.wikipedia.org/wiki/List_of_HTTP_status_codes#2xx_Success
			if (code >= 200 && code < 400) {
				isValid = Boolean.TRUE;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return isValid;
	}

	/**
	 * Method converts 'nextNumber' to Base 62 encode
	 * @param nextNumber
	 * @return
	 */
	private String convertAndGetBase62(long nextNumber) {
		StringBuffer sb = new StringBuffer();

		while (nextNumber > 0) {
			int remainder = (int) (nextNumber % base);
			sb.append(charSetStr.charAt(remainder));
			nextNumber = nextNumber / base;
		}
		return sb.toString();
	}

	/**
	 * Returns an incremented and unique number.
	 * 
	 * @return
	 */
	public long getNextNumber() {
		int counterValue = counter.incrementAndGet();
		long systemTime = Long.valueOf(counterValue + System.currentTimeMillis());
		return systemTime;
	}

	
}
