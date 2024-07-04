package com.trilobiet.oapen.sitesearch.mysql;

import java.util.List;
import java.util.Map;

import com.trilobiet.oapen.sitesearch.SiteSearchException;
import com.trilobiet.oapen.sitesearch.SiteSearchResult;
import com.trilobiet.oapen.sitesearch.SiteSearchService;

public class MySQLSiteSearchService implements SiteSearchService {
	
	MySQLSiteSearchGateway gateway;
	
	public MySQLSiteSearchService(String url, String searchfields) {
		
		// searchfields = "title, summary, content, `references`, resources, sources, doi, author, tags, keywords";
		this.gateway = new MySQLSiteSearchGateway(url, searchfields);
	}

	@Override
	public List<SiteSearchResult> search(String term) throws SiteSearchException {

		return gateway.search(term);
	}

	@Override
	public Map<String, String> getSections() throws SiteSearchException {

		return gateway.getSections();
	}
	
}
