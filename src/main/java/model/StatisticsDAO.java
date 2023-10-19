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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author yj
 */
public class StatisticsDAO {
    private Date date = new Date();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    DecimalFormat df = new DecimalFormat("00");
    //올해 날짜 구하기
    int year = date.getYear()+1900;

    String firstDay = year+"-01-01";
    String lastDay = year+"-12-31"; 

    //기간별 조회(주간)
    public List<Statistics1DTO> showAllDateTable() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        
        try{
            con = DbUtil.getConnection();
            
            String sql ="SELECT TO_CHAR(TRUNC(s.sellDate, 'IW'), 'YYYY-IW') AS sellWeek," +
                        " SUM(A.total) AS totalSum, SUM(A.qty) AS qtySum" +
                        " FROM sell s" +
                        " LEFT JOIN ( SELECT i.sellNo,NVL(SUM(b.price), 0) AS total," +
                        "                                   NVL(SUM(i.qty), 0) AS qty" +
                        " FROM sellItem i" +
                        " LEFT JOIN book b ON i.bookNo = b.bookNo" +
                        " GROUP BY i.sellNo, b.price, i.qty) A ON s.sellNo = A.sellNo" +
                        " WHERE s.sellstate = '판매완료'" +
                        " AND (s.sellDate > ? AND s.sellDate <= TO_DATE(?) + 1)" +
                        " GROUP BY TO_CHAR(TRUNC(s.sellDate, 'IW'), 'YYYY-IW')" +
                        " ORDER BY TO_CHAR(TRUNC(s.sellDate, 'IW'), 'YYYY-IW') DESC";

            ps = con.prepareCall(sql);
            ps.setString(1, firstDay);
            ps.setString(2, lastDay);

            
            rs = ps.executeQuery();
            List<Statistics1DTO> list = new ArrayList<>();
            Statistics1DTO Dto = null;
            while(rs.next()){
                String sellDate = rs.getString(1);
                int price = rs.getInt(2);
                int qty = rs.getInt(3);
                
                Dto = new Statistics1DTO(sellDate, price, qty);
                list.add(Dto);
                
            }
           
            System.out.println("주간 조회 결과 list.size()"+list.size()+"dto="+Dto);
            return list;
   

        }finally{
            DbUtil.dbClose(rs, ps, con);
        }
    }//showAllDateTable
    
    
     //기간별 조회(월간)
    public List<Statistics1DTO> showAllDateTable_Month() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Calendar cal = Calendar.getInstance();
                
        
        try{
            con = DbUtil.getConnection();
            
            String sql ="SELECT TO_CHAR(s.sellDate, 'YYYY-MM') AS  sellMonth," +
                        " SUM(A.total) AS totalSum, SUM(A.qty) AS qtySum" +
                        " FROM sell s" +
                        " LEFT JOIN ( SELECT i.sellNo,NVL(SUM(b.price), 0) AS total," +
                        "                                   NVL(SUM(i.qty), 0) AS qty" +
                        " FROM sellItem i" +
                        " LEFT JOIN book b ON i.bookNo = b.bookNo" +
                        " GROUP BY i.sellNo, b.price, i.qty) A ON s.sellNo = A.sellNo" +
                        " WHERE s.sellstate = '판매완료'" +
                        " AND (s.sellDate > ? AND s.sellDate <= TO_DATE(?) + 1)" +
                        " GROUP BY TO_CHAR(s.sellDate, 'YYYY-MM')" +
                        " ORDER BY TO_CHAR(s.sellDate, 'YYYY-MM') DESC";
            
            ps = con.prepareCall(sql);
            ps.setString(1, firstDay);
            ps.setString(2, lastDay);

            
            rs = ps.executeQuery();
            List<Statistics1DTO> list = new ArrayList<>();
            Statistics1DTO Dto = null;
            while(rs.next()){
                String sellDate = rs.getString(1);
                int price = rs.getInt(2);
                int qty = rs.getInt(3);
                
                Dto = new Statistics1DTO(sellDate, price, qty);
                list.add(Dto);
                
            }
           
            System.out.println("월간 조회 결과 list.size()"+list.size()+"dto="+Dto);
            return list;
   

        }finally{
            DbUtil.dbClose(rs, ps, con);
        }        
    }//showAllDateTable_Month
    
    
    ////기간별 조회(연간)
    public List<Statistics1DTO> showAllDateTable_Year() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
           
        
        try{
            con = DbUtil.getConnection();
            
            
            String sql ="SELECT TO_CHAR(s.sellDate, 'YYYY') AS  sellYear," +
                        " SUM(A.total) AS totalSum, SUM(A.qty) AS qtySum" +
                        " FROM sell s" +
                        " LEFT JOIN ( SELECT i.sellNo,NVL(SUM(b.price), 0) AS total," +
                        "                                   NVL(SUM(i.qty), 0) AS qty" +
                        " FROM sellItem i" +
                        " LEFT JOIN book b ON i.bookNo = b.bookNo" +
                        " GROUP BY i.sellNo, b.price, i.qty) A ON s.sellNo = A.sellNo" +
                        " WHERE s.sellstate = '판매완료'" +
                        " AND s.sellDate <= TO_DATE(?) + 1" +
                        " GROUP BY TO_CHAR(s.sellDate, 'YYYY')" +
                        " ORDER BY TO_CHAR(s.sellDate, 'YYYY') DESC";
            
            ps = con.prepareCall(sql);
            ps.setString(1, lastDay);

            
            rs = ps.executeQuery();
            List<Statistics1DTO> list = new ArrayList<>();
            Statistics1DTO Dto = null;
            while(rs.next()){
                String sellDate = rs.getString(1);
                int price = rs.getInt(2);
                int qty = rs.getInt(3);
                
                Dto = new Statistics1DTO(sellDate, price, qty);
                list.add(Dto);
                
            }
           
            System.out.println("연간 조회 결과 list.size()"+list.size()+"dto="+Dto);
            return list;
   

        }finally{
            DbUtil.dbClose(rs, ps, con);
        }       
    }//showAllDateTable_Year

    
    
    //기간별 통계 검색
    public List<Statistics1DTO> showAllDateTable_Serch(String date1, String date2) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        //이번달 시작 날짜
        String firstDay = date1;
        
        //이번달 마지막 날짜
         String lastDay =  date2;        
        
        try{
            con = DbUtil.getConnection();
            
            String sql ="SELECT TO_CHAR(s.sellDate, 'YYYY-MM-DD') AS  sellDate," +
                        " SUM(A.total) AS totalSum, SUM(A.qty) AS qtySum" +
                        " FROM sell s" +
                        " LEFT JOIN ( SELECT i.sellNo,NVL(SUM(b.price), 0) AS total," +
                        "                                   NVL(SUM(i.qty), 0) AS qty" +
                        " FROM sellItem i" +
                        " LEFT JOIN book b ON i.bookNo = b.bookNo" +
                        " GROUP BY i.sellNo, b.price, i.qty) A ON s.sellNo = A.sellNo" +
                        " WHERE s.sellstate = '판매완료'" +
                        " AND (s.sellDate > ? AND s.sellDate <= TO_DATE(?) + 1)" +
                        " GROUP BY TO_CHAR(s.sellDate, 'YYYY-MM-DD')" +
                        " ORDER BY TO_CHAR(s.sellDate, 'YYYY-MM-DD') DESC";
            
            ps = con.prepareCall(sql);
            ps.setString(1, firstDay);
            ps.setString(2, lastDay);

            
            rs = ps.executeQuery();
            List<Statistics1DTO> list = new ArrayList<>();
            Statistics1DTO Dto = null;
            while(rs.next()){
                String sellDate = rs.getString(1);
                int price = rs.getInt(2);
                int qty = rs.getInt(3);
                
                Dto = new Statistics1DTO(sellDate, price, qty);
                list.add(Dto);
                
            }
           
            System.out.println("월간 조회 결과 list.size()"+list.size()+"dto="+Dto);
            return list;
   

        }finally{
            DbUtil.dbClose(rs, ps, con);
        }            
    }//showAllDateTable_Serch
