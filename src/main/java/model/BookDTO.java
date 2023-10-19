/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 *
 * @author yj
 */
public class BookDTO {
    private int bookNo;
    private int caNo;
    private String writer;
    private String subject;
    private int price;
    private Date regdate;
    private String conNo;
    private String puId;
    private int state;

    public BookDTO() {
    }

    public BookDTO(int bookNo, int caNo, String writer, String subject, int price, Date regdate, String conNo, String puId) {
        this.bookNo = bookNo;
        this.caNo = caNo;
        this.writer = writer;
        this.subject = subject;
        this.price = price;
        this.regdate = regdate;
        this.conNo = conNo;
        this.puId = puId;
    }

    public int getBookNo() {
        return bookNo;
    }

    public void setBookNo(int bookNo) {
        this.bookNo = bookNo;
    }

    public int getCaNo() {
        return caNo;
    }

    public void setCaNo(int caNo) {
        this.caNo = caNo;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
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

    public Date getRegdate() {
        return regdate;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }

    public String getConNo() {
        return conNo;
    }

    public void setConNo(String conNo) {
        this.conNo = conNo;
    }

    public String getPuId() {
        return puId;
    }

    public void setPuId(String puId) {
        this.puId = puId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "BookDTO{" + "bookNo=" + bookNo + ", caNo=" + caNo + ", writer=" + writer + ", subject=" + subject + ", price=" + price + ", regdate=" + regdate + ", conNo=" + conNo + ", puId=" + puId + ", state=" + state + '}';
    }   
}
