package com.wheelseye.tinyurl.limiter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

//Task 5: Client wise rate limiter is required.



@Component
public class RateLimiter {
	@Autowired
	StringRedisTemplate redisTemplate;
	
	public boolean limitRateCounter(HttpServletRequest request) //returns true if API hit limit reached
	{
		

		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
        String ts= sdf.format(cal.getTime()); //current timestamp for client
       // String keyname=request.getRemoteAddr()+":"+ts;//creating key in the format "ip:timestamp"
        //redisTemplate.opsForValue().increment(keyname);//incrementing value for the key
        //String current=redisTemplate.opsForValue().get(keyname); //current value for the key
        
        
        int hh=Integer.parseInt(ts.substring(0,2));
        int mm=Integer.parseInt(ts.substring(3,5));
        int ss=Integer.parseInt(ts.substring(6));
        //System.out.println(ts+" "+ts.substring(0, 2));
        //System.out.println("time is "+hh+" | "+mm+" "+ss);
        int seconds=60; //to avoid error at 00:00:00
        seconds+=(hh*60*60)+(mm*60)+ss; 
        String current_second=Integer.toString(seconds);
        String previous_second=Integer.toString(seconds-1);
        String keyname_current=request.getRemoteAddr()+":"+current_second;
        String keyname_previous=request.getRemoteAddr()+":"+previous_second;
        String cur=redisTemplate.opsForValue().get(keyname_current);
        String prev=redisTemplate.opsForValue().get(keyname_previous);
        
        if(prev==null)
        {	
        	prev="0";
        }
        if(cur==null)
        {
        	cur="0";
        }
        
        int temp1=Integer.parseInt(cur);
        int temp2=Integer.parseInt(prev);
        
        if(temp1 < 4)
        	{
        		redisTemplate.opsForValue().increment(keyname_current);
        		temp1++;
        	
        	}
        
        
        System.out.println("keyname: "+keyname_previous+" current value: "+prev);
        System.out.println("keyname: "+keyname_current+" current value: "+cur);
        
       
        
        if(temp1+temp2 > 5) //if API hits surpasses 5 times in 2 seconds then API requested is rejected
        	{
        		//System.out.println("Reached");
        		return false;
        	}
        else
        	{
	        	redisTemplate.opsForValue().increment(keyname_current); // Counter is increased for the particular key
	        	redisTemplate.expire(keyname_current, 10, TimeUnit.SECONDS); 
	        	return true;
        	}
        
		
	}
	

}