//--------------------------------연령별, 성별 통계-------------------------------------------------------
    //주간 조회
    public List<Statistics2DTO> showDefaultRankAgeNGenger_Week() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Calendar cal = Calendar.getInstance();
        
        //이번주 시작 날짜
        cal.add(Calendar.DATE,2 - cal.get(Calendar.DAY_OF_WEEK));
        String firstWeekDay = sdf.format(date)+"-"+df.format(cal.get(Calendar.DATE));
        
        //이번주 마지막 날짜
        cal.add(Calendar.DATE,7 - cal.get(Calendar.DAY_OF_WEEK)+1);
        String lastWeekDay = sdf.format(date)+"-"+df.format(cal.get(Calendar.DATE));    

        try{
            con = DbUtil.getConnection();
            
            String sql = "select a.gender, a.age,count(*)" +
                            " from sell s " +
                            " left join " +
                            " (select id," +
                            " case when substr(ssn,8,1) in ('1','3') then '남자' else '여자' end as gender," +
                            " trunc(extract(year from sysdate) - (substr(ssn,1,2) + case when substr(ssn,8,1) in ('1','2') then 1900 else 2000 end)+1, -1) as age" +
                            " from member)A" +
                            " on s.id=A.id" +
                            " where s.sellDate>=? and s.sellDate<to_date(?)+1"+
                            " and s.sellstate='판매완료'"+
                            " group by A.age, A.gender" +
                            " order by count(*) desc";

            
            ps = con.prepareStatement(sql);
            ps.setString(1, firstWeekDay);
            ps.setString(2, lastWeekDay);
            
            rs = ps.executeQuery();
            List<Statistics2DTO> list = new ArrayList<>();

            while(rs.next()){
                String gender = rs.getString(1);
                int age = rs.getInt(2); 
                int count = rs.getInt(3);
                
                Statistics2DTO dto = new Statistics2DTO(age, gender, count);
                list.add(dto);
            }
            System.out.println("이번주 연령별 통계 list.size()"+list.size());
            return list;
            
        }finally{
            DbUtil.dbClose(rs, ps, con);
        }
    }
    
    //월간 조회
    public List<Statistics2DTO> showDefaultRankAgeNGenger_Month() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Calendar cal = Calendar.getInstance();
        
        //이번달 시작 날짜
        String firstDay = sdf.format(date)+"-01";
        
        //이번달 마지막 날짜
         String lastDay =  sdf.format(date)+"-"+df.format(cal.getActualMaximum(Calendar.DAY_OF_MONTH ));

        
        
        try{
            con = DbUtil.getConnection();
            
            String sql = "select a.gender, a.age,count(*)" +
                            " from sell s " +
                            " left join " +
                            " (select id," +
                            " case when substr(ssn,8,1) in ('1','3') then '남자' else '여자' end as gender," +
                            " trunc(extract(year from sysdate) - (substr(ssn,1,2) + case when substr(ssn,8,1) in ('1','2') then 1900 else 2000 end)+1, -1) as age" +
                            " from member)A" +
                            " on s.id=A.id" +
                            " where s.sellDate>=? and s.sellDate<to_date(?)+1"+
                            " and s.sellstate='판매완료'"+
                            " group by A.age, A.gender" +
                            " order by count(*) desc";

            
            ps = con.prepareStatement(sql);
            ps.setString(1, firstDay);
            ps.setString(2, lastDay);
            
            rs = ps.executeQuery();
            List<Statistics2DTO> list = new ArrayList<>();

            while(rs.next()){
                String gender = rs.getString(1);
                int age = rs.getInt(2);
                int count = rs.getInt(3);
                
                Statistics2DTO dto = new Statistics2DTO(age, gender, count);
                list.add(dto);
            }
            System.out.println("이번달 연령별 통계 list.size()"+list.size());
            return list;

            
        }finally{
             DbUtil.dbClose(rs, ps, con);
        }        

    }
    
    //연간 조회
    public List<Statistics2DTO> showDefaultRankAgeNGenger_Year() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        //올해 연도 구하기
        int year = date.getYear()+1900;
        
        String firstDay = year+"-01-01";
        String lastDay = year+"-12-31";
        
        try{
            con = DbUtil.getConnection();
            
            String sql = "select a.gender, a.age,count(*)" +
                            " from sell s " +
                            " left join " +
                            " (select id," +
                            " case when substr(ssn,8,1) in ('1','3') then '남자' else '여자' end as gender," +
                            " trunc(extract(year from sysdate) - (substr(ssn,1,2) + case when substr(ssn,8,1) in ('1','2') then 1900 else 2000 end)+1, -1) as age" +
                            " from member)A" +
                            " on s.id=A.id" +
                            " where s.sellDate>=? and s.sellDate<to_date(?)+1"+
                            " and s.sellstate='판매완료'"+
                            " group by A.age, A.gender" +
                            " order by count(*) desc";

            
            ps = con.prepareStatement(sql);
            ps.setString(1, firstDay);
            ps.setString(2, lastDay);
            
            rs = ps.executeQuery();
            List<Statistics2DTO> list = new ArrayList<>();

            while(rs.next()){
                String gender = rs.getString(1);
                int age = rs.getInt(2);
                int count = rs.getInt(3);
                
                Statistics2DTO dto = new Statistics2DTO(age, gender, count);
                list.add(dto);
            }
            System.out.println("이번년도 연령별 통계 list.size()"+list.size());
            return list;

            
        }finally{
             DbUtil.dbClose(rs, ps, con);
        }            
    }
