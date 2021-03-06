package com.cab;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.cab.shortenurl.BootApplication;
import com.cab.shortenurl.DTO.UrlDTO;
import com.cab.shortenurl.service.ShortenUrlService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BootApplication.class)
@WebAppConfiguration
public class TestShortUrl {

	protected MockMvc mvc;
	@Autowired
	WebApplicationContext webApplicationContext;

	protected void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	@Test
	public void testRetrieveShortUrl() {		
		try {
			String longURL = "http://facebook.com";
			MvcResult mvcResult = getResult(longURL);
			int status = 0;
			if (mvcResult != null) {
				status = mvcResult.getResponse().getStatus();
			}
			assertEquals(200, status);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	public MvcResult getResult(String longURL) throws JsonProcessingException, Exception {
		setUp();
		String uri = "/shortenURL";
		UrlDTO urlDTO = new UrlDTO();
		urlDTO.setOriginalURL(longURL);
		urlDTO.setShortenedURL("");

		String inputJson;
		inputJson = mapToJson(urlDTO);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		return mvcResult;
	}

	@Test
	public void testRetrieveLongUrl() {		
		try {
			String longURL = "http://cabonline.com";
			//get the longURL back from the map stored
			ShortenUrlService service = new ShortenUrlService();
			String retrievedLongURlFromMap = "";
			UrlDTO responseDTO = service.generateShortURL(longURL);
			if (responseDTO != null) {
				retrievedLongURlFromMap = service.getLongURL(responseDTO.getShortenedURL());
			}
			assertEquals(longURL, retrievedLongURlFromMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
