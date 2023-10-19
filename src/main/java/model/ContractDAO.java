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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author yj
 */
public class ContractDAO {

    //계약조회(입고등록에서 사용)
    public int selectContract(String conNo) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
       
        try{
            //1&2
            con = DbUtil.getConnection();
            
            //3
            String sql ="select count(*)" +
                        " from contract c " +
                        " where conNo=?";
            
            ps = con.prepareStatement(sql);
            ps.setString(1, conNo);
            
            //4
            rs = ps.executeQuery();
            int result = 0;
            if(rs.next()){
                int count = rs.getInt(1);
                if(count>0){
                    result = 1;
                }else{
                    result =0;
                }
            } 
            
            System.out.println("매개변수="+conNo+", 검색결과 "+result);
            return result;
        }finally{
            DbUtil.dbClose(rs, ps, con);
        
        }
    }
        
//===============================민기========================================================================
     //계약 전체조회
    public List<Map> contractShowAll(String contractOrderBy) throws SQLException {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;

        List<Map> list = new ArrayList<>();

        try {
            con = DbUtil.getConnection();

            String sql = "select c.conNo, p.puName, p.puId, e.name, d.deptName, c.condate"
                    + " from contract c join publisher p"
                    + " on c.puId = p.puId"
                    + " join emp e"
                    + " on c.empNo = e.empNo"
                    + " join dept d"
                    + " on e.deptNo = d.deptNo"
                    + " and c.state = 1"
                    + " order by c.conDate " + contractOrderBy;
            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();

                String conNo = rs.getString(1);
                String puName = rs.getString(2);
                String puId = rs.getString(3);
                String name = rs.getString(4);
                String deptName = rs.getString(5);
                Date conDate = rs.getDate(6);

                map.put("conNo", conNo);
                map.put("puName", puName);
                map.put("puId", puId);
                map.put("name", name);
                map.put("deptName", deptName);
                map.put("conDate", conDate);

                list.add(map);
            }

            return list;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }
    
    //계약검색
    public List<Map> contractSelect(String conSel, String userInput) throws SQLException {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        
        List<Map> list = new ArrayList<>();
        
        try {
            con = DbUtil.getConnection();
            
            String sql = "select c.conNo, p.puName, p.puId, e.name, d.deptName, c.condate"
                    + " from contract c join publisher p"
                    + " on c.puId = p.puId"
                    + " join emp e"
                    + " on c.empNo = e.empNo"
                    + " join dept d"
                    + " on e.deptNo = d.deptNo"
                    + " and not c.state = 0"
                    + " and " + conSel + " like ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, userInput);
            
            rs = ps.executeQuery();
            while(rs.next()) {
                Map<String, Object> map = new HashMap<>();

                String conNo = rs.getString(1);
                String puName = rs.getString(2);
                String puId = rs.getString(3);
                String name = rs.getString(4);
                String deptName = rs.getString(5);
                Date conDate = rs.getDate(6);

                map.put("conNo", conNo);
                map.put("puName", puName);
                map.put("puId", puId);
                map.put("name", name);
                map.put("deptName", deptName);
                map.put("conDate", conDate);

                list.add(map);
            }
            
            return list;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }

    //계약추가
    public int contractAdd(ContractDTO dto) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DbUtil.getConnection();

            String sql = "insert into contract(conno, condate, empno, puId)"
                    + " values(?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getConNo());
            ps.setTimestamp(2, dto.getConDate());
            ps.setString(3, dto.getEmpNo());
            ps.setString(4, dto.getPuId());

            int cnt = ps.executeUpdate();

            return cnt;
        } finally {
            DbUtil.dbClose(ps, con);
        }
    }

    //계약수정
    public int contractEdit(ContractDTO dto) throws SQLException {
        PreparedStatement ps = null;
        Connection con = null;

        try {
            con = DbUtil.getConnection();

            String sql = "update contract"
                    + " set empNo = ?, puId = ?"
                    + " where conNo = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getEmpNo());
            ps.setString(2, dto.getPuId());
            ps.setString(3, dto.getConNo());

            int cnt = ps.executeUpdate();

            return cnt;
        } finally {
            DbUtil.dbClose(ps, con);
        }
    }

    //계약삭제
    public int contractDelete(String conNo) throws SQLException {
        PreparedStatement ps = null;
        Connection con = null;

        try {
            con = DbUtil.getConnection();

            String sql = "update contract"
                    + " set state = 0"
                    + " where conNo = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, conNo);
            
            int cnt = ps.executeUpdate();
            
            return cnt;
        } finally {
            DbUtil.dbClose(ps, con);
        }
    }

    //계약번호 구하기
    public String conNoAdd() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ContractDTO dto = null;

        try {
            //1.2
            con = DbUtil.getConnection();
            String sql = "select A.conNo"
                    + " from (select conNo"
                    + " from contract"
                    + " order by condate desc)A";
            ps = con.prepareStatement(sql);
            String conNo = "";

            //4.
            rs = ps.executeQuery();

            if (rs.next()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                DecimalFormat df = new DecimalFormat("0000");

                java.util.Date date = new java.util.Date();
                String sysdate = sdf.format(date);

                String result = rs.getString(1);
                String strDate = result.substring(3, 13);
                int num = Integer.parseInt(result.substring(14));

                if (sysdate.equals(strDate)) {
                    num = num + 2;

                    conNo = "co-" + sysdate + "-" + df.format(num) + "";
                } else {
                    conNo = "co-" + sysdate + "-" + "0001";
                }
                System.out.println(conNo);
            }
            return conNo;
        } finally {
            //5.
            DbUtil.dbClose(rs, ps, con);
        }
    }

    //책등록 계약번호 콤보박스
    public Vector<String> contractComboBoxSet(String puName) throws SQLException {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;

        Vector<String> vec = new Vector<>();

        try {
            con = DbUtil.getConnection();

            String sql = "select p.*, c.conNo"
                    + " from contract c join ("
                    + "select puId"
                    + " from publisher"
                    + " where puName = ?"
                    + " group by puId"
                    + ") p"
                    + " on c.puId = p.puId";
            ps = con.prepareStatement(sql);
            ps.setString(1, puName);

            vec.add("선택하세요");

            rs = ps.executeQuery();
            while (rs.next()) {
                String conNo = rs.getString(2);

                vec.add(conNo);
            }

            return vec;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }

    //계약추가 사원이름체크
    public int empNameCheck(String empName) throws SQLException {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;

        int result = 0;

        try {
            con = DbUtil.getConnection();

            String sql = "select count(*)"
                    + " from emp"
                    + " where name = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, empName);

            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }

            return result;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }

    //사원번호 구하기
    public String empNo_search(String empName) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String empNo = "";

        try {
            con = DbUtil.getConnection();

            String sql = "select empNo from emp where name = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, empName);

            rs = ps.executeQuery();
            if (rs.next()) {
                empNo = rs.getString(1);
            }

            return empNo;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }   
 

   
}
