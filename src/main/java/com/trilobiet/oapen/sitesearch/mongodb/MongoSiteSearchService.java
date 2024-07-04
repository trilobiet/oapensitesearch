package com.trilobiet.oapen.sitesearch.mongodb;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.trilobiet.oapen.sitesearch.SiteSearchException;
import com.trilobiet.oapen.sitesearch.SiteSearchResult;
import com.trilobiet.oapen.sitesearch.SiteSearchService;

import com.trilobiet.oapen.sitesearch.HelperFunctions;

public class MongoSiteSearchService implements SiteSearchService {
	
	public final MongoSiteSearchGateway gateway;
	public final ArticleToResultMapper mapper = new ArticleToResultMapper();

	
	public MongoSiteSearchService(String url) {
		this.gateway = new MongoSiteSearchGateway(url);
	}

	
	@Override
	public List<SiteSearchResult> search(String term) throws SiteSearchException {

		List<Article> articles = gateway.search(term);
		List<SiteSearchResult> results = mapper.map(articles);
		
		Map<String,String> sections = gateway.getSections();
		
		return results.stream().map( result -> 
		
			SiteSearchResult.builder()
				.title(result.getTitle())
				.text(HelperFunctions.textExcerpt(result.getText(), term))
				.slug(result.getSlug())
				.topic(result.getTopic())
				.section(sections.get(result.getSection()))
				.updatedAt(result.getUpdatedAt())
				.score(result.getScore())
			.build()
		)
		.collect(Collectors.toList());
	}

	
	@Override
	public Map<String, String> getSections() throws SiteSearchException {

		return gateway.getSections();
	}
	
}