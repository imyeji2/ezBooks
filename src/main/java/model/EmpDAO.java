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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


/**
 *
 * @author yj
 */
public class EmpDAO {
    public static final int LOGIN_OK=1;  //로그인 성공
    public static final int PWD_DISAGREE=2; //비밀번호 불일치
    public static final int EMPNO_NONE=3;  //해당 사원 번호 없음
     
    
    
    //계약 담당자명 조회(입고 등록에서 사용)
    public EmpDTO selectByName(String conNo) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
         //1.2
         con = DbUtil.getConnection();
         
         //3
        
         String sql = "select name from emp " +
                       "where empNo = (select empNo from contract where conNo=?)";   
         ps = con.prepareStatement(sql);
         ps.setString(1, conNo);
         //4.
         //String name = "";
         rs = ps.executeQuery();
         EmpDTO dto = new EmpDTO();
         if(rs.next()){
             
            String name = rs.getString("name");
            dto.setEname(name);
            
            
         }
           System.out.println("검색 결과, 사원 이름="+dto.getEname());
        return dto;
    }
    
    
    
    //로그인 처리
    public int loginCheck(EmpDTO dto) throws SQLException {
        Connection con= null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            //1,2
            con = DbUtil.getConnection();
            
            //3
            String sql ="select pw,name from emp where empNo =? ";
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getEmpNo());
            
            //4
            rs = ps.executeQuery();
            
            int result =0;
            
            if(rs.next()){
                String pwd = rs.getString("pw");
                if(pwd.equals(dto.getPw())){//로그인 성공
                    result = LOGIN_OK;
                    //로그인 성공시 해당 사원 이름 저장
                    EmpService.setEmpName(rs.getString("name"));
                    
                }else{//비밀번호 불일치
                    result =PWD_DISAGREE;
                }
            }else{//사번 존재하지 않음
                result = EMPNO_NONE;
            }
             System.out.println("로그인 결과 result="+result+", 매개변수 사원번호="+dto.getEmpNo()+",pw="+dto.getPw());
            return result;
        }finally{
            //5
        } 
    }
    
