package com.utilsframework.android.suggestions;

import android.location.Address;

public class GeoSearchResult {
 
	private Address address;
 
	public GeoSearchResult(Address address) {
		this.address = address;
	}
 
	public String getAddress(){
 
		String display_address = "";
 
		display_address += address.getAddressLine(0) + "\n";
 
		for(int i = 1; i < address.getMaxAddressLineIndex(); i++)
		{
			display_address += address.getAddressLine(i) + ", ";
		}
 
		display_address = display_address.substring(0, display_address.length() - 2);
 
		return display_address;
	}
 
	public String toString(){
		return getAddress();
	}
}