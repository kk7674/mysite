package com.javaex.dao.ktu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.javaex.vo.ktu.BoardVo;



public class BoardDaoImpl implements BoardDao {
	
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
	public List<BoardVo> getList() {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVo> list = new ArrayList<BoardVo>();

		try {
			conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "select b.no, b.title, b.hit, b.reg_date, b.user_no, b.file1, b.file2, u.name "
					     + " from board b, users u "
					     + " where b.user_no = u.no "
					     + " order by no desc";
			
			pstmt = conn.prepareStatement(query);

			rs = pstmt.executeQuery();
			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String file1 = rs.getString("file1");
				String file2 = rs.getString("file2");
				String userName = rs.getString("name");				
				
				BoardVo vo = new BoardVo(no, title, hit, regDate, userNo, userName, file1, file2);
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

	
	public BoardVo getBoard(int no) {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardVo boardVo = null;
		
		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "select b.no, b.title, b.content, b.hit, b.reg_date, b.user_no, b.file1, b.file2, u.name "
					     + "from board b, users u "
					     + "where b.user_no = u.no "
					     + "and b.no = ?";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			
			rs = pstmt.executeQuery();
			// 4.결과처리
			while (rs.next()) {
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String file1 = rs.getString("file1");
				String file2 = rs.getString("file2");
				String userName = rs.getString("name");
				
				boardVo = new BoardVo(no, title, content, hit, regDate, userNo, file1, file2, userName);
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
		System.out.println(boardVo);
		return boardVo;

	}
	
	
	
	public int insert(BoardVo vo) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = getConnection();
		  
		  System.out.println("vo.userNo : ["+vo.getUserNo()+"]");
      System.out.println("vo.title : ["+vo.getTitle()+"]");
      System.out.println("vo.content : ["+vo.getContent()+"]");
      
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "insert into board values (seq_board_no.nextval, ?, ?, 0, sysdate, ?, ?, ?)";
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setInt(3, vo.getUserNo());
			pstmt.setString(4, vo.getFile1());
			pstmt.setString(5, vo.getFile2());
			
			
      
			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 등록");

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

		return count;
	}
	
	
	public int delete(int no) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "delete from board where no = ?";
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 삭제");

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

		return count;
	}
	
	
	public int update(BoardVo vo) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "update board set title = ?, content = ?, file1 = ?, file2 = ? where no = ? ";
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setString(3, vo.getFile1());
			pstmt.setString(4, vo.getFile2());
			pstmt.setInt(5, vo.getNo());

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 수정");

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

		return count;
	}

	@Override
	public int getTotalCount(String keyField, String keyWord) {		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int totalCount = 0;
		try {
			
			System.out.println("keyField:[" + keyField + "]");
			System.out.println("keyWord:[" + keyWord + "]");
			
			
			con = getConnection();
			System.out.println("뭘까요?");
			
			if (keyWord.equals("null") || keyWord.equals("")) {
				sql = "select count(*) from Board b, users u where b.user_no = u.no";
				pstmt = con.prepareStatement(sql);
			} else {
				sql = "select count(*) from  Board b, users u where b.user_no = u.no and " + keyField + " like ? ";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, "%" + keyWord + "%");
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				totalCount = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return totalCount;
	}
	@Override
	public Vector<BoardVo> getBoardList(String keyField, String keyWord, int start, int end) {
		System.out.println(start);
		System.out.println(end);
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Vector<BoardVo> vlist = new Vector<BoardVo>();
		try {
			con = getConnection();
			if (keyWord.equals("null") || keyWord.equals("")) {
			  sql = "SELECT * \r\n" + 
			      "        FROM(\r\n" + 
			      "              SELECT ROWNUM AS RNUM, A.*\r\n" + 
			      "                  FROM ( select * from Board order by reg_date) A \r\n" + 
			      "               WHERE ROWNUM <= ?+?\r\n" + 
			      "            ) B, users u\r\n" + 
			      "       WHERE RNUM > ? and B.user_no = u.no";
			  
			         
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, start); //0
			  	pstmt.setInt(2, end);	//5
			  	pstmt.setInt(3, start); 

			} else {
				//sql = "select * from  tblBoard where " + keyField + " like ? ";
				//sql += "order by ref desc, pos limit ? , ?";
				
				System.out.println("나여기야");
				sql = "SELECT A.*\r\n" + 
		                  "        FROM(\r\n" + 
		                  "              select rownum as rnum, us.name, bo.no, bo.title, bo.content, bo.hit, bo.reg_date, bo.user_no\r\n" + 
		                  "                   from board bo, users us \r\n" + 
		                  "                where " + keyField +" like ?"+" and bo.user_no = us.no \r\n" + 
		                  "        order by reg_date desc ) A \r\n" +
		                  " 	   WHERE rnum <= ? + ? and rnum > ?";
				
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, "%" + keyWord + "%");
				pstmt.setInt(2, start); //0
				//System.out.println(start);
				pstmt.setInt(3, end); //5
				//System.out.println(end);
				pstmt.setInt(4, start); //0
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BoardVo Vo = new BoardVo();
				Vo.setNo(rs.getInt("no"));
				Vo.setTitle(rs.getString("title"));
				Vo.setContent(rs.getString("content"));
				Vo.setHit(rs.getInt("hit"));
				Vo.setRegDate(rs.getString("reg_date"));
				Vo.setUserNo(rs.getInt("user_no"));
				Vo.setUserName(rs.getString("name"));
				vlist.add(Vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return vlist;
	}	
	@Override
	public void upcount(int no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			con = getConnection();
			sql = "update Board set hit=hit+1 where no=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, no);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
		
}
