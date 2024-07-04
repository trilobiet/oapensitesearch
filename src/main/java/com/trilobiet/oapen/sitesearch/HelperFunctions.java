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
		// System.out.println("POS -> " + searchterm + " = " + pos);
		int len = 200;
		int start = Math.max( pos - 20, 0 );
		int end = Math.min( start + len, cleanText.length() );
		
		cleanText = cleanText.substring( start, end );
		cleanText = "…" + cleanText
			.substring( cleanText.indexOf(" "), cleanText.lastIndexOf(" ") ) + " … "; // remove first and last word fragments 

		return cleanText;
	}	

}
