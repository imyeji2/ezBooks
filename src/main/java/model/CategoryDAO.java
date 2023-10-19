/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import db.DbUtil;
/**
 *
 * @author yj
 */
public class CategoryDAO {
     //카테고리 전체조회
    public List<CategoryDTO> categoryShowAll() throws SQLException {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        
        List<CategoryDTO> list = new ArrayList<>();
        
        try {
            con = DbUtil.getConnection();
            
            String sql = "select * from category"
                    + " where state = 1"
                    + " order by caNo";
            ps = con.prepareStatement(sql);
            
            rs = ps.executeQuery();
            while(rs.next()) {
                int caNo = rs.getInt(1);
                String caName = rs.getString(2);
                
                CategoryDTO dto = new CategoryDTO(caNo, caName);
                
                list.add(dto);
            }
            
            return list;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }
    
    //카테고리 추가
    public int categoryAdd(CategoryDTO dto) throws SQLException {
        PreparedStatement ps = null;
        Connection con = null;
        
        try {
            con = DbUtil.getConnection();
            
            String sql = "insert into category(caNo, caName)"
                    + " values(caNo_seq.nextval, ?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getCaName());
            
            int cnt = ps.executeUpdate();
            
            return cnt;
        } finally {
            DbUtil.dbClose(ps, con);
        }
    }
    
    //카테고리 수정
    public int categoryEdit(CategoryDTO dto) throws SQLException {
        PreparedStatement ps = null;
        Connection con = null;
        
        try {
            con = DbUtil.getConnection();
            
            String sql = "update category"
                    + " set caName = ?"
                    + " where caNo = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getCaName());
            ps.setInt(2, dto.getCaNo());
            
            int cnt = ps.executeUpdate();
            
            return cnt;
        } finally {
            DbUtil.dbClose(ps, con);
        }
    }
    
    // 카테고리 삭제
    public int categoryDelete(int caNo) throws SQLException {
        PreparedStatement ps = null;
        Connection con = null;
        
        try {
            con = DbUtil.getConnection();
            
            String sql = "update category"
                    + " set state = 0"
                    + " where caNo = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, caNo);
            
            int cnt = ps.executeUpdate();
            
            return cnt;
        } finally {
            DbUtil.dbClose(ps, con);
        }
    }
    
    //카테고리 검색 팝업창 카테고리별 책구하기
    public List<String> categoryBook(int caNo) throws SQLException {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        
        List<String> list = new ArrayList<>();
        
        try {
            con = DbUtil.getConnection();
            
            String sql = "select b.subject"
                    + " from category c join book b"
                    + " on c.caNo = b.caNo"
                    + " and c.caNo = ?"
                    + " and b.state = 1";
            ps = con.prepareStatement(sql);
            ps.setInt(1, caNo);

            rs = ps.executeQuery();
            while(rs.next()) {
                String bookName = rs.getString(1);
                
                list.add(bookName);
                System.out.println(list);
            }
            
            return list;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }
    
    //책등록 카테고리 콤보박스
    public Vector<String> categoryComboBoxSet() throws SQLException {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        
        Vector<String> vec = new Vector<>();
        
        try {
            con = DbUtil.getConnection();
            
            String sql = "select caName"
                    + " from category"
                    + " where state = 1"
                    + " order by caName";
            ps = con.prepareStatement(sql);
            
            vec.add("선택하세요");
            
            rs = ps.executeQuery();
            while(rs.next()) {
                String categoryName = rs.getString(1);
                
                vec.add(categoryName);
            }
            
            return vec;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }
    
    //책등록 카테고리번호 구하기
    public int categoryNo(String caName) throws SQLException {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;

        int result = 0;
        
        try {
            con = DbUtil.getConnection();
            
            String sql = "select caNo"
                    + " from category"
                    + " where caName = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, caName);
            
            rs = ps.executeQuery();
            if(rs.next()) {
                int caNo = rs.getInt(1);
                System.out.println("카테고리번호: " + caNo);
                
                result = caNo;
            }
            
            return result;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }   
}
