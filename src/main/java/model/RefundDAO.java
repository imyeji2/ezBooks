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
public class RefundDAO {

    public RefundDTO getReNo() throws SQLException {//환불번호 중복없이 날짜별 자동생성
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RefundDTO dto = null;

        try {
            //1.2
            con = DbUtil.getConnection();
            String sql = "";
            //3.
            sql = "select A.reNo"
                    + " from (select reNo from refund order by reNo desc)A"
                    + " where ROWNUM = 1"; //가장 최신날짜 및 최신번호 row 1번값으로 정렬
            ps = con.prepareStatement(sql);

            //4.
            rs = ps.executeQuery();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //환불번호 틀
            DecimalFormat df = new DecimalFormat("0000"); //환불번호 틀 ex)0001,0002....

            Date date = new Date();
            String sysdate = sdf.format(date);
            String reNo = "";

            if (rs.next()) {
                String result = rs.getString(1);
                String reDate = result.substring(3, 13); //날짜 문자열 추출
                int num = Integer.parseInt(result.substring(14)); //번호 문자열 추출

                System.out.println("reDate=" + reDate + ",num=" + num);
                if (sysdate.equals(reDate)) { // 오늘 날짜와 환불번호코드가 같으면 최신 환불번호에 +1
                    num += 1;

                    reNo = "RE-" + sysdate + "-" + df.format(num) + "";
                } else { //없으면 RE+판매된 날짜+0001 번호키 생성
                    reNo = "RE-" + sysdate + "-" + "0001";
                }
                System.out.println(reNo);
                dto = new RefundDTO(reNo);
            } else {
                reNo = "RE-" + sysdate + "-" + "0001";
                dto = new RefundDTO(reNo);
            }
            return dto;
        } finally {
            //5.
            DbUtil.dbClose(rs, ps, con);
        }

    }
    
