package com.quickplay.restbot.param.resolver;


public interface ValueResolver {

	String resolve(String value, ValueResolverChain chain);
}
