/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import db.DbUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeptDAO {
    public static final int USABLE_MANAGER = 1; //사원번호 존재
    public static final int UNUSABLE_MANAGER = 2; //사원번호 존재하지 않음
    
    
    public List<DeptTableDTO> selectAll() throws SQLException {
        //모든 부서 조회
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<DeptTableDTO> list = new ArrayList<>();
        
        try {
            con = DbUtil.getConnection();
            
            String sql = " select deptno, deptname, manager, " +
                    "(select count(*) from emp e where e.deptno = d.deptno) " +
                    "from dept d order by deptno ";
            ps = con.prepareStatement(sql);
            
            rs = ps.executeQuery();
            while(rs.next()) {
                int deptno = rs.getInt(1);
                String deptname = rs.getString(2);
                String manager = rs.getString(3);
                int total = rs.getInt(4);
                
                DeptTableDTO dto = new DeptTableDTO(deptno, deptname, manager, total);
                
                list.add(dto);
            }
            System.out.println("모든 부서 조회, list.size() = " + list.size());
            
            return list;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }//
    
    public List<DeptTableDTO> selectDeptNO(int deptno) throws SQLException {
        //부서번호로 조회
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<DeptTableDTO> list = new ArrayList<>();
        
        try {
            con = DbUtil.getConnection();
            
            String sql = " select deptno, deptname, manager, " +
                    "(select count(*) from emp e where e.deptno = d.deptno) " +
                    "from dept d where deptno = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, deptno);
            
            rs = ps.executeQuery();
            if(rs.next()) {
                int deptno2 = rs.getInt(1);
                String deptname = rs.getString(2);
                String manager = rs.getString(3);
                int total = rs.getInt(4);
                
                DeptTableDTO dto = new DeptTableDTO(deptno2, deptname, manager, total);
                list.add(dto);
            }
            System.out.println("부서번호로 조회, list.size() = " + list.size() + ", 매개변수 deptno = " + deptno);
            return list;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }//
    
    public List<DeptTableDTO> selectDeptName(String deptname) throws SQLException {
        //부서명으로 조회
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<DeptTableDTO> list = new ArrayList<>();
        
        try {
            con = DbUtil.getConnection();
            
            String sql = "select deptno, deptname, manager, " +
                    "(select count(*) from emp e where e.deptno = d.deptno) " +
                    "from dept d where deptname like '%' || ? || '%' ";
            ps = con.prepareStatement(sql);
            ps.setString(1, deptname);
            
            rs = ps.executeQuery();
            while(rs.next()) {
                int deptno = rs.getInt(1);
                String deptname2 = rs.getString(2);
                String manager = rs.getString(3);
                int total = rs.getInt(4);
                
                DeptTableDTO dto = new DeptTableDTO(deptno, deptname2, manager, total);
                list.add(dto);
            }
            System.out.println("부서명으로 조회, list.size() : " + list.size() + ", 매개변수 deptname : " + deptname);
            return list;
            
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }//
    
    public DeptDTO showTableDept(int deptno) throws SQLException {
        //테이블 선택하고 오른쪽에 뿌리기
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        DeptDTO dto = new DeptDTO();

        try {
            con = DbUtil.getConnection();

            String sql = "select deptno, deptname, manager from dept where deptno = ? ";
            ps = con.prepareStatement(sql);

            ps.setInt(1, deptno);

            rs = ps.executeQuery();
            if (rs.next()) {
                int deptno2 = rs.getInt(1);
                String deptname = rs.getString(2);
                String manager = rs.getString(3);

                dto.setDeptNo(deptno2);
                dto.setDeptName(deptname);
                dto.setManager(manager);
            }
            System.out.println("부서번호로 조회, dto = " + dto + ", 매개변수 deptno = " + deptno);
            return dto;

        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }//
    
    public int insertDept(DeptDTO dto) throws SQLException {
        //부서 등록
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = DbUtil.getConnection();
            
            String sql = "insert into dept(deptno, deptname) "
                    + "values (deptno_seq.nextval, ?)";
            ps = con.prepareStatement(sql);
            
            ps.setString(1, dto.getDeptName());
            
            int cnt = ps.executeUpdate();
            
            System.out.println("부서 등록 결과, cnt = " + cnt + ", 매개변수 dto = " + dto);
            return cnt;
            
        } finally {
            DbUtil.dbClose(ps, con);
        }
    }//
    
    public int modifyDept(DeptDTO dto) throws SQLException {
        //부서 정보 수정
        Connection con = null;
        PreparedStatement ps = null; 
        
        try {
            con = DbUtil.getConnection();
            
            String sql = "update dept "
                    + "set deptname = ?, manager = ? "
                    + "where deptno = ? ";
            ps = con.prepareStatement(sql);
            
            ps.setString(1, dto.getDeptName());
            ps.setString(2, dto.getManager());
            ps.setInt(3, dto.getDeptNo());
            
            int cnt = ps.executeUpdate();
            
            System.out.println("부서정보 수정 결과, cnt = " + cnt + ", 매개변수 dto = " + dto);
            return cnt;
            
        } finally {
            DbUtil.dbClose(ps, con);
        }
    }//
    
    public int deleteDept(int deptno) throws SQLException {
        //부서정보 삭제
        Connection con = null;
        PreparedStatement ps = null;
        DeptDTO dto = new DeptDTO();
        
        try {
            con = DbUtil.getConnection();
            
            String sql = "delete from dept " +
                    "where deptno = ? ";
            ps = con.prepareStatement(sql);
            
            ps.setInt(1, deptno);
            
            int cnt = ps.executeUpdate();
            
            System.out.println("부서정보 삭제 결과, cnt = " + cnt + ", 매개변수 deptno = " + deptno);
            return cnt;
            
        } finally {
            DbUtil.dbClose(ps, con);
        }
    }//
    
    public int findManager(String empname) throws SQLException {
        //부서 등록할 때 매니저 확인하기
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        int result = 0;
        try {
            con = DbUtil.getConnection();
            
            String sql = "select count(*) from emp where name = ? ";
            ps = con.prepareStatement(sql);
            
            ps.setString(1, empname);
            
            rs = ps.executeQuery();
            if(rs.next()) {
                int count = rs.getInt(1);
                
                if (count > 0) {
                    result = USABLE_MANAGER;
                } else {
                    result = UNUSABLE_MANAGER;
                }
            }
            System.out.println("매니저 이름 체크 = " + result);
            
            return result;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }//

    public DeptDTO serchDeptNO(String deptName) throws SQLException {
             
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        DeptDTO Dto = null;

        try {
            con = DbUtil.getConnection();
            String sql = "select deptno from dept where deptName = ? ";
            ps = con.prepareStatement(sql);
            ps.setString(1, deptName);
            
            rs = ps.executeQuery();
            
            if(rs.next()) {
                int deptNo = rs.getInt("deptno");
                Dto = new DeptDTO(deptNo);
            }
            System.out.println("부서번호 = " + Dto);
            
            return Dto;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }


    
}
