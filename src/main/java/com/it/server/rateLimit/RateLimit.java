package com.it.server.rateLimit;

public interface RateLimit {
	//获取访问许可
	boolean getToken();
}
