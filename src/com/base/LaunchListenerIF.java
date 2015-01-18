package com.base;
/////////////////////////////////////////////////////////////////////////////
//
//  FILE:           LaunchListenerIF.java
//
//  AUTHOR:          David Keeney
//
//  DATE:           November 9, 2000
//
//  DESCRIPTION:    This defines the listener to the Launch class that launches 
//                  outside processes.
//
//  
/////////////////////////////////////////////////////////////////////////////




public interface LaunchListenerIF
{
	/**
	*	Called when the launched process completes.
	*	@param  process  The process that terminated.
	*	@param  exitStatus   The returned exit status code.
	*/
	public void processFinished(Launch process, int exitStatus);
}

