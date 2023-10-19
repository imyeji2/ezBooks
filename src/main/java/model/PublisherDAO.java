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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class PublisherDAO {
 //=========================================은정=========================================================
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    //전체값 출력
    public List<PublisherDTO> showAllPublisher() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<PublisherDTO> list = new ArrayList<>();

        try {
            //1.2 db 연결
            con = DbUtil.getConnection();
            //3 sql 로직 처리
            String sql = "select * from publisher where state = 1 order by puname";
            ps = con.prepareStatement(sql);

            //4 쿼리 실행, 결과 처리
            //executeQuery : select 일 때 , executeUpdate: insert, delete, update 
            rs = ps.executeQuery();
            PublisherDTO dto = null;

            while (rs.next()) { //rs가 존재하면 계속 반복                
                String puid = rs.getString("puId");
                String puName = rs.getString("puName");
                String puTel = rs.getString("puTel");
                String puManager = rs.getString("puManager");
                String puHp = rs.getString("puHp");

                dto = new PublisherDTO(puid, puName, puTel, puManager, puHp);
                list.add(dto);
            }

            System.out.println("list.size()" + list.size() + "dto" + dto);
            return list;

        } finally {
            //5 db 연결 닫기
            DbUtil.dbClose(rs, ps, con);
        }

    }//showAllPublisher

    //전체값 출력2
    public PublisherTableDTO getPublisherInfo(String puId) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //1,2 db연결, 드라이브로딩
            con = DbUtil.getConnection();
            //3 sql 로직 처리
            //sql 작성, f9누르기 전
            String sql = "SELECT COUNT(*) AS contractnum, MIN(condate) AS firstcontract"
                    + "  FROM contract"
                    + " where puid=?"
                    + " GROUP BY puid";
            //f9 누르기
            ps = con.prepareStatement(sql);
            ps.setString(1, puId);

            //4. 쿼리 실행 및 결과 처리
            rs = ps.executeQuery();

            PublisherTableDTO dto = null;
            int count;
            String firstConDate;
            if (rs.next()) {//rs가(sql결과값) 있으면 
                count = rs.getInt(1);
                firstConDate = sdf.format(rs.getTimestamp(2));
            } else {
                count = 0;
                firstConDate = " ";
            }
            dto = new PublisherTableDTO(count, firstConDate);
            System.out.println("매개변수 puid=" + puId + "dto" + dto);
            return dto;

        } finally {
            // 5. db 연결 닫아주기
            DbUtil.dbClose(rs, ps, con);
        }
    }//getPublisherInfo

    //사업자번호 확인
    public int checkPuId(String puId) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //1,2 db 연결
            con = DbUtil.getConnection();

            //3 sql 로직처리
            String sql = "select count(*) from publisher where puId =?";
            ps = con.prepareStatement(sql);
            ps.setString(1, puId);

            //4 쿼리 실행, 결과 처리
            rs = ps.executeQuery();
            int reuslt = 0;

            if (rs.next()) {
                reuslt = rs.getInt(1);
            }
            System.out.println(reuslt);
            return reuslt;

        } finally {
            //5. db연결 닫아주기
            DbUtil.dbClose(rs, ps, con);
        }

    }//checkPuId

    //삭제
    public int delete(List<String> arr) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            //1,2) db 연결, 드라이브 로딩 
            con = DbUtil.getConnection();

            int cnt = 0;
            for (int i = 0; i < arr.size(); i++) {
                //3 sql쿼리 로직 처리
                String sql = "UPDATE publisher set state = 0 where puid =?";
                ps = con.prepareStatement(sql);

                //4 쿼리 실행, 결과 처리
                ps.setString(1, arr.get(i));
                ps.executeUpdate();
                //executeQuery : select //executeUpdate: insert, delete, update 
                cnt++;
            }
            System.out.println("삭제 완료 cnt=" + cnt + "arr.size()" + arr.size());
            return cnt;
        } finally {
            //5 연결 닫기
            DbUtil.dbClose(ps, con);
        }
    }//delete

    //데이터 수정
    public int editPu(PublisherDTO dto) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            //1.2 db 연결
            con = DbUtil.getConnection();

            //3 sql 처리
            String sql = "update publisher set putel=?, pumanager=?, puhp=? where puid= ? ";
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getPuTel());
            ps.setString(2, dto.getPuManager());
            ps.setString(3, dto.getPuHp());
            ps.setString(4, dto.getPuId());

            //4 쿼리 실행 및 결과 처리
            int result = ps.executeUpdate();
            //executeUpdate: insert, delete, update  //executeQuery : select
            System.out.println("결과값:" + result + "dto" + dto);
            return result;
        } finally {
            //5 연결 닫기
            DbUtil.dbClose(ps, con);
        }
    }//editPu

    //수정 저장 데이터 출력
    public PublisherDTO Editshowdata(String puid) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PublisherDTO dto = null;

        try {
            //1,2 db연결
            con = DbUtil.getConnection();

            //3 sql 쿼리문 입력
            String sql = "select * from publisher where puid = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, puid);
            //executeQuery : select //executeUpdate: insert, delete, update  
            rs = ps.executeQuery();
            //4 쿼리 실행해서 값 받아 결과 처리 //executeQuery : select
            if (rs.next()) {
                String puId = rs.getString("puId");
                String puName = rs.getString("puName");
                String puTel = rs.getString("puTel");
                String puManager = rs.getString("puManager");
                String puHp = rs.getString("puHp");

                dto = new PublisherDTO(puId, puName, puTel, puManager, puHp);

                System.out.println("매개변수 puid=" + puid + " ,dto=" + dto);
            }
            //4. 쿼리 실행 및 처리 결과 출력
            return dto;
        } finally {
            //5 db 닫기
            DbUtil.dbClose(rs, ps, con);
        }

    }//Editshowdata

    //출판사명 검색
    public List<PublisherDTO> searchPuname(String serchStr) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<PublisherDTO> list = new ArrayList<>();
        try {
            //1,2 db 연결
            con = DbUtil.getConnection();
            //3 sql 출력
            String sql = "select * from publisher where puname like ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + serchStr + "%");
            //4 쿼리 실행해서 값 받아 결과 처리
            //executeQuery : select //executeUpdate: insert, delete, update  
            rs = ps.executeQuery();

            while (rs.next()) {
                String puid = rs.getString("puid");
                String puName = rs.getString("puName");
                String puTel = rs.getString("puTel");
                String puManager = rs.getString("puManager");
                String puHp = rs.getString("puHp");

                PublisherDTO dto = new PublisherDTO(puid, puName, puTel, puManager, puHp);
                list.add(dto);
                System.out.println(list);
            }
            System.out.println("검색 결과 list.size()=" + list.size());
            return list;
        } finally {
            //5
            DbUtil.dbClose(rs, ps, con);
        }

    }//serchPuname
    
     //담당자명 검색
    public List<PublisherDTO> searchPumanager(String serchStr) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<PublisherDTO> list = new ArrayList<>();
        try {
            //1,2 db 연결
            con = DbUtil.getConnection();
            //3 sql 출력
            String sql = "select * from publisher where pumanager like ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + serchStr + "%");
            //4 쿼리 실행해서 값 받아 결과 처리
            //executeQuery : select //executeUpdate: insert, delete, update  
            rs = ps.executeQuery();

            while (rs.next()) {
                String puid = rs.getString("puid");
                String puName = rs.getString("puName");
                String puTel = rs.getString("puTel");
                String puManager = rs.getString("puManager");
                String puHp = rs.getString("puHp");

                PublisherDTO dto = new PublisherDTO(puid, puName, puTel, puManager, puHp);
                list.add(dto);
                System.out.println(list);
            }
            System.out.println("검색 결과 list.size()=" + list.size());
            return list;
        } finally {
            //5
            DbUtil.dbClose(rs, ps, con);
        }

    }//searchPumanager

    //추가 등록
    public int AddPu(PublisherDTO dto) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            //1,2 db연결
            con = DbUtil.getConnection();

            //3 sql문 출력
            String sql = "insert into publisher"
                    + " values(?, ?, ?, ?, ?, 1)";
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getPuId());
            ps.setString(2, dto.getPuName());
            ps.setString(3, dto.getPuTel());
            ps.setString(4, dto.getPuManager());
            ps.setString(5, dto.getPuHp());

            //4 쿼리 실행해서 값 받아 결과 처리
            //executeUpdate: insert, delete, update 
            int result = ps.executeUpdate();

            System.out.println("수정 결과 = " + result + "dto=" + dto);
            return result;

        } finally {
            //5
            DbUtil.dbClose(ps, con);
        }
    }//AddPu

  //=========================================민기=========================================================  
    //출판사 콤보박스 
    public Vector<String> publisherComboBoxSet() throws SQLException {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        
        Vector<String> vec = new Vector<>();
        
        try {
            con = DbUtil.getConnection();
            
            String sql = "select puName, puId"
                    + " from publisher"
                    + " where state = 1"
                    + " order by puName";
            ps = con.prepareStatement(sql);
            
            vec.add("선택하세요");
            
            rs = ps.executeQuery();
            while(rs.next()) {
                String puName = rs.getString(1);
                
                vec.add(puName);
            }
            
            return vec;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }
    
    //책등록 사업자등록 번호 조회
    public String publisherPuId(String puName) throws SQLException {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        
        String puId = "";
        
        try {
            con = DbUtil.getConnection();
            
            String sql = "select puId"
                    + " from publisher"
                    + " where puName = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, puName);
            
            rs = ps.executeQuery();
            if(rs.next()) {
                puId = rs.getString(1);
            }
            
            return puId;
        } finally {
            DbUtil.dbClose(rs, ps, con);
        }
    }    
}
