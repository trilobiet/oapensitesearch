package oapensitesearch;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.trilobiet.oapen.sitesearch.SiteSearchException;
import com.trilobiet.oapen.sitesearch.SiteSearchResult;
import com.trilobiet.oapen.sitesearch.mysql.MySQLSiteSearchService;

public class MySQLSiteSearchGatewayTest {
	
	private final String url = "jdbc:mysql://strapi-oatoolkit.trilobiet.eu:3306/strapi36_oatoolkit?user=sitesearch&password=Whateveryouwant";
	private final String searchfields = "title, summary, content, `references`, resources, sources, doi, author, tags, keywords";
	private final MySQLSiteSearchService osss = new MySQLSiteSearchService(url, searchfields);
	
	@Test
	public void testSectionsNotEmpty() throws SiteSearchException {
		
		Map<String, String> map;
		map = osss.getSections();
		assertTrue(map.size()>0);
	}
	
	@Test
	public void testQuery1() {
		
		String term = "research institution"; 
		assertDoesNotThrow(() -> osss.search(term));
	}

	@Test
	public void testQuery2() {
		
		String term = "(*¨¨:;\"'";
		assertDoesNotThrow(() -> osss.search(term));
	}

	@Test
	public void testQuery3() {
		
		String term = "Checking OA book policies\n"
				+ "\n"
				+ "26-09-2024 ~ 02:00 … and funders increasingly require that researchers make their books openly available in order to maximise the impact of the research they support. Open access policies vary considerably, …\n"
				+ "Open-acces-for-books/oa-books-landscape/article/the-research-life-cycle-in-relation-to-the-publication-of-an-open-access-book\n"
				+ "The research life cycle in relation to the publication of an open access book\n"
				+ "\n"
				+ "30-09-2024 02:0";
		
		/*
		try {
			printResult(osss.search(term));
		} catch (SiteSearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		assertDoesNotThrow(() -> osss.search(term));
	}

	@Test
	public void testQuery4() {
		
		String term = "((SELECT)";
		assertDoesNotThrow(() -> osss.search(term));
	}
	
	@Test
	public void testQuery5() {
		
		String term = " ";
		
		try {
			printResult(osss.search(term));
		} catch (SiteSearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		assertDoesNotThrow(() -> osss.search(term));
	}

	@SuppressWarnings("unused")
	private void printResult(List<SiteSearchResult> r) {

		r.forEach(System.out::println);
		System.out.println("TOTAL COUNT " + r.size());
	}
	
	
}
