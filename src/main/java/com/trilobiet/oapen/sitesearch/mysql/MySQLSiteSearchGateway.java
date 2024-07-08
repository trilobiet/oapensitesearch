package com.trilobiet.oapen.sitesearch.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trilobiet.oapen.sitesearch.HelperFunctions;
import com.trilobiet.oapen.sitesearch.SiteSearchException;
import com.trilobiet.oapen.sitesearch.SiteSearchResult;

class MySQLSiteSearchGateway {
	
	// connection url
	private final String url;
	private final String sqlSelect;
	
	private final String getSqlSelect(String searchfields) { 
	
		return ""
		+ "SELECT \n"
		+ "	match(" + searchfields + ") against (? IN NATURAL LANGUAGE MODE) as score, \n"
		+ "	sections.slug as sectionSlug, \n"
		+ " topics.slug as topicSlug, \n"
		+ "	any_value(articles.slug) as articleSlug, \n"
		+ "	any_value(articles.title) as title, \n"
		+ "	any_value(articles.content) as content, \n"
		+ " any_value(articles.publish) and any_value(topics.publish) and any_value(sections.publish) as publish, \n"
		+ " any_value(articles.updated_at) as updatedAt \n"
		+ "FROM \n"
		+ "	articles \n"
		+ "	left join articles_topics__topics_articles arto on arto.article_id = articles.id \n"
		+ "	left join topics on topics.id = arto.topic_id \n"
		+ "	left join sections_topics__topics_sections secto on secto.topic_id = topics.id \n"
		+ "	left join sections on sections.id = secto.section_id \n"
		+ "WHERE \n"
		+ "	articles.published_at is not null \n"
		+ " and topics.published_at is not null \n"
		+ "GROUP BY \n"
		+ "	sectionSlug, topicSlug, articleSlug, score \n"
		+ "HAVING \n"
		+ "	score > 0 \n"
		+ "ORDER BY \n"
		+ "	score DESC, title ASC \n"
		+ "";
	}	
	
	private final String sqlSections = ""
		+ "SELECT slug, name "
		+ "FROM sections ";
	
	
	public MySQLSiteSearchGateway(String url, String searchfields) {
		
		this.sqlSelect = getSqlSelect(searchfields);
		this.url = url;
	}

	public List<SiteSearchResult> search(String term) throws SiteSearchException {
		
		List<SiteSearchResult> results = new ArrayList<>();
		
		try (Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = conn.prepareStatement(sqlSelect)) {
			
			ps.setString(1, term);	
			
			try (ResultSet rs = ps.executeQuery()) { 
			
				while (rs.next()) {
					
					Double score = rs.getDouble("score");
					String title = rs.getString("title");
					Boolean publish =  rs.getBoolean("publish");
					String content = rs.getString("content");
					String sectionSlug = rs.getString("sectionSlug");
					String topicSlug = rs.getString("topicSlug");
					String articleSlug = rs.getString("articleSlug");
					Date updatedAt = rs.getDate("updatedAt");
					
					if (publish == true) {
						
						String contentExcerpt = HelperFunctions.textExcerpt(content, term);
					
						SiteSearchResult result = SiteSearchResult.builder()
							.score(score)
							.title(title)
							.text(contentExcerpt)
							.section(sectionSlug)
							.topic(topicSlug)
							.slug(articleSlug)
							.updatedAt(updatedAt)
							.build();
						
						results.add(result);
					}
				}
			}	
			
			return results;
			
		} catch (SQLException e) {
			
			throw new SiteSearchException(e.getMessage());
		}
		
	}	
	
	
	public Map<String, String> getSections() throws SiteSearchException {
		
		Map <String, String> result = new HashMap<>();
		
		try (Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = conn.prepareStatement(sqlSections);
			ResultSet rs = ps.executeQuery()) {
			
			while (rs.next()) {
				result.put(rs.getString("slug"), rs.getString("name"));
			}
			
			return result;

		} catch (SQLException e) {
			
			throw new SiteSearchException(e.getMessage());
		}
			
	}
	

}
