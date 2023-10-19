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
public class StockDAO {
    
    //입고 테이블에서 등록했을 때 해당 재고가 없으면 insert()
    public int insertStock(StockDTO dto) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            //1&2
            con = DbUtil.getConnection();
            
            //3
            //기존에 저장된 계약 번호가 있는지 조회
            String sql = "select count(*) from stock where conNo = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getConNo());
            
            rs = ps.executeQuery();
            int cnt = 0;
            int result=0;
            if(rs.next()){
                cnt = rs.getInt(1);
                System.out.println(cnt);
            }
            //기존에 등록된 계약 번호가 없으면 새로 insert
            if(cnt<=0){
                
                sql ="insert into stock" +
                            " values(stock_seq.NEXTVAL,?,?)";
                ps = con.prepareStatement(sql);

                ps.setInt(1, dto.getQty());
                ps.setString(2, dto.getConNo());
            
                //4
                result = ps.executeUpdate();
            }
            
             
           System.out.println("입력 결과 ="+result+"매개변수 dto="+dto);
           return result;
        }finally{
            //5
            
        }
    }
    
    
    //재고 테이블 전체 조회(입고번호순 오름차순)
    public List<StockTableViewDTO> showAll() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        List<StockTableViewDTO> list= new ArrayList<>();
        
        try{
            //1,2
            con = DbUtil.getConnection();
            
            //3
            String sql ="select s.stockNo ,b.subject, p.puName,s.qty," +
                        " (select max(i.inDate) from inOrder i where c.conNo = i.conNo group by conNo) as inDate" +
                        " from stock s left join contract c" +
                        " on s.conNo = c.conNo" +
                        " left join book b" +
                        " on c.conNo = b.conNo" +
                        " left join publisher p" +
                        " on c.puId = p.puId" +
                        " order by s.stockNo desc";
            ps = con.prepareStatement(sql);
            
            //4

            rs = ps.executeQuery();
            while(rs.next()){
                int stockNo = rs.getInt("stockNo");
                String subject = rs.getString("subject");
                String puName = rs.getString("puName");
                int qty = rs.getInt("qty");
                Timestamp inDate = rs.getTimestamp("inDate");
                
                StockTableViewDTO dto = new StockTableViewDTO(stockNo, subject, puName, qty, inDate);
                list.add(dto);
            }
            System.out.println("list.size="+list.size());
            return list;
            
            //5
        }finally{
            DbUtil.dbClose(rs, ps, con);
            
        }
        
    }
    //재고 테이블 재고번호순으로 정렬(오름차순)
    public List<StockTableViewDTO> sortStockNo() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<StockTableViewDTO> list= new ArrayList<>();
        try{
            //1.2
            con = DbUtil.getConnection();
            
            //3
            String sql ="select s.stockNo ,b.subject, p.puName,s.qty," +
                        " (select max(i.inDate) from inOrder i where c.conNo = i.conNo group by conNo) as inDate" +
                        " from stock s left join contract c" +
                        " on s.conNo = c.conNo" +
                        " left join book b" +
                        " on c.conNo = b.conNo" +
                        " left join publisher p" +
                        " on c.puId = p.puId" +
                        " order by s.stockNo";
            ps = con.prepareStatement(sql);
            
            //4
            rs = ps.executeQuery();
            
            while(rs.next()){
                int stockNo = rs.getInt("stockNo");
                String subject = rs.getString("subject");
                String puName = rs.getString("puName");
                int qty = rs.getInt("qty");
                Timestamp inDate = rs.getTimestamp("inDate");
                
                StockTableViewDTO dto = new StockTableViewDTO(stockNo, subject, puName, qty, inDate);
                list.add(dto);
            }
            System.out.println("list.size="+list.size());
            return list;
        }finally{
            //5
            DbUtil.dbClose(rs, ps, con);
        }
        
        
    }
    
    //재고 적은 순 정렬
    public List<StockTableViewDTO> sortStockQty() throws SQLException {
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<StockTableViewDTO> list= new ArrayList<>();
        try{
            //1.2
            con = DbUtil.getConnection();
            
            //3
            String sql ="select s.stockNo ,b.subject, p.puName,s.qty," +
                        " (select max(i.inDate) from inOrder i where c.conNo = i.conNo group by conNo) as inDate" +
                        " from stock s left join contract c" +
                        " on s.conNo = c.conNo" +
                        " left join book b" +
                        " on c.conNo = b.conNo" +
                        " left join publisher p" +
                        " on c.puId = p.puId" +
                        " order by s.qty";
            
            ps = con.prepareStatement(sql);
            
            //4
            rs = ps.executeQuery();
            
            while(rs.next()){
                int stockNo = rs.getInt("stockNo");
                String subject = rs.getString("subject");
                String puName = rs.getString("puName");
                int qty = rs.getInt("qty");
                Timestamp inDate = rs.getTimestamp("inDate");
                
                StockTableViewDTO dto = new StockTableViewDTO(stockNo, subject, puName, qty, inDate);
                list.add(dto);
            }
            System.out.println("재고 적은순 list.size="+list.size());
            return list;
        }finally{
            //5
            DbUtil.dbClose(rs, ps, con);
        }        

    }
    
    //책 이름으로 검색
    public List<StockTableViewDTO> serchSubject(String str) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<StockTableViewDTO> list = new ArrayList<>();
        
        try{
            con = DbUtil.getConnection();
            
            String sql = "select s.stockNo ,b.subject, p.puName,s.qty," +
                        " (select max(i.inDate) from inOrder i where c.conNo = i.conNo group by conNo) as inDate" +
                        " from stock s left join contract c" +
                        " on s.conNo = c.conNo" +
                        " left join book b" +
                        " on c.conNo = b.conNo" +
                        " left join publisher p" +
                        " on c.puId = p.puId" +
                        " where b.subject like ? " +
                        " order by s.stockNo desc";
                    ps = con.prepareStatement(sql);
                    ps.setString(1, "%"+str+"%");
            
                    rs = ps.executeQuery();

                    while(rs.next()){
                        int stockNo = rs.getInt("stockNo");
                        String subject = rs.getString("subject");
                        String puName = rs.getString("puName");
                        int qty = rs.getInt("qty");
                        Timestamp inDate = rs.getTimestamp("inDate");

                        StockTableViewDTO dto = new StockTableViewDTO(stockNo, subject, puName, qty, inDate);
                        list.add(dto);
                    }    
                    System.out.println("책 이름으로 검색 결과 list.size()"+list.size()+"매개변수="+str);
                    return list;
            
            }finally{
                DbUtil.dbClose(rs, ps, con);
            }
        }
    
    
    //출판사명으로 검색
    public List<StockTableViewDTO> serchPuName(String str) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<StockTableViewDTO> list = new ArrayList<>();
        
        try{
            con = DbUtil.getConnection();
            
            String sql ="select s.stockNo ,b.subject, p.puName,s.qty," +
                        " (select max(i.inDate) from inOrder i where c.conNo = i.conNo group by conNo) as inDate" +
                        " from stock s left join contract c" +
                        " on s.conNo = c.conNo" +
                        " left join book b" +
                        " on c.conNo = b.conNo" +
                        " left join publisher p" +
                        " on c.puId = p.puId" +
                        " where p.puName like ?" +
                        " order by s.stockNo desc";
                    
                    ps = con.prepareStatement(sql);
                    ps.setString(1, "%"+str+"%");
            
                    rs = ps.executeQuery();

                    while(rs.next()){
                        int stockNo = rs.getInt("stockNo");
                        String subject = rs.getString("subject");
                        String puName = rs.getString("puName");
                        int qty = rs.getInt("qty");
                        Timestamp inDate = rs.getTimestamp("inDate");

                        StockTableViewDTO dto = new StockTableViewDTO(stockNo, subject, puName, qty, inDate);
                        list.add(dto);
                    }    
                    System.out.println("출판사 명으로 검색 결과 list.size()"+list.size()+"매개변수="+str);
                    return list;
            
            }finally{
                DbUtil.dbClose(rs, ps, con);
            }        
    }
    
    //재고확인
    public boolean checkStock(String conNo, int qty) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            
            con =DbUtil.getConnection();
            
            String sql  = "select qty from stock where conNo = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, conNo);
            
            rs=ps.executeQuery();
            
             boolean result =false;
             int stockQty =0; 
            if(rs.next()){
                stockQty = rs.getInt("qty");
                if(stockQty>qty){
                    result = true;
                }
            }
            System.out.println("재고 stockQty:"+stockQty+"수정값 qty"+qty+"결과:"+result);
            return result;
        }finally{
            DbUtil.dbClose(rs, ps, con);
        }
    }
    
}
