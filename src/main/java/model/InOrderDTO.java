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
public class InOrderDTO {
    private String inNo;
    private Timestamp inDate;
    private String conNo;
    private int qty;
    private int cost;
    
    public InOrderDTO(String inNo, int qty, int cost) {
        this.inNo = inNo;
        this.qty = qty;
        this.cost = cost;
    }
        
    public InOrderDTO(String inNo, String conNo, int qty, int cost) {
        this.inNo = inNo;
        this.conNo = conNo;
        this.qty = qty;
        this.cost = cost;
    }
    
    public InOrderDTO(String inNo, Timestamp inData, String conNo, int qty, int cost) {
        this.inNo = inNo;
        this.inDate = inData;
        this.conNo = conNo;
        this.qty = qty;
        this.cost = cost;
    }
    
       public InOrderDTO(String inNo) {
        this.inNo = inNo;
    }

    public String getInNo() {
        return inNo;
    }

    public void setInNo(String inNo) {
        this.inNo = inNo;
    }

    public Timestamp getInDate() {
        return inDate;
    }

    public void setInDate(Timestamp inData) {
        this.inDate = inData;
    }

    public String getConNo() {
        return conNo;
    }

    public void setConNo(String conNo) {
        this.conNo = conNo;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
    
    

    @Override
    public String toString() {
        return "InOrderDTO{" + "inNo=" + inNo + ", inDate=" + inDate + ", conNo=" + conNo + ", qty=" + qty + ", cost=" + cost + '}';
    }

    
    
}
