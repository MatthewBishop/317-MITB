package com.jagex;

public class Utils {

	public static void startRunnable(Runnable runnable, int priority)
	{
	    if(priority > 10)
	    	priority = 10;
	    Thread thread = new Thread(runnable);
	    thread.start();
	    thread.setPriority(priority);
	}

	public static void reporterror(String s)
	{
	    System.out.println("Error: " + s);
	}

}
