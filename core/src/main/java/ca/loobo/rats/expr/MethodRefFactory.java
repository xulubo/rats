package ca.loobo.rats.expr;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import ca.loobo.rats.annotations.ExprHandler;
import ca.loobo.rats.annotations.ExprMethod;
import ca.loobo.rats.exceptions.ExprMethodNotFoundException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component
public class MethodRefFactory {
	private static final Logger logger  = LoggerFactory.getLogger(MethodRefFactory.class);
	private static Map<String, String> typeMap = Maps.newHashMap();
	private Map<String, Set<MethodRef>> methodMap = new HashMap<String, Set<MethodRef>>();
	
	@Resource
	ApplicationContext ctx;
	
	static {
		typeMap.put("boolean", 				"Z");
		typeMap.put("java.lang.Boolean", 	"Z");
		typeMap.put("byte", 				"B");
		typeMap.put("java.lang.Byte", 		"B");
		typeMap.put("char", 				"C");
		typeMap.put("java.lang.Char", 		"C");
		typeMap.put("long", 				"J");
		typeMap.put("java.lang.Long", 		"J");
		typeMap.put("int", 					"I");
		typeMap.put("java.lang.Integer", 	"I");
		typeMap.put("short", 				"S");
		typeMap.put("java.lang.Short", 		"S");
		typeMap.put("float", 				"F");
		typeMap.put("java.lang.Float", 		"F");
		typeMap.put("double", 				"D");
		typeMap.put("java.lang.Double", 	"D");
		typeMap.put("java.lang.Object", 	"L");
		typeMap.put("array", 				"[");
	}
	
	@PostConstruct
	public void lookupMethods() {
		Map<String, Object> beans = ctx.getBeansWithAnnotation(ExprHandler.class);
		for(Object o : beans.values()) {
			ExprHandler h = o.getClass().getAnnotation(ExprHandler.class);
			String instanceName = h.value();
			Method[] methods = o.getClass().getMethods();
			for(Method m : methods) {
				if (m.isAnnotationPresent(ExprMethod.class)) {
					MethodRef ref = new MethodRef(o, m);
					registerMethod(instanceName, m.getName(), m.getParameterTypes(), ref);
					ExprMethod annotation = m.getAnnotation(ExprMethod.class);
					if (!StringUtils.isBlank(annotation.alias())) {
						registerMethod(instanceName, annotation.alias(), m.getParameterTypes(), ref);
					}
				}
			}
		}
	}
	
	private void registerMethod(String instanceName, String methodName, Class<?>[] parameterTypes, MethodRef ref) {
		String key = methodKey(instanceName, methodName, parameterTypes.length);
		Set<MethodRef> refs = methodMap.get(key);
		if (refs == null) {
			refs = new HashSet<MethodRef>();
			methodMap.put(key, refs);
		}
		else if (refs.contains(ref) ) {
			throw new RuntimeException("method " + ref.getSignature() + " has duplicated definiation");
		}
		
		refs.add(ref);
		
		logger.debug("discovered method {}", key);		
	}
	
	private static String methodKey (String instanceName, String methodName, int numOfArgs) {
		String prefix = StringUtils.isBlank(instanceName) ? "" : (instanceName + ".");
		return prefix + methodName + ":" + numOfArgs;

	}

	private static String parametersSignature(Object[] args) {
		List<Class<?>> types = Lists.newLinkedList();
		for(Object arg : args) {
			types.add(arg.getClass());
		}
		return parametersSignature(types);
	}
	
	private static String parametersSignature(Collection<Class<?>> types) {
		StringBuilder signature = new StringBuilder();
		for(Class<?> v : types) {
			String type = typeMap.get(v.getName());
			if (type != null) {
				signature.append(type);
			}
			else if (v.getName().startsWith("[")) {
				signature.append(v.getName());
			}
			else {
				signature.append("L").append(v.getName()).append(";");
			}
		}
		return signature.toString();
	}
	
	public static class MethodRef {
		Method method;
		Object instance;
		
		MethodRef(Object instance, Method method) {
			this.instance = instance;
			this.method = method;
		}

		public Class<?>[] getParameterTypes() {
			return method.getParameterTypes();
		}
		
		private String getSignature() {
			String signature = parametersSignature(Arrays.asList(method.getParameterTypes()));
			return signature;
		}

		@Override
		public int hashCode() {
			return getSignature().hashCode();
		}
	}

	public MethodRef getMethodRef(String instanceName, String methodName, Object[] args) {
		String methodKey = methodKey(instanceName, methodName, args.length);
		logger.debug("looking for method {}", methodKey);
		
		Set<MethodRef> refs = methodMap.get(methodKey);
		if (refs == null) {
			throw new ExprMethodNotFoundException("method '" + methodKey + "' couldn't be found");
		}
		
		for(MethodRef ref : refs) {
			boolean applicable = true;
			Class<?>[] parameterTypes = ref.getParameterTypes();
			
			for(int i=0; i<parameterTypes.length; i++) {
				if (!parameterTypes[i].isAssignableFrom(args[i].getClass())) {
					logger.debug("{} can't be assigned to parameter type {}", args[i].getClass(), parameterTypes[i]);
					applicable = false;
					break;
				}
			}
			
			if (applicable) {
				return ref;
			}
		}
		
		throw new ExprMethodNotFoundException("method " + methodKey + "(" + parametersSignature(args) + ") couldn't be found");
	}
}
