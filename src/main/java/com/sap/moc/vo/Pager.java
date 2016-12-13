package com.sap.moc.vo;

import java.util.List;

public class Pager<E> {
	private List<E> entryList;
	
	private int currentPage;
	
	private int pageLine;
	
	private int pageCount;
	
	private int totalCount;

    
	public Pager( int pageline, int pagecount){
		//set page size, default 20 
		this.setPageLine(pageline == 0 ? 20 : pageline);
		this.setPageCount(pageCount);
	}
	
	public Pager() {
		
	}

	/**
	 * @return the entryList
	 */
	public List<E> getEntryList() {
		return entryList;
	}

	/**
	 * @param entryList the entryList to set
	 */
	public void setEntryList(List<E> entryList) {
		this.entryList = entryList;
	}

	/**
	 * @return the currentPage
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * @param currentPage the currentPage to set
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * @return the pageLine
	 */
	public int getPageLine() {
		return pageLine;
	}

	/**
	 * @param pageLine the pageLine to set
	 */
	public void setPageLine(int pageLine) {
		this.pageLine = pageLine;
	}

	/**
	 * @return the pageCount
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * @param pageCount the pageCount to set
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
