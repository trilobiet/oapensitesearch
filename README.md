		
# Site search for OAPEN web sites based on strapi / graphqlweb		
		
		
    <dependency>
        <groupId>com.trilobiet.oapen</groupId>
        <artifactId>sitesearch</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    
## Before you can run a full text search on mongodb you must create an index:

	db.article.createIndex({"field_1":"text", ... , "field_n":"text"})
	
For example:
	
    db.article.createIndex({"title":"text","content":"text","summary":"text","tags":"text","...":"text"})
    
     