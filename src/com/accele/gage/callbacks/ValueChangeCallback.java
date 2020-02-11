package com.accele.gage.callbacks;

public interface ValueChangeCallback<T> {

	public void call(T oldValue, T newValue);
	
}
