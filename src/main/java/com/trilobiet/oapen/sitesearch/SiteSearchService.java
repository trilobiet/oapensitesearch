package com.trilobiet.oapen.sitesearch;

import java.util.List;
import java.util.Map;

public interface SiteSearchService {
	
	List<SiteSearchResult> search(String term) throws SiteSearchException;
	Map<String, String> getSections() throws SiteSearchException;
}
