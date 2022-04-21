package com.trilobiet.oapen.sitesearch;

import java.util.List;
import java.util.Map;

interface SiteSearchGateway {
	
	 List<Article> search(String term) throws SiteSearchException;
	 Map<String,String> getSections() throws SiteSearchException;
	 String createIndex(List<String> fields) throws SiteSearchException;
	 
}
