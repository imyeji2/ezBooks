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
public class InOrderTableDTO {
    private String inNo;
    private String conNo;
    private int qty;
    private int cost;
    private String empName;
    private Timestamp inDate;

    public InOrderTableDTO(String inNo, String conNo, int qty, int cost, String empName, Timestamp inDate) {
        this.inNo = inNo;
        this.conNo = conNo;
        this.qty = qty;
        this.cost = cost;
        this.empName = empName;
        this.inDate = inDate;
    }

    public String getInNo() {
        return inNo;
    }

    public void setInNo(String inNo) {
        this.inNo = inNo;
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

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public Timestamp getInDate() {
        return inDate;
    }

    public void setInDate(Timestamp inDate) {
        this.inDate = inDate;
    }


    @Override
    public String toString() {
        return "InOrderTableDTO{" + "inNo=" + inNo + ", conNo=" + conNo + ", qty=" + qty + ", cost=" + cost + ", empName=" + empName + ", inDate=" + inDate + '}';
    }

    
}

   
    