//-------------------------------카테고리별 통계--------------------------------------------------------
    //주간
    public List<Statistics3DTO> showCategoryRank_week() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Calendar cal = Calendar.getInstance();
        
        //이번주 시작 날짜
        cal.add(Calendar.DATE,2 - cal.get(Calendar.DAY_OF_WEEK));
        String firstWeekDay = sdf.format(date)+"-"+df.format(cal.get(Calendar.DATE));
        
        //이번주 마지막 날짜
        cal.add(Calendar.DATE,7 - cal.get(Calendar.DAY_OF_WEEK)+1);
        String lastWeekDay = sdf.format(date)+"-"+df.format(cal.get(Calendar.DATE));        

        
        try{
            con = DbUtil.getConnection();
            
            String sql = "SELECT c.caname, SUM(s.qty) AS total_qty, " +
                        " RANK() OVER (ORDER BY SUM(s.qty) DESC) AS rank" +
                        " FROM sellitem s" +
                        " LEFT JOIN book b ON s.bookNo = b.bookNO" +
                        " LEFT JOIN category c ON b.CANO = c.CANO" +
                        " LEFT JOIN sell ss ON s.sellno = ss.sellno" +
                        " where sellstate like '판매완료'" +
                        " and ss.sellDate>=? and ss.sellDate<to_date(?)+1" +
                        " GROUP BY c.caname" +
                        " ORDER BY rank";

            
            ps = con.prepareStatement(sql);
            ps.setString(1, firstWeekDay);
            ps.setString(2, lastWeekDay);
            
            rs = ps.executeQuery();
            List<Statistics3DTO> list = new ArrayList<>();

            while(rs.next()){
                String caName = rs.getString(1);
                int pty = rs.getInt(2); 
                
                Statistics3DTO dto = new Statistics3DTO(caName, pty);
                list.add(dto);
            }
            System.out.println("이번주 카테고리별 통계 list.size()"+list.size());
            return list;
            
        }finally{
            DbUtil.dbClose(rs, ps, con);
        }
    }
    
    
    
    //월간
    public List<Statistics3DTO> showCategoryRank_Month() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Calendar cal = Calendar.getInstance();
        
        //이번달 시작 날짜
        String firstDay = sdf.format(date)+"-01";
        
        //이번달 마지막 날짜
         String lastDay =  sdf.format(date)+"-"+df.format(cal.getActualMaximum(Calendar.DAY_OF_MONTH ));         
        
        try{
            con = DbUtil.getConnection();
            
            String sql = "SELECT c.caname, SUM(s.qty) AS total_qty, " +
                        " RANK() OVER (ORDER BY SUM(s.qty) DESC) AS rank" +
                        " FROM sellitem s" +
                        " LEFT JOIN book b ON s.bookNo = b.bookNO" +
                        " LEFT JOIN category c ON b.CANO = c.CANO" +
                        " LEFT JOIN sell ss ON s.sellno = ss.sellno" +
                        " where sellstate like '판매완료'" +
                        " and ss.sellDate>=? and ss.sellDate<to_date(?)+1" +
                        " GROUP BY c.caname" +
                        " ORDER BY rank";

            
            ps = con.prepareStatement(sql);
            ps.setString(1, firstDay);
            ps.setString(2, lastDay);
            
            rs = ps.executeQuery();
            List<Statistics3DTO> list = new ArrayList<>();

            while(rs.next()){
                String caName = rs.getString(1);
                int pty = rs.getInt(2); 
                
                Statistics3DTO dto = new Statistics3DTO(caName, pty);
                list.add(dto);
            }
            System.out.println("월간 카테고리별 통계 list.size()"+list.size());
            return list;
   

        }finally{
            DbUtil.dbClose(rs, ps, con);
        }               
    }
    
    //연간
    public List<Statistics3DTO> showCategoryRank_Year() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        //올해 연도 구하기
        int year = date.getYear()+1900;
        
        String firstDay = year+"-01-01";
        String lastDay = year+"-12-31";        
                
        
        try{
            con = DbUtil.getConnection();
            
            String sql = "SELECT c.caname, SUM(s.qty) AS total_qty, " +
                        " RANK() OVER (ORDER BY SUM(s.qty) DESC) AS rank" +
                        " FROM sellitem s" +
                        " LEFT JOIN book b ON s.bookNo = b.bookNO" +
                        " LEFT JOIN category c ON b.CANO = c.CANO" +
                        " LEFT JOIN sell ss ON s.sellno = ss.sellno" +
                        " where sellstate like '판매완료'" +
                        " and ss.sellDate>=? and ss.sellDate<to_date(?)+1" +
                        " GROUP BY c.caname" +
                        " ORDER BY rank";

            
            ps = con.prepareStatement(sql);
            ps.setString(1, firstDay);
            ps.setString(2, lastDay);
            
            rs = ps.executeQuery();
            List<Statistics3DTO> list = new ArrayList<>();

            while(rs.next()){
                String caName = rs.getString(1);
                int pty = rs.getInt(2); 
                
                Statistics3DTO dto = new Statistics3DTO(caName, pty);
                list.add(dto);
            }
            System.out.println("연간 카테고리별 통계 list.size()"+list.size());
            return list;
   

        }finally{
            DbUtil.dbClose(rs, ps, con);
        }          
    }




    
}
