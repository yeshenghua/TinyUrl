package com.wheelseye.tinyurl.service;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.google.common.hash.*;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.wheelseye.tinyurl.entities.TinyLongMapping;
import com.wheelseye.tinyurl.repo.TinyLongMappingRepository;

//Task 4: Caching layer can be introduced for higher read throughput 


@Component
public class TinyUrlService {
	
	@Autowired
	private TinyLongMappingRepository tinylongmappingrepository; //injecting repository
	 
	
	


	public String getTinyUrl(String longUrl) { //method to create tin
		try { 
			  
			
			
			
           MessageDigest md = MessageDigest.getInstance("MD5"); //converts longUrl to a byte array(size=16)
  
            byte[] messageDigest = md.digest(longUrl.getBytes());  //16bytes array
            
            
         	  	BigInteger no = new BigInteger(1, messageDigest); //converting the byte array into BigInteger
                
         	  	String bin_num=no.toString(2); //converting Biginteger into a BinaryString
         	  	long num=getLONG(bin_num); //converting First 36 bits of the BinaryString(128bit) to Long
         	  	//System.out.println("xx: "+num);
         	  	
         	  	
         	  	
         	  	String hashtext =getbase62(num); //converting Long to base62 (tinyUrl)
                while (hashtext.length() < 6) { 
                  hashtext = "0" + hashtext; 
                } 
               return hashtext; 
       }  
  
        catch (NoSuchAlgorithmException e) { 
            throw new RuntimeException(e); 
        }
        
		
		
		
	}
	//finder method for database
	@Cacheable("urls")
	public String getLongUrlForTinyUrl(String tinyUrl){ 
		
		try 
		{		
			String longUrl = tinylongmappingrepository.findBytinyURL(tinyUrl).getLongURL();
			System.out.println("lookedup from the DB");
			return longUrl;
		}
		catch (NullPointerException e)
		{
			
			 eviction(tinyUrl);
			 return "Not in DB!";
			
		}
		
		
	}
	@CacheEvict(value="urls",key = "{#tinyUrl}")
	public void eviction(String tinyUrl)
	{
		System.out.println("Not in DB!!!");
	}
	
	
	//service for POST Request
	public String retrieve(String longUrl) {
		//url must start from http,https
		UrlValidator urlvalidator=new UrlValidator(new String[] {"http","https"}); 
		//checking if URL is valid or not
		if(urlvalidator.isValid(longUrl)) 
		{
			String tiny=getTinyUrl(longUrl);
			try
			{
				String lookup=tinylongmappingrepository.findBytinyURL(tiny).getLongURL();  //if longUrl already present in DB then return tiny for it
				
				return ("Already Exists! \n https://dushyant.com/"+tiny);
				
			}
			catch(NullPointerException e)
			{
				TinyLongMapping tinylongmapping = new TinyLongMapping();
				tinylongmapping.setLongURL(longUrl);
				tinylongmapping.setTinyURL(tiny);	
				tinylongmappingrepository.save(tinylongmapping);
				return ("https://dushyant.com/"+tiny);
			}
		}
		else
		{
			return "URL must be valid!";
		}
	}
	
	//conversion of Long to Base62	
	public String getbase62(long num) {
		String base="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String result="";
		int cnt=6;
		while(num!=0 && cnt>0)
		{
			long r=num % 62;
			result+=base.charAt((int)r);
			num=num/62;
			cnt--;
		}
		return result;
		
	}
	
	//converting first 36 bits to Long
	public Long getLONG(String bin)
	{
		long num=0;
		long pow=1;
		for(int i=35;i>=0;i--)
		{
			pow=pow * 2;
			if(bin.charAt(i)=='1')
				num=num+pow;
		}
		return num;
	}
	
	
	
}
