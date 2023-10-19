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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author yj
 */
public class BookDAO {
    //책 전체조회
    public List<Map> bookshowAll(String bookOrderBy) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<Map> list = new ArrayList<>();

        try {
            con = DbUtil.getConnection();

            String sql = "select b.bookNo, c.caName, b.subject, b.writer, p.puName, b.price, c1.conNo"
                    + " from book b join publisher p"
                    + " on b.puId = p.puId"
                    + " join category c"
                    + " on b.caNo = c.caNo"
                    + " join contract c1"
                    + " on c1.conNo = b.conNo"
                    + " and b.state = 1"
                    + " order by " + bookOrderBy;
            ps = con.prepareStatement(sql);            

            rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();

                int bookNo = rs.getInt(1);
                String caName = rs.getString(2);
                String subject = rs.getString(3);
                String writer = rs.getString(4);
                String puName = rs.getString(5);
                int price = rs.getInt(6);
                String conNo = rs.getString(7);

                map.put("bookNo", bookNo);
                map.put("caName", caName);
                map.put("subject", subject);
                map.put("writer", writer);
                map.put("puName", puName);
                map.put("price", price);
                map.put("conNo", conNo);

                list.add(map);
            }

            return list;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }

    //책조회
    public List<Map> bookSelect(String bookSel, String userInput) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<Map> list = new ArrayList<>();

        try {
            con = DbUtil.getConnection();

            String sql = "select b.bookNo, c.caName, b.subject, b.writer, p.puName, b.price, b.conNo"
                    + " from book b join publisher p"
                    + " on b.puId = p.puId"
                    + " join category c"
                    + " on b.caNo = c.caNo"
                    + " and not b.state = 0"
                    + " and "+ bookSel+ " like ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, userInput);

            rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();

                int bookNo = rs.getInt(1);
                String caName = rs.getString(2);
                String subject = rs.getString(3);
                String writer = rs.getString(4);
                String puName = rs.getString(5);
                int price = rs.getInt(6);
                String conNo = rs.getString(7);

                map.put("bookNo", bookNo);
                map.put("caName", caName);
                map.put("subject", subject);
                map.put("writer", writer);
                map.put("puName", puName);
                map.put("price", price);
                map.put("conNo", conNo);

                list.add(map);
                
            }
                System.out.println("테스트" + list);
            
            return list;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }

    //책 등록
    public int bookAdd(BookDTO dto) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DbUtil.getConnection();

            String sql = "insert into book(bookNo, caNo, writer, subject, price, puId, conNo, regdate)"
                    + "values(bookNo_seq.nextval, ?, ?, ?, ?, ?, ?, sysdate)";
            ps = con.prepareStatement(sql);

            ps.setInt(1, dto.getCaNo());
            ps.setString(2, dto.getWriter());
            ps.setString(3, dto.getSubject());
            ps.setInt(4, dto.getPrice());
            ps.setString(5, dto.getPuId());
            ps.setString(6, dto.getConNo());
            System.out.println(dto);

            int cnt = ps.executeUpdate();

            return cnt;
        } finally {
            DbUtil.dbClose(ps, con);
        }
    }

    //책 수정
    public int bookEdit(BookDTO dto) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DbUtil.getConnection();

            String sql = "update book"
                    + " set caNo = ?, writer = ?, subject = ?, price = ?, puId = ?, conNo = ?"
                    + " where bookNo = ?";
            ps = con.prepareStatement(sql);

            ps.setInt(1, dto.getCaNo());
            ps.setString(2, dto.getWriter());
            ps.setString(3, dto.getSubject());
            ps.setInt(4, dto.getPrice());
            ps.setString(5, dto.getPuId());
            ps.setString(6, dto.getConNo());
            ps.setInt(7, dto.getBookNo());

            int cnt = ps.executeUpdate();

            return cnt;
        } finally {
            DbUtil.dbClose(ps, con);
        }
    }

    //책삭제
    public int bookDelete(int bookNo) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DbUtil.getConnection();

            String sql = "update book"
                    + " set state = 0"
                    + " where bookno = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, bookNo);

            int cnt = ps.executeUpdate();

            return cnt;
        } finally {
            DbUtil.dbClose(ps, con);
        }
    }

    //책 원가 구하기
    public int bookCost(String conNo) throws SQLException {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;

        int cost = 0;

        try {
            con = DbUtil.getConnection();

            String sql = "select i.cost"
                    + " from book b join inorder i"
                    + " on i.conNo = b.conNo"
                    + " and b.conNo = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, conNo);

            rs = ps.executeQuery();
            if (rs.next()) {
                cost = rs.getInt(1);
            }
            System.out.println("코스트: " + cost);
            return cost;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }    
}