//============================================예린=========================================================
    
   public List<EmpTableDTO> selectAll() throws SQLException {
        //사원 전체조회
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<EmpTableDTO> list = new ArrayList<>();

        try {
            con = DbUtil.getConnection();

            String sql = "select empno, name, hp, position, d.deptname "
                    + "from emp e left join dept d "
                    + "on e.deptno = d.deptno "
                    + "order by empno";
            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                String empno = rs.getString(1);
                String ename = rs.getString(2);
                String hp = rs.getString(3);
                String position = rs.getString(4);
                String deptname = rs.getString(5);

                EmpTableDTO dto = new EmpTableDTO(empno, ename, hp, position, deptname);

                list.add(dto);
            }
            System.out.println("사원 전체조회 결과, list.size() = " + list.size());

            return list;

        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }//

    public List<EmpTableDTO> selectName(String ename) throws SQLException {
        //사원 이름조회
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<EmpTableDTO> list = new ArrayList<>();

        try {
            con = DbUtil.getConnection();

            String sql = "select empno, name, hp, position, d.deptname "
                    + "from emp e left join dept d "
                    + "on e.deptno = d.deptno "
                    + "where name like '%' || ? || '%' "
                    + "order by empno ";
            ps = con.prepareStatement(sql);
            ps.setString(1, ename);

            rs = ps.executeQuery();

            while (rs.next()) {
                String empno = rs.getString(1);
                String ename2 = rs.getString(2);
                String hp = rs.getString(3);
                String position = rs.getString(4);
                String deptno = rs.getString(5);

                EmpTableDTO dto = new EmpTableDTO(empno, ename2, hp, position, deptno);
                list.add(dto);

            }
            System.out.println("사원 이름조회 결과, list.size() = " + list.size());

            return list;

        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }//

    public List<EmpTableDTO> selectNo(String empNo) throws SQLException {
        //사원 사원번호로 조회
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<EmpTableDTO> list = new ArrayList<>();

        try {
            con = DbUtil.getConnection();

            String sql = "select empno, name, hp, position, d.deptname "
                    + "from emp e left join dept d "
                    + "on e.deptno = d.deptno "
                    + "where empno like ? || '%' "
                    + "order by empno";
            ps = con.prepareStatement(sql);
            ps.setString(1, empNo);

            rs = ps.executeQuery();
            while (rs.next()) {
                String empno = rs.getString(1);
                String ename = rs.getString(2);
                String hp = rs.getString(3);
                String position = rs.getString(4);
                String deptname = rs.getString(5);

                EmpTableDTO dto = new EmpTableDTO(empno, ename, hp, position, deptname);

                list.add(dto);
            }
            System.out.println("회원번호로 조회 결과, list.size() = " + list.size() + ", 매개변수 empNo = " + empNo);
            return list;

        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }//

    public EmpDTO selectTableEmpNo(String empNo) throws SQLException {
        //테이블에서 선택했을 때 뿌리는 용도
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        EmpDTO dto = new EmpDTO();

        try {
            con = DbUtil.getConnection();

            String sql = "select empno, name, hp, position, d.deptno, pw "
                    + "from emp e left join dept d "
                    + "on e.deptno = d.deptno "
                    + "where empno = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, empNo);

            rs = ps.executeQuery();
            if (rs.next()) {
                String ename = rs.getString(2);
                String hp = rs.getString(3);
                String position = rs.getString(4);
                int deptno = rs.getInt(5);
                String pw = rs.getString(6);

                dto.setEmpNo(empNo);
                dto.setEname(ename);
                dto.setHp(hp);
                dto.setPosition(position);
                dto.setDeptNo(deptno);
                dto.setPw(pw);

            }
            System.out.println("회원번호로 조회 결과, dto = " + dto + ", 매개변수 empNo = " + empNo);
            return dto;

        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }//

    public String findDeptName(int deptno) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String deptname = "";
        try {
            con = DbUtil.getConnection();

            String sql = "select deptname from dept where deptno = ? ";
            ps = con.prepareStatement(sql);

            ps.setInt(1, deptno);

            rs = ps.executeQuery();

            if (rs.next()) {
                deptname = rs.getString(1);
            }

            System.out.println("부서명 " + deptname);
            return deptname;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }

    }

    public int insertEmp(EmpDTO dto) throws SQLException {
        //사원 등록
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DbUtil.getConnection();

            String sql = "insert into emp "
                    + "values (?, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql);

            ps.setString(1, dto.getEmpNo());
            ps.setString(2, dto.getPw());
            ps.setString(3, dto.getEname());
            ps.setString(4, dto.getPosition());
            ps.setInt(5, dto.getDeptNo());
            ps.setString(6, dto.getHp());

            int cnt = ps.executeUpdate();
            System.out.println("사원 등록 결과, cnt = " + cnt + ", 매개변수 dto = " + dto);

            return cnt;
        } finally {
            DbUtil.dbClose(ps, con);
        }
    }//

    public int modifyEmp(EmpDTO dto) throws SQLException {
        //사원 정보 수정
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DbUtil.getConnection();

            String sql = "update emp "
                    + "set name = ?, hp = ?, position = ?, deptno = ?, pw = ? "
                    + "where empno = ? ";
            ps = con.prepareStatement(sql);

            ps.setString(1, dto.getEname());
            ps.setString(2, dto.getHp());
            ps.setString(3, dto.getPosition());
            ps.setInt(4, dto.getDeptNo());
            ps.setString(5, dto.getPw());
            ps.setString(6, dto.getEmpNo());

            int cnt = ps.executeUpdate();
            System.out.println("사원 정보수정 결과, cnt = " + cnt + ", 매개변수 dto = " + dto);

            return cnt;
        } finally {
            DbUtil.dbClose(ps, con);
        }
    }//

    public int deleteEmp(String empno) throws SQLException {
        //사원정보 삭제
        Connection con = null;
        PreparedStatement ps = null;
        EmpDTO dto = new EmpDTO();

        try {
            con = DbUtil.getConnection();

            String sql = "delete from emp "
                    + "where empno = ? ";
            ps = con.prepareStatement(sql);

            ps.setString(1, empno);

            int cnt = ps.executeUpdate();
            System.out.println("사원정보 삭제 결과, cnt = " + cnt + ", 매개변수 empno = " + empno);

            return cnt;
        } finally {
            DbUtil.dbClose(ps, con);
        }
    }//

    public EmpDTO AddInNo(int deptno) throws SQLException {
        //사원번호 자동입력
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        EmpDTO dto = null;
        String inNo = "";

        try {
            con = DbUtil.getConnection();
            String sql = "select a.empno "
                    + "from (select empno from emp where deptno like ? || '%' order by empno desc) a "
                    + "where rownum = 1";
            
            ps = con.prepareStatement(sql);

            ps.setInt(1, deptno);
            //4.
            rs = ps.executeQuery();

            if (rs.next()) {
                DecimalFormat df = new DecimalFormat("0000");

                String result = rs.getString(1);
                String str = result.substring(0, 3);
                int num = Integer.parseInt(result.substring(4));
                
                System.out.println("num = " + num + "result = " + result + "str = "  + str);
                
                if (Integer.toString(deptno).equals(str)) {
                    num = num + 1;

                    System.out.println("num + 1 = " + num);
                    inNo = deptno + "-" + df.format(num);
                } else {
                    inNo = deptno + "-" + "0001";
                }
                System.out.println("inNo = " + inNo);
                dto = new EmpDTO(inNo);
            } else {
                inNo = deptno + "-" + "0001";
                dto = new EmpDTO(inNo);
            }
            System.out.println("사원번호 자동등록 dto = " + dto);
            return dto;
        } finally {
            //5.
            DbUtil.dbClose(rs, ps, con);
        }

    }//

    public Vector<String> getDeptName() throws SQLException {
        //사원탭의 부서콤보박스에 정보 받아오기
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Vector<String> vec = new Vector<>();

        try {
            con = DbUtil.getConnection();

            String sql = "select Deptname from dept order by deptno ";
            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);
                vec.add(name);
            }
            System.out.println("부서이름 가져오기 결과, vector = " + vec.size());

            return vec;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }//
  
}
