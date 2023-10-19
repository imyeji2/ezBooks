/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author yj
 */
public class SaleDTO {
 	private String sellNo;
	private String id;
	private int bookNo;
	private String subject;
	private int price;
	private String sellState;
	private Timestamp sellDate;
	
        
	public SaleDTO(String sellNo, String id, int bookNo, String subject, int price, String sellState,
			Timestamp sellDate) {
		super();
		this.sellNo = sellNo;
		this.id = id;
		this.bookNo = bookNo;
		this.subject = subject;
		this.price = price;
		this.sellState = sellState;
		this.sellDate = sellDate;
	}
        public SaleDTO(String sellNo){
            this.sellNo=sellNo;
        }
	
	public SaleDTO() {
		super();
	}
	public String getSellNo() {
		return sellNo;
	}
	public void setSellNo(String sellNo) {
		this.sellNo = sellNo;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public int getBookNo() {
		return bookNo;
	}

	public void setBookNo(int bookNo) {
		this.bookNo = bookNo;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getSellState() {
		return sellState;
	}

	public void setSellState(String sellState) {
		this.sellState = sellState;
	}

	public Timestamp getSellDate() {
		return sellDate;
	}

	public void setSellDate(Timestamp sellDate) {
		this.sellDate = sellDate;
	}

	@Override
	public String toString() {
		return "SaleDTO [sellNo=" + sellNo + ", id=" + id + ", bookNo=" + bookNo + ", subject=" + subject + ", price="
				+ price + ", sellState=" + sellState + ", sellDate=" + sellDate + "]";
	}   
}
