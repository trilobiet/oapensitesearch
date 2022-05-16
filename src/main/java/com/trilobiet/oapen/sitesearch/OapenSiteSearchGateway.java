package com.trilobiet.oapen.sitesearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

class OapenSiteSearchGateway implements SiteSearchGateway {
	
	public final String URL;
	public final String DATABASE = "strapi";
	public final String ARTICLES = "article";
	
	private final Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
	{
		mongoLogger.setLevel(Level.SEVERE);
	}

	public OapenSiteSearchGateway(String url) {
		this.URL = url;
	}
	
	
	public Map<String, String> getSections() {
		
		Map<String,String> map = new HashMap<>();
		
		try (var mongoClient = MongoClients.create(URL)) {
			
			MongoDatabase database = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> sections = database.getCollection("section");
            
            try ( MongoCursor<Document> cur = sections.find().iterator() ) {
            	
            	while (cur.hasNext()) {
            		
            		Document doc = cur.next();
            		//System.out.println(doc.get("_id") + " -> " + doc.getString("slug"));
            		map.put(doc.get("_id").toString(), doc.getString("slug"));
            		
            	}
            }
            
		}
		
		return map;
	}
	

	@Override
	public List<Article> search(String term) {
		
		List<Article> results = new ArrayList<>();
		
        try (var mongoClient = MongoClients.create(URL)) {

            MongoDatabase database = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> articles = database.getCollection(ARTICLES);
            
            try ( MongoCursor<Document> cur = findInArticles(term, articles) ) {
            	
            	ObjectReader reader = getConfiguredObjectReader();
            	
                while (cur.hasNext()) {
                	
                    Document doc = cur.next();
                    //System.out.println(doc);
                    
                    String json = doc.toJson(JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build());
                    try {
                    	Article result = reader.readValue(json, Article.class);
						results.add(result);
					} catch (IOException e) {
						e.printStackTrace();
					}
                }
            }
        }		
		
        return results;
		
	}
	
	private ObjectReader getConfiguredObjectReader() {

		return new ObjectMapper().reader()
			.forType(Article.class)
			.without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}	

	
	private MongoCursor<Document> findInArticles(String term, MongoCollection<Document> articles) {

		Document qtext = new Document("$text",
			new Document("$search", term)
			.append("$caseSensitive", false)
			.append("$diacriticSensitive", false)
			.append("$language", "en")
		);
		Document qscore = new Document("score", new Document("$meta", "textScore"));

		// return documents.find(qtext).projection(qscore).iterator();

		// Pipeline search:
		// ============================================================================
		// 1. actual search
		Document match = new Document("$match", qtext);
		// 2. add value for 'score'
		Document addFlds = new Document("$addFields", qscore);
		// 3. join with topic(s)
		Document lookup = new Document("$lookup", 
				new Document("from", "topic")
				.append("localField", "topics")
				.append("foreignField", "_id")
				.append("as", "topics")
		);
		
		// 4. sort result by score
		Document sort = new Document("$sort", qscore);

		List<Document> stages = new ArrayList<>();
		stages.add(match);
		stages.add(addFlds);
		stages.add(lookup);
		stages.add(sort);

		return articles.aggregate(stages).iterator();
	}

	
	@Override
	public String createIndex(List<String> fields) throws SiteSearchException {
		
		String name = "";
		
		try (var mongoClient = MongoClients.create(URL)) {
		
			MongoDatabase database = mongoClient.getDatabase(DATABASE);
			
			Document doc = new Document();
			fields.forEach(f -> doc.append(f, "text"));
			
			MongoCollection<Document> col = database.getCollection(ARTICLES);
			
			name = col.createIndex(doc);
		}
		catch (MongoCommandException e) {
			
			throw new SiteSearchException(e.getResponse().toJson());
		}
		
		return name;
	}
	

}
