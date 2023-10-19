/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author yj
 */
public class StockDTO {
    private int stockNo;
    private int qty;
    private String conNo;
    
    public StockDTO(int qty, String conNo) {
        this.qty = qty;
        this.conNo = conNo;
    }
    
    public StockDTO(int stockNo, int qty, String conNo) {
        this.stockNo = stockNo;
        this.qty = qty;
        this.conNo = conNo;
    }

    public int getStockNo() {
        return stockNo;
    }

    public void setStockNo(int stockNo) {
        this.stockNo = stockNo;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getConNo() {
        return conNo;
    }

    public void setConNo(String conNo) {
        this.conNo = conNo;
    }

    @Override
    public String toString() {
        return "StockDTO{" + "stockNo=" + stockNo + ", qty=" + qty + ", conNo=" + conNo + '}';
    }
    
}
