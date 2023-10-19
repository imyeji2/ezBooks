/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author yj
 */
public class SaleAddDTO {
    private String sellNo, subject, id;
    private int price, qty, bookNo;

    public SaleAddDTO(String sellNo, String id) {
        this.sellNo = sellNo;
        this.id = id;
    }
    
    
    public SaleAddDTO(int bookNo, int qty, String sellNo) {
        this.bookNo = bookNo;
        this.qty = qty;
        this.sellNo = sellNo;
    }

    public SaleAddDTO(int bookno, int price) {
        this.bookNo = bookno;
        this.price = price;
    }

    public SaleAddDTO() {
        super();
    }

    public SaleAddDTO(String sellNo) {
        this.sellNo = sellNo;
    }

    public String getSellNo() {
        return sellNo;
    }

    public void setSellNo(String sellNo) {
        this.sellNo = sellNo;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "SaleAddDTO{" + "sellNo=" + sellNo + ", id=" + id + ", bookNo=" + bookNo + ", subject=" + subject + ", price=" + price + ", qty=" + qty + '}';
    }

}
