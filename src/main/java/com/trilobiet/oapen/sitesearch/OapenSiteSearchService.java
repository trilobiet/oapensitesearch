package com.trilobiet.oapen.sitesearch;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OapenSiteSearchService implements SiteSearchService {
	
	public final SiteSearchGateway gateway;
	public final ArticleToResultMapper mapper = new ArticleToResultMapper();

	public OapenSiteSearchService(String url) {
		this.gateway = new OapenSiteSearchGateway(url);
	}

	@Override
	public List<SiteSearchResult> search(String term) throws SiteSearchException {

		List<Article> articles = gateway.search(term);
		List<SiteSearchResult> results = mapper.map(articles);
		
		Map<String,String> sections = gateway.getSections();
		
		return results.stream().map( result -> 
		
			SiteSearchResult.builder()
				.title(result.getTitle())
				.text(textExcerpt(result.getText(), term))
				.slug(result.getSlug())
				.topic(result.getTopic())
				.section(sections.get(result.getSection()))
				.updatedAt(result.getUpdatedAt())
				.score(result.getScore())
			.build()
		)
		.collect(Collectors.toList());
	}

	
	private String textExcerpt(String text, String searchterm) {
		
		String cleanText = text
			.replaceAll( "\\s+", " " )   // remove whitespace
			.replaceAll( "]\\([^)]*\\)", "") // remove link urls [link description](https://the.url)
			.replaceAll( "[#\\*\\[\\]]", "" )
			;  
		
		int pos = cleanText.toLowerCase().indexOf(searchterm.toLowerCase());
		// System.out.println("POS -> " + searchterm + " = " + pos);
		int len = 200;
		int start = Math.max( pos - 20, 0 );
		int end = Math.min( start + len, cleanText.length() );
		
		return cleanText.substring(start, end) + " … ";
	}

	
	@Override
	public Map<String, String> getSections() throws SiteSearchException {

		return gateway.getSections();
	}

	
}
