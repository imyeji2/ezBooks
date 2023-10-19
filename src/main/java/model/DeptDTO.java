/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;


public class DeptDTO {

    private int deptNo;
    private String deptName;
    private String manager;

    public DeptDTO() {
        super();
    }
    
        public DeptDTO(int deptNo) {
        super();
        this.deptNo = deptNo;
    }

    public DeptDTO(int deptNo, String deptName, String manager) {
        super();
        this.deptNo = deptNo;
        this.deptName = deptName;
        this.manager = manager;
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

    @Override
    public String toString() {
        return "DeptDTO{" + "deptNo=" + deptNo + ", deptName=" + deptName + ", manager=" + manager + '}';
    }
    
}
