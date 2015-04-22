package com.quickplay.restbot.caze;

public class CaseSessionHolder {

	private static TestContext context;
	private static CaseSession globalSession = new CaseSession();
	
    private static final ThreadLocal<CaseSession> contextHolder =
        new ThreadLocal<CaseSession>() {
            @Override protected CaseSession initialValue() {
                return new CaseSession();
        }
    };

    public static CaseSession currentSession() {
        return contextHolder.get();
    }
    
    public static CaseSession globalSession() {
    	return globalSession;
    }
    
	
	public static TestContext getContext() {
		return context;
	}

	public static void setContext(TestContext context) {
		CaseSessionHolder.context = context;
	}    
}
