package com.it.server.rateLimit.provider;

import com.it.server.rateLimit.RateLimit;
import com.it.server.rateLimit.impl.TokenBucketRateLimitImpl;

import java.util.HashMap;
import java.util.Map;

public class RateLimitProvider {
	private Map<String, RateLimit> rateLimitMap=new HashMap<>();

	public RateLimit getRateLimit(String interfaceName){
		if(!rateLimitMap.containsKey(interfaceName)){
			RateLimit rateLimit=new TokenBucketRateLimitImpl(100,10);
			rateLimitMap.put(interfaceName,rateLimit);
			return rateLimit;
		}
		return rateLimitMap.get(interfaceName);
	}
}
