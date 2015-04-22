package ca.loobo.rats.param.resolver;


public interface ValueResolver {

	String resolve(String value, ValueResolverChain chain);
}