    public int insertRefund(RefundDTO dto) throws SQLException{
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con=DbUtil.getConnection();
            String sql = "insert into refund(reno, sellno)\n"
                    + "values(?,?)";
            ps=con.prepareStatement(sql);
            ps.setString(1, dto.getReno());
            ps.setString(2, dto.getSellno());
            int cnt = ps.executeUpdate();
            
            System.out.println("환불insert 결과 cnt="+cnt+", dto="+dto);
            return cnt;
        } finally{
            DbUtil.dbClose(ps, con);
        }
        
    }
    
    public List<RefundDTO> refundSearch(String reno) throws SQLException{
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<RefundDTO> list = new ArrayList<>();
        try {
            con=DbUtil.getConnection();
            
            String sql = "select r.reno, r.sellno, i.qty*b.price, r.restate, redate\n"
                    + "from refund r join sellitem i\n"
                    + "on r.sellno=i.sellno\n"
                    + "join book b "
                    + "on i.bookno=b.bookno "
                    + "where r.reno like '%' || ? || '%' ";
            
            ps=con.prepareStatement(sql);
            ps.setString(1, reno);
            
            rs=ps.executeQuery();
            while(rs.next()){
                String reno2 = rs.getString(1);
                String sellno = rs.getString(2);
                int price = rs.getInt(3);
                String restate = rs.getString(4);
                Timestamp redate = rs.getTimestamp(5);
                
                RefundDTO dto = new RefundDTO(reno2, sellno, price, redate, restate);
                
                list.add(dto);
            }
            return list;
        }finally{
            DbUtil.dbClose(rs, ps, con);
        }
    }
    
    public List<RefundDTO> refundShowAll() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<RefundDTO> list = new ArrayList<>();
        
        try {
            con = DbUtil.getConnection();

            String sql = "select r.reno, r.sellno, i.qty*b.price, r.restate, redate\n"
                    + "from refund r join sellitem i\n"
                    + "on r.sellno=i.sellno\n"
                    + "join book b\n"
                    + "on i.bookno=b.bookno ";

            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();
            while (rs.next()) {
                String reNo = rs.getString(1);
                String sellNo = rs.getString(2);
                int totalPrice = rs.getInt(3);
                String restate = rs.getString(4);
                Timestamp redate = rs.getTimestamp(5);

                RefundDTO dto = new RefundDTO(reNo, sellNo, totalPrice, redate, restate);
                list.add(dto);
            }
            System.out.println("showAll 결과 list="+list);
            return list;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }
    
    public List<RefundDTO> refundSortState() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<RefundDTO> list = new ArrayList<>();

        try {
            con = DbUtil.getConnection();

            String sql = "select r.reno, r.sellno, i.qty*b.price, r.restate, redate\n"
                    + "from refund r join sellitem i\n"
                    + "on r.sellno=i.sellno\n"
                    + "join book b\n"
                    + "on i.bookno=b.bookno\n"
                    + "order by r.restate desc";

            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();
            while (rs.next()) {
                String reNo = rs.getString(1);
                String sellNo = rs.getString(2);
                int totalPrice = rs.getInt(3);
                String restate = rs.getString(4);
                Timestamp redate = rs.getTimestamp(5);

                RefundDTO dto = new RefundDTO(reNo, sellNo, totalPrice, redate, restate);

                list.add(dto);
            }
            return list;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }
        
    public List<RefundDTO> refundSortState(String state) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<RefundDTO> list = new ArrayList<>();

        try {
            con = DbUtil.getConnection();

            String sql = "select r.reno, r.sellno, i.qty*b.price, r.restate, redate\n"
                    + "from refund r join sellitem i\n"
                    + "on r.sellno=i.sellno\n"
                    + "join book b\n"
                    + "on i.bookno=b.bookno\n"
                    + "where r.restate=?";

            ps = con.prepareStatement(sql);
            ps.setString(1, state);

            rs = ps.executeQuery();
            while (rs.next()) {
                String reNo = rs.getString(1);
                String sellNo = rs.getString(2);
                int totalPrice = rs.getInt(3);
                String restate = rs.getString(4);
                Timestamp redate = rs.getTimestamp(5);

                RefundDTO dto = new RefundDTO(reNo, sellNo, totalPrice, redate, restate);

                list.add(dto);
            }
            System.out.println("매개변수 state=" +state+", list="+list);
            return list;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }
    
    public List<RefundDTO> refundSortDate() throws SQLException { //환불일순 정렬
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<RefundDTO> list = new ArrayList<>();

        try {
            con = DbUtil.getConnection();

            String sql = "select r.reno, r.sellno, i.qty*b.price, r.restate, redate\n"
                    + "from refund r join sellitem i\n"
                    + "on r.sellno=i.sellno\n"
                    + "join book b\n"
                    + "on i.bookno=b.bookno\n"
                    + "order by r.redate desc";

            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();
            while (rs.next()) {
                String reNo = rs.getString(1);
                String sellNo = rs.getString(2);
                int totalPrice = rs.getInt(3);
                String restate = rs.getString(4);
                Timestamp redate = rs.getTimestamp(5);

                RefundDTO dto = new RefundDTO(reNo, sellNo, totalPrice, redate, restate);

                list.add(dto);
            }
            System.out.println("redate순 조회결과 list=" + list);
            return list;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }

    public List<RefundDTO> refundSortPrice() throws SQLException { //가격별 정렬
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<RefundDTO> list = new ArrayList<>();

        try {
            con = DbUtil.getConnection();

            String sql = "select r.reno, r.sellno, i.qty*b.price, r.restate, redate\n"
                    + "from refund r join sellitem i\n"
                    + "on r.sellno=i.sellno\n"
                    + "join book b\n"
                    + "on i.bookno=b.bookno\n"
                    + "order by i.qty*b.price desc";

            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();
            while (rs.next()) {
                String reNo = rs.getString(1);
                String sellNo = rs.getString(2);
                int totalPrice = rs.getInt(3);
                String restate = rs.getString(4);
                Timestamp redate = rs.getTimestamp(5);

                RefundDTO dto = new RefundDTO(reNo, sellNo, totalPrice, redate, restate);

                list.add(dto);
            }
            System.out.println("redate순 조회결과 list=" + list);
            return list;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }
    
    public int refundEdit(String reNo) throws SQLException{
        Connection con = null;
        PreparedStatement ps = null;
        
         try {
             con=DbUtil.getConnection();
             
             String sql = "update refund\n"
                     + "set restate='환불완료'\n"
                     + "where reno=?";
             ps=con.prepareStatement(sql);
             ps.setString(1, reNo);
             
             int cnt = ps.executeUpdate();
             System.out.println("환불결과 cnt="+cnt+",매개변수 reNo="+reNo);
             return cnt;
         } finally{
             DbUtil.dbClose(ps, con);
         }
    }
    
    public int refundComplete(String sellNo) throws SQLException{
        Connection con = null;
        PreparedStatement ps = null;
        
         try {
             con=DbUtil.getConnection();
             
             String sql = "update sell\n"
                     + "set sellstate='환불완료'\n"
                     + "where sellno=?";
             ps=con.prepareStatement(sql);
             ps.setString(1, sellNo);
             
             int cnt = ps.executeUpdate();
             System.out.println("환불결과 cnt="+cnt+",매개변수 reNo="+sellNo);
             return cnt;
         } finally{
             DbUtil.dbClose(ps, con);
         }
    }
    //환불시 재고테이블 +처리
    public int plusStock(String sellNo) throws SQLException {
        Connection con=null;
        PreparedStatement ps=null;
        
        try{
            con=DbUtil.getConnection();
            String sql = "update stock\n"
                    + "set qty= qty+(select si.qty from sellitem si where si.sellno=?)\n" //환불하는해당 수량 재고테이블에 +
                    + "where conno =(select b.conno \n"
                    + "              from sellitem s right join book b\n"
                    + "              on s.bookno=b.bookno\n"
                    + "              where sellno=?)"; //환불하는 해당 판매번호의 계약번호와 같은 재고에
            ps=con.prepareStatement(sql);
            ps.setString(1, sellNo);
            ps.setString(2, sellNo);
            
            int cnt=ps.executeUpdate();
            System.out.println("재고처리결과 cnt="+cnt+", 매개변수 sellNo="+sellNo);
            return cnt;
        }finally{
            DbUtil.dbClose(ps, con);
        }
    }
    
   
}
