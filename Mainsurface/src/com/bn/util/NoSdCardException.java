package com.bn.util;

public class NoSdCardException extends Exception 
{
	private static final long serialVersionUID = 1L;
	public NoSdCardException()
    {   	
    }
    public NoSdCardException(String msg)
    {
    	super(msg);
    }
}
