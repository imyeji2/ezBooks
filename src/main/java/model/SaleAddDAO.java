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
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author yj
 */
public class SaleAddDAO {

    public SaleAddDTO AddSellNo() throws SQLException {//판매번호 중복없이 날짜별 자동생성
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        SaleAddDTO dto = null;

        try {
            //1.2
            con = DbUtil.getConnection();
            String sql = "";
            //3.
            sql = "select A.sellNo"
                    + " from (select sellNo from sell order by sellNo desc)A"
                    + " where ROWNUM = 1"; //가장 최신날짜 및 최신번호 row 1번값으로 정렬
            ps = con.prepareStatement(sql);

            //4.
            rs = ps.executeQuery();
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //판매번호 틀
            DecimalFormat df = new DecimalFormat("0000"); //판매번호 틀 ex)0001,0002....

            Date date = new Date();
            String sysdate = sdf.format(date);
            String sellNo = "";
            
            if (rs.next()) {


                String result = rs.getString(1);
                String sellDate = result.substring(4, 14); //날짜 문자열 추출
                int num = Integer.parseInt(result.substring(15)); //번호 문자열 추출
           

                System.out.println("sellDate=" + sellDate + ",num=" + num);
                if (sysdate.equals(sellDate)) { // 오늘 날짜와 판매번호코드가 같으면 최신 판매번호에 +1
                    num += 1;

                    sellNo = "SEL-" + sysdate + "-" + df.format(num) + "";
                } else { //없으면 SEL+판매된 날짜+0001 번호키 생성
                    sellNo = "SEL-" + sysdate + "-" + "0001";
                }
                System.out.println(sellNo);
                dto = new SaleAddDTO(sellNo);
            }else{
                 sellNo = "SEL-" + sysdate + "-" + "0001";
                 dto = new SaleAddDTO(sellNo);
            }
            return dto;
        } finally {
            //5.
            DbUtil.dbClose(rs, ps, con);
        }

    }

    public Vector<String> getBookInfo() throws SQLException { //콤봅가스에 DB 책정보 받아오기
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Vector<String> vec = new Vector<>();
        try {
            con = DbUtil.getConnection();

            String sql = "select subject"
                    + " from book"
                    + " order by bookno";
            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();
            while (rs.next()) {
                String subject = rs.getString(1);
                vec.add(subject);
            }
            System.out.println("책정보받기 결과 vector=" + vec.size());
            return vec;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }

    public Vector<String> getIdInfo() throws SQLException { //콤보박스에 DB 회원 id정보 받아오기
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Vector<String> vec = new Vector<>();
        try {
            con = DbUtil.getConnection();

            String sql = "select id"
                    + " from member"
                    + " order by rownum";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String subject = rs.getString(1);
                vec.add(subject);
            }
            System.out.println("회원정보받기 결과 vector=" + vec.size());
            return vec;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }

    public SaleAddDTO getBookInfo(String subject) throws SQLException { //책이름으로 책넘버 받아오기
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        SaleAddDTO dto = new SaleAddDTO();
        try {
            con = DbUtil.getConnection();
            String sql = "select bookno "
                    + " from book"
                    + " where subject=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, subject);

            rs = ps.executeQuery();
            while (rs.next()) {
                int bookno = rs.getInt(1);
                dto.setBookNo(bookno);
            }

            System.out.println("책번호 가격 받기 결과 dto=" + dto);
            return dto;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }

    public int insertSell(SaleAddDTO dto) throws SQLException { //추가버튼으로 책DB정보 입력
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DbUtil.getConnection();

            String sql = "insert into sell(sellno, id)\n"
                    + "values(?,?)";
            ps = con.prepareStatement(sql);

            String sellNo = dto.getSellNo();
            String id = dto.getId();

            ps.setString(1, sellNo);
            ps.setString(2, id);

            int cnt = ps.executeUpdate();
            System.out.println("판매정보 입력결과 매개변수 dto=" + dto + ", cnt=" + cnt);
            return cnt;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }

    public int insertSellItem(SaleAddDTO dto) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DbUtil.getConnection();

            String sql = "insert into sellItem(sellItem_No, bookno, qty, sellno)\n"
                    + "values('SI-'||sellItem_seq.NEXTVAL,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, dto.getBookNo());
            ps.setInt(2, dto.getQty());
            ps.setString(3, dto.getSellNo());

            int cnt = ps.executeUpdate();
            System.out.println("sellItem 인서트 결과 cnt=" + cnt);
            return cnt;
        } finally {
            DbUtil.dbClose(ps, con);
        }
    }
    
    //재고 확인하기
    public SaleAddDTO checkStock(int bookNo) throws SQLException{
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs = null;
        SaleAddDTO dto = new SaleAddDTO();
        
     
        try{
            con=DbUtil.getConnection();
            
            String sql = "select st.qty\n"
                    + "from sellitem s right join book b\n"
                    + "on s.bookNo=b.bookNo\n"
                    + "join stock st\n"
                    + "on b.conno=st.conno\n"
                    + "where b.bookno=? and b.conno=st.conno"; //해당 책넘버에 해당하는 계약번호와 
                                                               //재고테이블 계약번호 조회후 수량체크
            ps=con.prepareStatement(sql);
            ps.setInt(1, bookNo);
            
            rs=ps.executeQuery();
            if(rs.next()){
                int qty = rs.getInt(1);
                dto.setQty(qty);
            }
            System.out.println("재고확인결과 qty="+dto.getQty());
            return dto;
        }finally{
            DbUtil.dbClose(ps, con);
        }
        
    }
    
    public int minusStock(SaleAddDTO dto) throws SQLException{
        Connection con = null;
        PreparedStatement ps = null;
        
        try{
            con = DbUtil.getConnection();
            String sql = "update stock st\n"
                       + "set qty=qty-?\n"
                       + "where st.conno=(select conno from book where bookno=?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, dto.getQty());
            ps.setInt(2, dto.getBookNo());
            int cnt = ps.executeUpdate();
            
            System.out.println("재고 처리 결과 cnt="+cnt+"매개변수 bookno="+dto.getBookNo()+"qty="+dto.getQty());
            return cnt;
        }finally{
            DbUtil.dbClose(ps, con);
        }
        
    }

}
