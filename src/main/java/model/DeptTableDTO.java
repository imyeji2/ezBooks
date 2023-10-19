/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author yj
 */
public class DeptTableDTO {

    private int deptNo;
    private String deptName;
    private String manager;
    private int total;

    public DeptTableDTO(int deptNo, String deptName, String manager, int total) {
        this.deptNo = deptNo;
        this.deptName = deptName;
        this.manager = manager;
        this.total = total;
    }

    public int getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(int deptNo) {
        this.deptNo = deptNo;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "DeptDTO2{" + "deptNo=" + deptNo + ", deptName=" + deptName + ", manager=" + manager + ", total=" + total + '}';
    }

}
