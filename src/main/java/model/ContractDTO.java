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
public class ContractDTO {
      private String conNo;
    private Timestamp conDate;
    private String empNo;
    private String puId;

    public ContractDTO() {
    }


    
    public ContractDTO(String conNo, Timestamp conDate, String empNo, String puId) {
        this.conNo = conNo;
        this.conDate = conDate;
        this.empNo = empNo;
        this.puId = puId;
    }

    public String getConNo() {
        return conNo;
    }

    public void setConNo(String conNo) {
        this.conNo = conNo;
    }

    public Timestamp getConDate() {
        return conDate;
    }

    public void setConDate(Timestamp conDate) {
        this.conDate = conDate;
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getPuId() {
        return puId;
    }

    public void setPuId(String puId) {
        this.puId = puId;
    }

    @Override
    public String toString() {
        return "ContractDTO{" + "conNo=" + conNo + ", conDate=" + conDate + ", empNo=" + empNo + ", puId=" + puId + '}';
    }
}
