package com.cab.shortenurl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cab.shortenurl.DTO.UrlDTO;
import com.cab.shortenurl.service.ShortenUrlService;

@RestController
public class ShortenUrlController {

	@Autowired
	private ShortenUrlService shortenUrlService;

	public ShortenUrlService getShortenUrlService() {
		return shortenUrlService;
	}

	public void setShortenUrlService(ShortenUrlService shortenUrlService) {
		this.shortenUrlService = shortenUrlService;
	}

	/**
	 * @return
	 */
	@RequestMapping(value="/shortenURL", method = RequestMethod.POST, //
            produces = { MediaType.APPLICATION_JSON_VALUE, //
                    MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public UrlDTO shortenURL(@RequestBody UrlDTO inDTO) {
		String originalURL ="";		
		if (inDTO != null) {
			originalURL = inDTO.getOriginalURL();
		}
		return getShortenUrlService().generateShortURL(originalURL);
	}
}