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
public class Statistics1DTO {
    private String sellDate;
    private int price;
    private int qty;

    public Statistics1DTO(String sellDate, int price, int qty) {
        this.sellDate = sellDate;
        this.price = price;
        this.qty = qty;
    }

    public String getSellDate() {
        return sellDate;
    }

    public void setSellDate(String sellDate) {
        this.sellDate = sellDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "Statistics1DTO{" + "sellDate=" + sellDate + ", price=" + price + ", qty=" + qty + '}';
    }

   
}
