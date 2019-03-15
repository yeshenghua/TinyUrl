package com.wheelseye.tinyurl.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wheelseye.tinyurl.entities.TinyLongMapping;
import com.wheelseye.tinyurl.limiter.RateLimiter;
import com.wheelseye.tinyurl.repo.TinyLongMappingRepository;
import com.wheelseye.tinyurl.service.TinyUrlService;

//Controller Class
//Task 2:Rest API should be used

@RestController
public class TinyUrlController {
	@Autowired
	private TinyLongMappingRepository tinylongmappingrepository; //injecting repository
	@Autowired
	private TinyUrlService tinyUrlService; //injecting service class
	@Autowired
	private RateLimiter ratelimiter; //injecting API rate limiter
	
	@PostMapping("/input")
	public String getInput(@RequestBody TinyLongMapping tinylongmapping,HttpServletRequest request  ) //Taking longUrl and request as a parameter.
	{	
		if(!ratelimiter.limitRateCounter(request)) //passing client's IP to limit it's API request calls.
		{
			return "API HIT Limit Reached";
		}
		String tinyexp=tinyUrlService.retrieve(tinylongmapping.getLongURL()); //converting longUrl to tinyUrl and saving both of them to DB.
		return tinyexp;
	}
	@RequestMapping(value="input", method = RequestMethod.GET)
	public @ResponseBody String redirectToLongUrl(@RequestParam("tinyurl") String temp,HttpServletRequest request ) // input?tinyurl="https://wheelseye.com/cDYvtsc"
	{
		int ind=temp.lastIndexOf('/');
		int ind2=temp.lastIndexOf('"');
		String tinyUrl=temp.substring(ind+1,ind2);
		if(!ratelimiter.limitRateCounter(request)) //passing client's IP to limit it's API request calls.
		{
			return "API HIT Limit Reached";
		}
		String longUrl = tinyUrlService.getLongUrlForTinyUrl(tinyUrl); //redirecting from tinyUrl -> longUrl
		return longUrl;
	}
}
