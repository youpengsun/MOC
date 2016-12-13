package com.sap.moc.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CONTRACT")
public class Contract {
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="ID")
    private int id;
    
    @ManyToOne
    @JoinColumn(name="VENDOR_ID")
    private Vendor vendor;
    
    @Column(name="BEGIN_DATE")
    private Date beginDate;
    
    @Column(name="END_DATE")
    private Date endDate;
    
    @Column(name="STATUS",length=2)
    private String status;
    
    @Column(name="COMMENT",length=45)
    private String comment;
    
    @Column(name="CONTRACT_NO",length=45)
    private String contract_no;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Vendor getVendor() {
        return vendor;
    }
    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getContract_no() {
        return contract_no;
    }
    public void setContract_no(String contract_no) {
        this.contract_no = contract_no;
    }

}
