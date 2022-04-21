package com.trilobiet.oapen.sitesearch;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ArticleToResultMapper {
	
	public List<SiteSearchResult> map(List<Article> articles) {
		
		return articles.stream()
			.filter(a -> a.isPublish())
			.flatMap(a -> explode(a))
			.collect(Collectors.toList());
	}
	

	private Stream<SiteSearchResult> explode(Article article) {
		
		return 
			article.getTopics().stream()
			.flatMap(
				(Topic topic) -> topicToSections(topic, article) 
			);		
	}
	
	
	private Stream<SiteSearchResult> topicToSections(Topic topic, Article article) {
		
		return topic.getSections().stream().map( section -> 
		
			SiteSearchResult.builder()
				.title(article.getTitle())
				.text(article.getContent())
				.slug(article.getSlug())
				.topic(topic.getSlug())
				.section(section.id)
				.updatedAt(article.getUpdatedAt())
				.score(article.getScore())
				.build()
		);
	}
	
	
}
