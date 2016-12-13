package com.sap.moc.vo;

import java.util.ArrayList;
import java.util.List;

// passive news message

public class PasNewsMessage {
	
	private String ToUserName;
	private String FromUserName;
    private String CreateTime;
    private String MsgType;
    private String ArticleCount;
    private List<PasArticle> Articles;
    
    
    public PasNewsMessage(String toUserName, String fromUserName, String title, String description){
    	PasArticle item = new PasArticle();
    	item.setTitle(title);
    	item.setDescription(description);
    	List<PasArticle> items = new ArrayList<PasArticle>();
    	items.add(item);
    	this.setArticles(items);
    	this.setCreateTime(System.currentTimeMillis()+"");
    	this.setToUserName(toUserName);
    	this.setFromUserName(fromUserName);
    	this.setArticleCount("1");
    	this.setMsgType("news"); //News message
    }
    
    public PasNewsMessage(String toUserName, String fromUserName, String title, String description,String url){
    	PasArticle item = new PasArticle();
    	item.setTitle(title);
    	item.setDescription(description);
    	item.setUrl(url);
    	List<PasArticle> items = new ArrayList<PasArticle>();
    	items.add(item);
    	this.setArticles(items);
    	this.setCreateTime(System.currentTimeMillis()+"");
    	this.setToUserName(toUserName);
    	this.setFromUserName(fromUserName);
    	this.setArticleCount("1");
    	this.setMsgType("news"); //News message
    }    
    /**
	 * @return the articles
	 */


    /**
	 * @return the toUserName
	 */
	public String getToUserName() {
		return ToUserName;
	}
	/**
	 * @return the articles
	 */
	public List<PasArticle> getArticles() {
		return Articles;
	}


	/**
	 * @param articles the articles to set
	 */
	public void setArticles(List<PasArticle> articles) {
		Articles = articles;
	}


	/**
	 * @param toUserName the toUserName to set
	 */
	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}
	/**
	 * @return the fromUserName
	 */
	public String getFromUserName() {
		return FromUserName;
	}
	/**
	 * @param fromUserName the fromUserName to set
	 */
	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}
	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return CreateTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	/**
	 * @return the msgType
	 */
	public String getMsgType() {
		return MsgType;
	}
	/**
	 * @param msgType the msgType to set
	 */
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	/**
	 * @return the articleCount
	 */
	public String getArticleCount() {
		return ArticleCount;
	}
	/**
	 * @param articleCount the articleCount to set
	 */
	public void setArticleCount(String articleCount) {
		ArticleCount = articleCount;
	}



    
}
