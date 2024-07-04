package com.trilobiet.oapen.sitesearch.playground;

import java.util.List;
import java.util.Map;

import com.trilobiet.oapen.sitesearch.SiteSearchException;
import com.trilobiet.oapen.sitesearch.SiteSearchResult;
import com.trilobiet.oapen.sitesearch.mysql.MySQLSiteSearchService;

class Playground {
	
	public static void main(String[] args) {
		
		String url = "jdbc:mysql://strapi-oatoolkit.trilobiet.eu:3306/strapi36_oatoolkit?user=sitesearch&password=Whateveryouwant";
		String searchfields = "title, summary, content, `references`, resources, sources, doi, author, tags, keywords";
		
		MySQLSiteSearchService osss = new MySQLSiteSearchService(url, searchfields);

		Map<String, String> map;
		try {
			map = osss.getSections();
			System.out.println("Sections: " + map);
		} catch (SiteSearchException e) {
			System.out.println("Exception: " + e.getMessage());
		}
		
		List<SiteSearchResult> r;
		try {
			r = osss.search("author");
			r.forEach(System.out::println);
			System.out.println("TOTAL COUNT " + r.size());
		} catch (SiteSearchException e) {
			System.out.println("Exception: " + e.getMessage());
		}
		
	}

}
