/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author yj
 */
public class EmpTableDTO {
    private String empNo;
    private String ename;
    private String hp;
    private String position;
    private String deptName;

    public EmpTableDTO(String empNo, String ename, String hp, String position, String deptName) {
        this.empNo = empNo;
        this.ename = ename;
        this.hp = hp;
        this.position = position;
        this.deptName = deptName;
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override
    public String toString() {
        return "EmpTableDTO{" + "empNo=" + empNo + ", ename=" + ename + ", hp=" + hp + ", position=" + position + ", deptName=" + deptName + '}';
    }
    
    
}
