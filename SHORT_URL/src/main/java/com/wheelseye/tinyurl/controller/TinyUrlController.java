package com.wheelseye.tinyurl.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import com.wheelseye.tinyurl.entities.TinyLongMapping;
import com.wheelseye.tinyurl.limiter.RateLimiter;
//import com.wheelseye.tinyurl.repo.TinyLongMappingRepository;
import com.wheelseye.tinyurl.service.TinyUrlService;

//Controller Class
//Task 2:Rest API should be used

@RestController
@RequestMapping("/api/v1")
public class TinyUrlController {
//	@Autowired
//	private TinyLongMappingRepository tinylongmappingrepository; //injecting repository
	@Autowired
	private TinyUrlService tinyUrlService; //injecting service class
	@Autowired
	private RateLimiter ratelimiter; //injecting API rate limiter

	/* ---------- ❷ 生成短链 ---------- */
	@PostMapping("/shorten")                  // 由 /input → /shorten
	public ResponseEntity<String> shorten(
			@RequestParam("url") String longUrl,   // 不再收 JSON，直接拿 url=…
			HttpServletRequest request) {

		if (!ratelimiter.limitRateCounter(request)) {
			return ResponseEntity.status(429).body("API HIT Limit Reached");
		}
		String code = tinyUrlService.retrieve(longUrl);   // long → code
		return ResponseEntity.ok("http://localhost:8080/api/v1/" + code + "\n");
	}

	/* ---------- ❸ 重定向 ---------- */
	@GetMapping("/{code}")                    // 由 input?tinyurl=… → /{code}
	public ResponseEntity<Void> redirect(
			@PathVariable String code,
			HttpServletRequest request) {

		if (!ratelimiter.limitRateCounter(request)) {
			return ResponseEntity.status(429).build();
		}
		String longUrl = tinyUrlService.getLongUrlForTinyUrl(code);
		return ResponseEntity.status(302)
				.header("Location", longUrl)
				.build();
	}

//	@PostMapping("/input")
//	public String getInput(@RequestBody TinyLongMapping tinylongmapping,HttpServletRequest request  ) //Taking longUrl and request as a parameter.
//	{
//		if(!ratelimiter.limitRateCounter(request)) //passing client's IP to limit it's API request calls.
//		{
//			return "API HIT Limit Reached";
//		}
//		String tinyexp=tinyUrlService.retrieve(tinylongmapping.getLongURL()); //converting longUrl to tinyUrl and saving both of them to DB.
//		return tinyexp;
//	}
//	@RequestMapping(value="input", method = RequestMethod.GET)
//	public @ResponseBody String redirectToLongUrl(@RequestParam("tinyurl") String temp,HttpServletRequest request ) // input?tinyurl="https://wheelseye.com/cDYvtsc"
//	{
//		int ind=temp.lastIndexOf('/');
//		int ind2=temp.lastIndexOf('"');
//		String tinyUrl=temp.substring(ind+1,ind2);
//		if(!ratelimiter.limitRateCounter(request)) //passing client's IP to limit it's API request calls.
//		{
//			return "API HIT Limit Reached";
//		}
//		String longUrl = tinyUrlService.getLongUrlForTinyUrl(tinyUrl); //redirecting from tinyUrl -> longUrl
//		return longUrl;
//	}
}
