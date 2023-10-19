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
public class StockTableViewDTO {
    private int stockNo;
    private String subject;
    private String puName;
    private int qty;
    private Timestamp inDate;

    public StockTableViewDTO(int stockNo, String subject, String puName, int qty, Timestamp inDate) {
        this.stockNo = stockNo;
        this.subject = subject;
        this.puName = puName;
        this.qty = qty;
        this.inDate = inDate;
    }

    public int getStockNo() {
        return stockNo;
    }

    public void setStockNo(int stockNo) {
        this.stockNo = stockNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPuName() {
        return puName;
    }

    public void setPuName(String puName) {
        this.puName = puName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Timestamp getInDate() {
        return inDate;
    }

    public void setInDate(Timestamp inDate) {
        this.inDate = inDate;
    }

    @Override
    public String toString() {
        return "StockTableViewDTO{" + "stockNo=" + stockNo + ", subject=" + subject + ", puName=" + puName + ", qty=" + qty + ", inDate=" + inDate + '}';
    }
    
}
