/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author yj
 */
public class CategoryDTO {
     private int caNo;
    private String caName;
    private int state;

    public CategoryDTO() {
    }

    public CategoryDTO(int caNo, String caName) {
        this.caNo = caNo;
        this.caName = caName;
    }

    public int getCaNo() {
        return caNo;
    }

    public void setCaNo(int caNo) {
        this.caNo = caNo;
    }

    public String getCaName() {
        return caName;
    }

    public void setCaName(String caName) {
        this.caName = caName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "CategoryDTO{" + "caNo=" + caNo + ", caName=" + caName + ", state=" + state + '}';
    }   
}
