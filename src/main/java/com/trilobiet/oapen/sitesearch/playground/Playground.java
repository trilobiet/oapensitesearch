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
		//String term = "research institution"; 
		//String term = "(*¨¨:;\"'";
		/*String term = "Checking OA book policies\n"
				+ "\n"
				+ "26-09-2024 ~ 02:00 … and funders increasingly require that researchers make their books openly available in order to maximise the impact of the research they support. Open access policies vary considerably, …\n"
				+ "Open-acces-for-books/oa-books-landscape/article/the-research-life-cycle-in-relation-to-the-publication-of-an-open-access-book\n"
				+ "The research life cycle in relation to the publication of an open access book\n"
				+ "\n"
				+ "30-09-2024 02:0";
		*/		
		String term = "((SELECT)";
		
		MySQLSiteSearchService osss = new MySQLSiteSearchService(url, searchfields);

		Map<String, String> map;
		try {
			map = osss.getSections();
			System.out.println("Sections: " + map);
		} catch (SiteSearchException e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		
		List<SiteSearchResult> r;
		try {
			r = osss.search(term);
			r.forEach(System.out::println);
			System.out.println("TOTAL COUNT " + r.size());
		} catch (SiteSearchException e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		
	}

}
