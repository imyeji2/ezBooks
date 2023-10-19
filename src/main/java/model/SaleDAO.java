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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yj
 */
public class SaleDAO {
    public List<SaleDTO> SellshowAll() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<SaleDTO> list = new ArrayList<>();
        try {
            con = DbUtil.getConnection();

            String sql = "select s.sellno, s.id, i.bookno, b.subject, i.qty*b.price, s.sellstate, s.selldate\n"
                    + "from sell s join sellitem i\n"
                    + "on s.sellno=i.sellno\n"
                    + "join book b\n"
                    + "on i.bookno=b.bookno\n"
                    + "where s.sellstate in('판매완료','판매처리중')";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String sellno = rs.getString(1);
                String id = rs.getString(2);
                int bookno = rs.getInt(3);
                String subject = rs.getString(4);
                int price = rs.getInt(5);
                String sellstate = rs.getString(6);
                Timestamp selldate = rs.getTimestamp(7);

                SaleDTO saleDto = new SaleDTO(sellno, id, bookno, subject, price, sellstate, selldate);
                list.add(saleDto);
            }
            System.out.println("전체조회결과 list=" + list.size());
            return list;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }

    }

    public List<SaleDTO> searchId(String id) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<SaleDTO> list = new ArrayList<>();
        try {
            con = DbUtil.getConnection();

            String sql = "select s.sellno, s.id, i.bookno, b.subject, i.qty*b.price, s.sellstate, s.selldate\n"
                    + "from sell s join sellitem i\n"
                    + "on s.sellno=i.sellno\n"
                    + "join book b\n"
                    + "on i.bookno=b.bookno\n"
                    + "where s.id=? and s.sellstate in('판매완료','판매처리중')";
            ps = con.prepareStatement(sql);
            ps.setString(1, id);

            rs = ps.executeQuery();

            while (rs.next()) {
                String sellno = rs.getString(1);
                String id2 = rs.getString(2);
                int bookno = rs.getInt(3);
                String subject = rs.getString(4);
                int price = rs.getInt(5);
                String sellstate = rs.getString(6);
                Timestamp selldate = rs.getTimestamp(7);

                SaleDTO dto = new SaleDTO(sellno, id2, bookno, subject, price, sellstate, selldate);
                list.add(dto);

            }
            return list;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }

    public List<SaleDTO> showSortSellDate() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<SaleDTO> list = new ArrayList<>();
        try {
            con = DbUtil.getConnection();

            String sql = "select s.sellno, s.id, i.bookno, b.subject, i.qty*b.price, s.sellstate, s.selldate\n"
                    + "from sell s join sellitem i\n"
                    + "on s.sellno=i.sellno\n"
                    + "join book b\n"
                    + "on i.bookno=b.bookno\n"
                    + "where s.sellstate in('판매완료','판매처리중')\n"
                    + "order by s.selldate desc";
            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                String sellno = rs.getString(1);
                String id = rs.getString(2);
                int bookno = rs.getInt(3);
                String subject = rs.getString(4);
                int price = rs.getInt(5);
                String sellstate = rs.getString(6);
                Timestamp selldate = rs.getTimestamp(7);

                SaleDTO saleDto = new SaleDTO(sellno, id, bookno, subject, price, sellstate, selldate);
                list.add(saleDto);
                System.out.println("처리상태 조회 결과 list = " + list.size());
            }
            return list;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }

    }

    public ArrayList<SaleDTO> showSortPrice() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<SaleDTO> list = new ArrayList<>();
        try {
            con = DbUtil.getConnection();

            String sql = "select s.sellno, s.id, i.bookno, b.subject, i.qty*b.price, s.sellstate, s.selldate\n"
                    + "from sell s join sellitem i\n"
                    + "on s.sellno=i.sellno\n"
                    + "join book b\n"
                    + "on i.bookno=b.bookno\n"
                    + "where s.sellstate in('판매완료','판매처리중')\n"
                    + "order by i.qty*b.price desc";
            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                String sellno = rs.getString(1);
                String id = rs.getString(2);
                int bookno = rs.getInt(3);
                String subject = rs.getString(4);
                int price = rs.getInt(5);
                String sellstate = rs.getString(6);
                Timestamp selldate = rs.getTimestamp(7);

                SaleDTO saleDto = new SaleDTO(sellno, id, bookno, subject, price, sellstate, selldate);
                list.add(saleDto);
            }
            return list;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }

    }

    public ArrayList<SaleDTO> showSortSubject() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<SaleDTO> list = new ArrayList<>();
        try {
            con = DbUtil.getConnection();

            String sql = "select s.sellno, s.id, i.bookno, b.subject, i.qty*b.price, s.sellstate, s.selldate\n"
                    + "from sell s join sellitem i\n"
                    + "on s.sellno=i.sellno\n"
                    + "join book b\n"
                    + "on i.bookno=b.bookno\n"
                    + "where s.sellstate in('판매완료','판매처리중')\n"
                    + "order by b.subject desc";
            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                String sellno = rs.getString(1);
                String id = rs.getString(2);
                int bookno = rs.getInt(3);
                String subject = rs.getString(4);
                int price = rs.getInt(5);
                String sellstate = rs.getString(6);
                Timestamp selldate = rs.getTimestamp(7);

                SaleDTO saleDto = new SaleDTO(sellno, id, bookno, subject, price, sellstate, selldate);
                list.add(saleDto);
            }
            return list;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }

    public List<SaleDTO> showSortState() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<SaleDTO> list = new ArrayList<>();
        try {
            con = DbUtil.getConnection();

            String sql = "select s.sellno, s.id, i.bookno, b.subject, i.qty*b.price, s.sellstate, s.selldate\n"
                    + "from sell s join sellitem i\n"
                    + "on s.sellno=i.sellno\n"
                    + "join book b\n"
                    + "on i.bookno=b.bookno\n"
                    + "where s.sellstate in('판매완료','판매처리중')\n"
                    + "order by s.sellstate desc";
            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                String sellno = rs.getString(1);
                String id = rs.getString(2);
                int bookno = rs.getInt(3);
                String subject = rs.getString(4);
                int price = rs.getInt(5);
                String sellstate = rs.getString(6);
                Timestamp selldate = rs.getTimestamp(7);

                SaleDTO saleDto = new SaleDTO(sellno, id, bookno, subject, price, sellstate, selldate);
                list.add(saleDto);
                System.out.println("처리상태 정렬결과 list" + list.size());
            }
            return list;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }

    public List<SaleDTO> showSortState2(String state) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<SaleDTO> list = new ArrayList<>();
        try {
            con = DbUtil.getConnection();

            String sql = "select s.sellno, s.id, i.bookno, b.subject, i.qty*b.price, s.sellstate, s.selldate\n"
                    + "from sell s join sellitem i\n"
                    + "on s.sellno=i.sellno\n"
                    + "join book b\n"
                    + "on i.bookno=b.bookno\n"
                    + "where s.sellstate=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, state);

            rs = ps.executeQuery();

            while (rs.next()) {
                String sellno = rs.getString(1);
                String id = rs.getString(2);
                int bookno = rs.getInt(3);
                String subject = rs.getString(4);
                int price = rs.getInt(5);
                String sellstate = rs.getString(6);
                Timestamp selldate = rs.getTimestamp(7);

                SaleDTO saleDto = new SaleDTO(sellno, id, bookno, subject, price, sellstate, selldate);
                list.add(saleDto);
                System.out.println("처리상태 정렬결과 list" + list.size());
            }
            return list;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }

    public int updateState(String sellNo) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DbUtil.getConnection();

            String sql = "update sell "
                    + " set sellstate='판매완료' "
                    + " where sellno=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, sellNo);

            int cnt = ps.executeUpdate();
            System.out.println("환불상태 수정결과 cnt=" + cnt + ", 매개변수 sellNo=" + sellNo);
            return cnt;
        } finally {
            DbUtil.dbClose(ps, con);
        }

    }

    public int updateRefund(String sellNo) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DbUtil.getConnection();
            String sql = "update sell"
                    + " set sellstate='환불처리중'"
                    + " where sellno=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, sellNo);

            int cnt = ps.executeUpdate();
            System.out.println("환불업데이트 결과 cnt=" + cnt + ", 매개변수 sellNo=" + sellNo);
            return cnt;
        } finally {
            DbUtil.dbClose(ps, con);
        }

    }

    public int refundComplete(String sellNo) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DbUtil.getConnection();
            String sql = "update sell\n"
                    + "set sellstate='환불완료'\n"
                    + "where sellno=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, sellNo);
            int cnt = ps.executeUpdate();

            System.out.println("판매업데이트 결과 cnt=" + cnt + ", 매개변수 sellNo=" + sellNo);
            return cnt;
        } finally {
            DbUtil.dbClose(ps, con);
        }
    }
}
