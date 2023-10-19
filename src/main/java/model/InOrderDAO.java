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
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author yj
 */
public class InOrderDAO {

    //전체 검색(입고 날짜순 내림차순)
    public List<InOrderTableDTO> selectInOrder() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<InOrderTableDTO> list = new ArrayList<>();

        try {

            //1&2
            con = DbUtil.getConnection();

            //3
            String sql = "select i.inNo, b.conNo, i.qty, i.cost, B.name, i.inDate"
                    + " from inOrder i left join"
                    + " (select c.conNo, e.name from emp e join contract c on e.empNo = c.empNo) B"
                    + " on i.conNo = B.conNo"
                    + " order by inDate desc";
            ps = con.prepareStatement(sql);

            //4
            rs = ps.executeQuery();
            while (rs.next()) {

                String inNo = rs.getString("inNo");
                String conNo = rs.getString("conNo");
                int qty = rs.getInt("qty");
                int cost = rs.getInt("cost");
                String name = rs.getString("name");
                Timestamp inDate = rs.getTimestamp("inDate");

                InOrderTableDTO dto = new InOrderTableDTO(inNo, conNo, qty, cost, name, inDate);
                list.add(dto);

            }
            System.out.println("상품 전체 조회 결과 list.size=" + list.size());
            return list;

        } finally {
            //5.
            DbUtil.dbClose(rs, ps, con);

        }

    }//selectInOrder

    
    
    //입고번호로 검색
    public List<InOrderTableDTO> serchInNo(String serchStr) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<InOrderTableDTO> list = new ArrayList<>();

        try {

            //1&2
            con = DbUtil.getConnection();

            //3
            String sql = "select i.inNo, b.conNo, i.qty, i.cost, B.name, i.inDate"
                    + " from inOrder i left join"
                    + " (select c.conNo, e.name from emp e join contract c on e.empNo = c.empNo) B"
                    + " on i.conNo = B.conNo"
                    + "  where inNo like ? order by inDate desc";

            ps = con.prepareStatement(sql);
            ps.setString(1, serchStr + "%");

            //4
            rs = ps.executeQuery();
            while (rs.next()) {
                String inNo = rs.getString("inNo");
                String conNo = rs.getString("conNo");
                int qty = rs.getInt("qty");
                int cost = rs.getInt("cost");
                String name = rs.getString("name");
                Timestamp inDate = rs.getTimestamp("inDate");

                InOrderTableDTO dto = new InOrderTableDTO(inNo, conNo, qty, cost, name, inDate);
                list.add(dto);

            }
            System.out.println("검색 결과 list.size=" + list.size());
            return list;

        } finally {
            //5
            DbUtil.dbClose(rs, ps, con);
        }

    }//serchInNo

    
    
    //담당자 이름으로 검색
    public List<InOrderTableDTO> serchEmpName(String serchStr) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<InOrderTableDTO> list = new ArrayList<>();

        try {
            //1&2
            con = DbUtil.getConnection();

            //3
            String sql = "select i.inNo, b.conNo, i.qty, i.cost, B.name, i.inDate"
                    + " from inOrder i left join"
                    + " (select c.conNo, e.name from emp e join contract c on e.empNo = c.empNo) B"
                    + " on i.conNo = B.conNo"
                    + "  where name like ? order by inDate desc";

            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + serchStr + "%");

            //4
            rs = ps.executeQuery();

            while (rs.next()) {
                String inNo = rs.getString("inNo");
                String conNo = rs.getString("conNo");
                int qty = rs.getInt("qty");
                int cost = rs.getInt("cost");
                String name = rs.getString("name");
                Timestamp inDate = rs.getTimestamp("inDate");

                InOrderTableDTO dto = new InOrderTableDTO(inNo, conNo, qty, cost, name, inDate);
                list.add(dto);
            }

            System.out.println("검색 결과 list.size()=" + list.size());
            return list;

        } finally {
            //5
            DbUtil.dbClose(rs, ps, con);
        }
    }//serchEmpName

    
    
    //입고일
    public List<InOrderTableDTO> sortIndate() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<InOrderTableDTO> list = new ArrayList<>();
        try {
            //1&2
            con = DbUtil.getConnection();

            //3
            String sql = "select i.inNo, b.conNo, i.qty, i.cost, B.name, i.inDate"
                    + " from inOrder i left join"
                    + " (select c.conNo, e.name from emp e join contract c on e.empNo = c.empNo) B"
                    + " on i.conNo = B.conNo"
                    + " order by inDate desc";

            ps = con.prepareStatement(sql);

            //4
            rs = ps.executeQuery();
            while (rs.next()) {
                String inNo = rs.getString("inNo");
                String conNo = rs.getString("conNo");
                int qty = rs.getInt("qty");
                int cost = rs.getInt("cost");
                String name = rs.getString("name");
                Timestamp inDate = rs.getTimestamp("inDate");

                InOrderTableDTO dto = new InOrderTableDTO(inNo, conNo, qty, cost, name, inDate);
                list.add(dto);
            }

            System.out.println("검색 결과 list.size()=" + list.size());
            return list;
        } finally {
            //5
            DbUtil.dbClose(rs, ps, con);
        }
    }//sortIndate

    
    
    //총금액순 정렬
    public List<InOrderTableDTO> sortTotal() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<InOrderTableDTO> list = new ArrayList<>();

        try {
            //1&2
            con = DbUtil.getConnection();
            //3
            String sql = "select i.inNo, b.ConNo, i.qty, i.cost, B.name, i.inDate, (i.qty*i.cost) total" +
                        " from inOrder i left join" +
                        " (select c.conNo, e.name from emp e join contract c on e.empNo = c.empNo) B " +
                        " on i.conNo = B.conNo" +
                        " order by total desc";
            ps = con.prepareStatement(sql);

            //4
            rs = ps.executeQuery();
            while (rs.next()) {
                String inNo = rs.getString("inNo");
                String conNo = rs.getString("conNo");
                int qty = rs.getInt("qty");
                int cost = rs.getInt("cost");
                String name = rs.getString("name");
                Timestamp inDate = rs.getTimestamp("inDate");

                InOrderTableDTO dto = new InOrderTableDTO(inNo, conNo, qty, cost, name, inDate);
                list.add(dto);
            }

            System.out.println("검색 결과 list.size()=" + list.size());
            return list;

        } finally {
            //5
            DbUtil.dbClose(rs, ps, con);
        }
    }//sortTotal

    
    
