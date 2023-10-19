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
public class RefundDTO {
	private String reno;
	private String sellno;
	private int price;
	private Timestamp redate;
	private String restate;
	
	
	public RefundDTO() {
		super();
	}
	
	public RefundDTO(String reno, String sellno, int price, Timestamp redate, String restate) {
		super();
		this.reno = reno;
		this.sellno = sellno;
		this.price = price;
		this.redate = redate;
		this.restate = restate;
	}
        public RefundDTO(String reno){
            this.reno=reno;
        }

	public String getReno() {
		return reno;
	}
	public void setReno(String reno) {
		this.reno = reno;
	}
	public String getSellno() {
		return sellno;
	}
	public void setSellno(String sellno) {
		this.sellno = sellno;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public Timestamp getRedate() {
		return redate;
	}
	public void setRedate(Timestamp redate) {
		this.redate = redate;
	}
	public String getRestate() {
		return restate;
	}
	public void setRestate(String restate) {
		this.restate = restate;
	}

	@Override
	public String toString() {
		return "RefundDTO [reno=" + reno + ", sellno=" + sellno + ", price=" + price + ", redate=" + redate
				+ ", restate=" + restate + "]";
	}
	    
}
