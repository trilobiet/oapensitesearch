package com.trilobiet.oapen.sitesearch;

public final class HelperFunctions {
	
	public static String textExcerpt(String text, String searchterm) {
		
		String cleanText = text
			.replaceAll( "<[^>]*>", "") // remove HTML (TODO test it)
			.replaceAll( "\\s+", " " )   // remove whitespace
			.replaceAll( "]\\([^)]*\\)", "") // remove link urls [link description](https://the.url)
			.replaceAll( "[#\\*\\[\\]]", "" ) // remove markdown markup
			;  
		
		int pos = cleanText.toLowerCase().indexOf(searchterm.toLowerCase());
		int len = 200;
		int start = Math.max( pos - 20, 0 );
		int end = Math.min( start + len, cleanText.length() );
		
		cleanText = cleanText.substring( start, end );
		
		int from = Math.max(cleanText.indexOf(" "), 0);
		int to = Math.max(cleanText.lastIndexOf(" "), 0);
		
		cleanText = "…" + cleanText
			.substring( from, to ) + " … "; // remove first and last word fragments

		return cleanText;
	}	

}
