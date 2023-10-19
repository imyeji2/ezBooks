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
public class Statistics3DTO {
    private String caName;
    private int qty;

    public Statistics3DTO(String caName, int qty) {
        this.caName = caName;
        this.qty = qty;
    }

    public String getCaName() {
        return caName;
    }

    public void setCaName(String caName) {
        this.caName = caName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "Statistics3DTO{" + "caName=" + caName + ", qty=" + qty + '}';
    }
    
    
    
}
