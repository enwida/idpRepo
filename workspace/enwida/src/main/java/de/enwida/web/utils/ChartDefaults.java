package de.enwida.web.utils;

import java.util.Hashtable;

public class ChartDefaults {
    
	private String username;
    private Hashtable<Integer, NavigationDefaults> chartDefaults;
    
    public ChartDefaults() {
        this.chartDefaults = new Hashtable<Integer, NavigationDefaults>();
    }
    
    public void set(int id, NavigationDefaults defaults) {
        chartDefaults.put(id, defaults);
    }
    
    public NavigationDefaults get(int id) {
        return chartDefaults.get(id);
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
