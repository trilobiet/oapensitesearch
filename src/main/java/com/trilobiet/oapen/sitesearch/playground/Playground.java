package com.trilobiet.oapen.sitesearch.playground;

import java.util.List;
import java.util.Map;

import com.trilobiet.oapen.sitesearch.OapenSiteSearchService;
import com.trilobiet.oapen.sitesearch.SiteSearchException;
import com.trilobiet.oapen.sitesearch.SiteSearchResult;

class Playground {
	
	public static void main(String[] args) throws SiteSearchException {
		
		String url = "mongodb://localhost:27019";
		
		OapenSiteSearchService osss = new OapenSiteSearchService(url);

		Map<String, String> map = osss.getSections();
		System.out.println(map);
		
		List<SiteSearchResult> r = osss.search("mythbusting");
		
		r.forEach(System.out::println);
		
		System.out.println("TOTAL COUNT " + r.size());
		
	}

}
