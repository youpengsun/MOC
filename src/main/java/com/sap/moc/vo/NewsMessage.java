package com.sap.moc.vo;

import java.util.ArrayList;
import java.util.List;

public class NewsMessage extends BaseMessage{
    private NewsContent news;

    public NewsContent getNews() {
        return news;
    }

    public void setNews(NewsContent news) {
        this.news = news;
    }
    
    public NewsMessage(String toUser, String title, String description, String agentId) {
    	NewsContent newscnt = new NewsContent();
    	Article art = new Article();
    	art.setTitle(title);
    	art.setDescription(description);
    	
    	List<Article> articles = new ArrayList<Article>();
    	articles.add(art);
    	
    	newscnt.setArticles(articles);
    	
    	this.setNews(newscnt);
    	this.setTouser(toUser);
    	this.setMsgtype("news");
    	this.setAgentid(agentId);
    }
}
