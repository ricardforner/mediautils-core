package com.it.media.renderer;

import com.it.hbean.HBeanDescriptor;

/**
 * M�dulo com�n descriptor del HBean
 * 
 * @author Ricard Forner
 * @version 1.0
 */
public class NotifyComponent implements HBeanDescriptor {

    // *************************************************************************
    // ** HBEAN M�todos
    // *************************************************************************

	public String hbeanGetPrimaryKey() {
		return ""+System.currentTimeMillis();
	}

	public Object hbeanGetTarget() {
		return this;
	}

	@SuppressWarnings("unchecked")
	public Class hbeanGetTargetClass() {
		return getClass();
	}

}