//입고 번호 생성
    public InOrderDTO AddInNo() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InOrderDTO dto = null;

        try {
            //1.2
            con = DbUtil.getConnection();
            String sql = "";
            
            //3.
            //입고 일자 중 가장 마지막 입고 번호 받아오기
            sql = "select A.inNo"
                    + " from (select inNo from inOrder order by inDate desc)A"
                    + " where ROWNUM = 1";
            ps = con.prepareStatement(sql);

            //4.
            rs = ps.executeQuery();
            
            String inNo = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//날짜 포멧
            DecimalFormat df = new DecimalFormat("0000");//일련번호로 스타일로 변경할 포멧
            
            //오늘 날짜 받아오기
            Date date = new Date();
            String sysdate = sdf.format(date);
            
            //해당 값이 있을 때
            if (rs.next()) {
                //일련변호 생성을 위한 문자열 쪼개기
                String result = rs.getString(1);//해당 일련번호 받아와서
                //날짜 부분 문자로 받아오기
                String strDate = result.substring(3, 13);
                //일련번호 부분 숫자로 받아오기
                int num = Integer.parseInt(result.substring(14));
                
                //오늘 날짜로 생성된 일련번호가 있으면
                if (sysdate.equals(strDate)) {
                    num = num + 1;
                    //해당 일련번호에 1증가해서 저장
                    inNo = "In-" + sysdate + "-" + df.format(num) + "";
                } else {
                    //오늘 생성된 입고번호가 없으면 0001번으로 일련번호 생성
                    inNo = "In-" + sysdate + "-" + "0001";
                }
            //저장된 값이 없을 때 초기값
            } else {
                inNo = "In-" + sysdate + "-" + "0001";
            }
            dto = new InOrderDTO(inNo);
            return dto;
            
        } finally {
            //5.
            DbUtil.dbClose(rs, ps, con);
        }

    }//AddInNo

    
    
    //입고 등록 
    public int insertInOrder(InOrderDTO dto) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            //1&2
            con = DbUtil.getConnection();

            //3
            String sql = "insert into inOrder(inNo, conNo, qty, cost)"
                    + " values(?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getInNo());
            ps.setString(2, dto.getConNo());
            ps.setInt(3, dto.getQty());
            ps.setInt(4, dto.getCost());

            //4
            int result = ps.executeUpdate();
            System.out.println("상품 등록 결과 result=" + result + "매개변수 dto=" + dto);
            return result;

        } finally {
            //5
            DbUtil.dbClose(ps, con);

        }
    }//insertInOrder

    
    
    //입고 수정 데이터 불러오기
    public InOrderTableDTO serchInOrderEdit(String inNo_pk) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InOrderTableDTO dto = null;

        try {
            //1,2
            con = DbUtil.getConnection();

            //3
            String sql = "select i.inNo, b.conNo, i.qty, i.cost, B.name, i.inDate"
                    + " from inOrder i left join"
                    + " (select c.conNo, e.name from emp e join contract c on e.empNo = c.empNo) B"
                    + " on i.conNo = B.conNo"
                    + " where inNo=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, inNo_pk);

            //4
            rs = ps.executeQuery();
            if (rs.next()) {
                String inNo = rs.getString("inNo");
                String conNo = rs.getString("conNo");
                int qty = rs.getInt("qty");
                int cost = rs.getInt("cost");
                String name = rs.getString("name");
                Timestamp inDate = rs.getTimestamp("inDate");

                dto = new InOrderTableDTO(inNo, conNo, qty, cost, name, inDate);
            }
            System.out.println("매개변수 inNo_pk=" + inNo_pk + " ,dto=" + dto);
            return dto
                    ;
        } finally {
            //5
            DbUtil.dbClose(rs, ps, con);
        }

    }//serchInOrderEdit

    
    //입고 수정 
    public int updateInOrder(InOrderDTO dto) throws SQLException {

        Connection con = null;
        PreparedStatement ps = null;

        try {
            //1,2
            con = DbUtil.getConnection();

            //3
            String sql = "update inOrder"
                    + " set qty =? , cost=?"
                    + "  where inNo = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, dto.getQty());
            ps.setInt(2, dto.getCost());
            ps.setString(3, dto.getInNo());

            //4
            int result = ps.executeUpdate();

            System.out.println("수정 결과 = " + result + "dto=" + dto);
            return result;

        } finally {
            //5
            DbUtil.dbClose(ps, con);
        }
    }//updateInOrder

    
    
    //입고 삭제
    public int deleteInOrder(List<String> arr) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            //1.2
            con = DbUtil.getConnection();

            int cnt = 0;

            for (int i = 0; i < arr.size(); i++) {
                //3
                String sql = "delete from inOrder where inNO =?";
                ps = con.prepareStatement(sql);

                //4
                ps.setString(1, arr.get(i));
                ps.executeUpdate();
                cnt++;
            }
            System.out.println("삭제 완료 cnt =" + cnt + "arr.size()" + arr.size());
            return cnt;

        } finally {
            //5
            DbUtil.dbClose(ps, con);
        }
    }//deleteInOrder
    
    
 
    //입고 상품 기존 수량 검색
    public int selectQty(String conNo, int qty) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //1,2
            con = DbUtil.getConnection();

            //3
            String sql = "select qty from inOrder where conNo=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, conNo);

            //4
            rs = ps.executeQuery();
            int result =0;
            if (rs.next()) {
                int inOrderQty = rs.getInt("qty");
                result = inOrderQty-qty;
               
            }
            System.out.println("매개변수 conNo =" +conNo +"qty="+qty+", 결과="+result);
            return result;
        } finally {
            //5
            DbUtil.dbClose(rs, ps, con);
        }

    }

}
