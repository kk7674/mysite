package com.javaex.dao.ktu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.ktu.EmaillistVo;

public class EmaillistDao {
  private Connection getConnection() throws SQLException {
    Connection conn = null;
    try {
      Class.forName("oracle.jdbc.driver.OracleDriver");
      String dburl = "jdbc:oracle:thin:@localhost:1521:xe";
      conn = DriverManager.getConnection(dburl, "webdb", "1234");
    } catch (ClassNotFoundException e) {
      System.err.println("JDBC 드라이버 로드 실패!");
    }
    return conn;
  }
  
	public int insert(EmaillistVo vo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0 ;
		
		try {
			conn = getConnection();
			
			String query ="insert into emaillist values (seq_emaillist_num.nextval, ?, ?, ?)";
			pstmt = conn.prepareStatement(query);	
			
			pstmt.setString(1, vo.getLastName());
			pstmt.setString(2, vo.getFirstName());
			pstmt.setString(3, vo.getEmail());
		
			count = pstmt.executeUpdate();
			
			System.out.println(count + "건 등록");
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
		}
		return count;
	}
	
	public void delete(String num) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    int count = 0 ;
    
    try {
      conn = getConnection();
      
      String query ="delete from emaillist where num = ?";
      pstmt = conn.prepareStatement(query); 
      
      pstmt.setInt(1, Integer.parseInt(num));
    
      count = pstmt.executeUpdate();
      
      System.out.println(count + "건 삭제");
      
    } catch (SQLException e) {
      System.out.println("error:" + e);
    } finally {
      try {
        if (pstmt != null) pstmt.close();
        if (conn != null) conn.close();
      } catch (SQLException e) {
        System.out.println("error:" + e);
      }
    }
  }
	
	public List<EmaillistVo> getList() {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<EmaillistVo> list = new ArrayList<EmaillistVo>();

		try {
		  conn = getConnection();
			
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "select num, last_name, first_name, email "
						 + "from emaillist "
					     + "order by num desc" ;
			pstmt = conn.prepareStatement(query);
			
			rs = pstmt.executeQuery();
			// 4.결과처리
			while(rs.next()) {
				int num = rs.getInt("num");
				String lastName = rs.getString("last_name");
				String firstName = rs.getString("first_name");
				String email = rs.getString("email");
				
				EmaillistVo vo = new EmaillistVo(num, lastName, firstName, email);
				list.add(vo);
			}
			
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return list;
	}

}
