/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author yj
 */
public class EmpDTO {
    private String empNo;
    private String pw;
    private String ename;
    private String position;
    private int deptNo;
    private String hp;
    
    
   public EmpDTO(){
       super();
   }

       
    public EmpDTO(String empNo, String pw) {
        this.empNo = empNo;
        this.pw = pw;
    }   
    
    public EmpDTO(String hp, String position, int deptNo, String pw) {
        this.hp = hp;
        this.position = position;
        this.deptNo = deptNo;
        this.pw = pw;
    }
    

    
    public EmpDTO(String empNo, String pw, String ename, String position, int deptNo, String hp) {
    this.empNo = empNo;
    this.pw = pw;
    this.ename = ename;
    this.position = position;
    this.deptNo = deptNo;
    this.hp = hp;
    }

   public EmpDTO(String empNo) {
        this.empNo = empNo;
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

    public int getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(int deptNo) {
        this.deptNo = deptNo;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    @Override
    public String toString() {
        return "EmpDTO{" + "empNo=" + empNo + ", pw=" + pw + ", ename=" + ename + ", position=" + position + ", deptNo=" + deptNo + ", hp=" + hp + '}';
    }


}
