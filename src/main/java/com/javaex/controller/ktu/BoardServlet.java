package com.javaex.controller.ktu;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.javaex.dao.ktu.BoardDao;
import com.javaex.dao.ktu.BoardDaoImpl;
import com.javaex.util.ktu.WebUtil;
import com.javaex.vo.ktu.BoardVo;
import com.javaex.vo.ktu.UserVo;

@WebServlet("/board")
public class BoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		String actionName = request.getParameter("a");

		if ("list".equals(actionName)) { // 처음 시작할때만 여기 들르고
			int totalRecord = 0; // 전체내용 개수
			int numPerPage = 5; // 페이지당 내용 개수

			request.setAttribute("numPerPage", numPerPage);

			int pagePerBlock = 5; // 1블럭당 페이지수

			request.setAttribute("pagePerBlock", pagePerBlock);

			int totalPage = 0; // 전체 페이지 수
			int totalBlock = 0; // 전체 블럭수

			int nowPage = 1; // 현재페이지
			if (request.getParameter("nowPage") != null) {
				nowPage = Integer.parseInt(request.getParameter("nowPage")); // readfrm 에서 가져오는 거
			}

			request.setAttribute("nowPage", nowPage);
			int nowBlock = 1; // 현재블럭

			int start = 0; // 디비의 select 시작번호
			int end = 10; // 시작번호로 부터 가져올 select 갯수

			int listSize = 0; // 현재 읽어온 게시물의 수

			String keyWord = "", keyField = "";
			//

			if (request.getParameter("reload") != null) {
				if (request.getParameter("reload").equals("true")) {
					keyWord = "";
					keyField = "";
				}
			}
			//
			if (request.getParameter("keyWord") != null) { // keyword 입력된게 있으면 내용 서버로 보내
				keyWord = request.getParameter("keyWord");
				keyField = request.getParameter("keyField");

			}

			start = (nowPage * numPerPage) - numPerPage; // (2 * 5) - 5 = 5
			end = numPerPage; //5

			BoardDao dao = new BoardDaoImpl();// dao 객체 만들어서 접근시키기
			totalRecord = dao.getTotalCount(keyField, keyWord);
			request.setAttribute("totalRecord", totalRecord);
			/////
			totalPage = (int) Math.ceil((double) totalRecord / numPerPage); // 전체페이지수
			request.setAttribute("totalPage", totalPage);
			/////
			nowBlock = (int) Math.ceil((double) nowPage / pagePerBlock); // 현재블럭 계산
			request.setAttribute("nowBlock", nowBlock);
			/////
			totalBlock = (int) Math.ceil((double) totalPage / pagePerBlock); // 전체블럭계산
			request.setAttribute("totalBlock", totalBlock);

			int pageStart = (nowBlock - 1) * pagePerBlock + 1; // 하단 페이지 시작번호
			request.setAttribute("pageStart", pageStart);

			int pageEnd = ((pageStart + pagePerBlock) <= totalPage) ? (pageStart + pagePerBlock - 1) : totalPage;
			// 하단 페이지 끝번호
			request.setAttribute("pageEnd", pageEnd);
			////////////////////////////////////////////////////////////////

			Vector<BoardVo> vlist = dao.getBoardList(keyField, keyWord, start, end); // start:0,end: 5
			System.out.println("vlist:" + vlist.toString());
			listSize = vlist.size(); // vlist 일단 size만 가져가는 걸로하자.
			System.out.println("listSize:" + listSize);
			request.setAttribute("vlist", vlist);
			
			request.setAttribute("keyField", keyField);
			request.setAttribute("keyWord", keyWord);

			// 리스트 가져오기
			System.out.println("완료");
			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");

		} else if ("read".equals(actionName)) {
			// 게시물 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			System.out.println("no:" + no);
			BoardDao dao = new BoardDaoImpl();
			BoardVo boardVo = dao.getBoard(no);
			request.setAttribute("boardVo", boardVo);
			// 게시물 넘버값 가져오기
			dao.upcount(no);
			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");

		} else if ("modifyform".equals(actionName)) {
			// 게시물 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			BoardDao dao = new BoardDaoImpl();
			BoardVo boardVo = dao.getBoard(no);

			// 게시물 화면에 보내기
			request.setAttribute("boardVo", boardVo);
			WebUtil.forward(request, response, "/WEB-INF/views/board/modifyform.jsp");
		} else if ("modify".equals(actionName)) {
			// 게시물 가져오기
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int no = Integer.parseInt(request.getParameter("no"));

			BoardVo vo = new BoardVo(no, title, content);
			BoardDao dao = new BoardDaoImpl();

			dao.update(vo);

			WebUtil.redirect(request, response, "/mysite/board?a=list");
		} else if ("writeform".equals(actionName)) {
			// 로그인 여부체크
			UserVo authUser = getAuthUser(request);
			if (authUser != null) { // 로그인했으면 작성페이지로
				WebUtil.forward(request, response, "/WEB-INF/views/board/writeform.jsp");
			} else { // 로그인 안했으면 리스트로
				WebUtil.redirect(request, response, "/mysite/board?a=list");
			}

		} else if ("write".equals(actionName)) {

		} else if ("delete".equals(actionName)) {
			int no = Integer.parseInt(request.getParameter("no"));

			BoardDao dao = new BoardDaoImpl();
			dao.delete(no);

			WebUtil.redirect(request, response, "/mysite/board?a=list");

		} else {
			System.out.print("else임");
			System.out.println("title:" + request.getParameter("title"));

			WebUtil.redirect(request, response, "/mysite/board?a=list");
		}
	}

	/**
	 * 현재 요청된 리퀘스트가 멀티파트 폼(파일처리용)인지 검사한다.
	 * 
	 * @param request 멀티파트용인경우 true를 그렇지 않은경우 false를 반환한다.
	 * @return true인경우 "multipart/form-data"요청인 경우이다.
	 */
	public static boolean isMultipartRequest(HttpServletRequest request) {
		System.out.println("변수 체크하는 곳");
		boolean retBool = false;

		String contentType = request.getHeader("Content-Type");

		if (contentType != null && contentType.indexOf("multipart/form-data") >= 0) {
			retBool = true;
		}
		System.out.println(retBool);
		return retBool;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (isMultipartRequest(request)) {

			///////////////// 값 다 뺴오자 /////
			int i = 0;
			int j = 0;
			String title = "";
			String content = "";
			String file1 = "";
			String file2 = "";

			request.setCharacterEncoding("UTF-8");
			final String CHARSET = "utf-8";
			final String ATTACHES_DIR = "C:\\attaches";
			final int LIMIT_SIZE_BYTES = 1024 * 1024;

			response.setContentType("text/html; charset=UTF-8");
			request.setCharacterEncoding(CHARSET);

			File attachesDir = new File(ATTACHES_DIR);

			DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
			fileItemFactory.setRepository(attachesDir);
			fileItemFactory.setSizeThreshold(LIMIT_SIZE_BYTES);
			ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
			//
			try {
				List<FileItem> items = fileUpload.parseRequest(request);
				for (FileItem item : items) {
					if (item.isFormField()) { // 일반 parameter
						System.out.printf("파라미터 명 : %s, 파라미터 값 :  %s \n", item.getFieldName(), item.getString(CHARSET));

						if (i == 0) {
							title = item.getString(CHARSET);
							System.out.println("title:" + title);
							i++;
						} else if (i == 1) {
							content = item.getString(CHARSET);
							System.out.println("content:" + content);
							i++;
						}
						////////////////////////////////////////////////////
					} else { // file
						System.out.printf("파라미터 명 : %s, 파일 명 : %s,  파일 크기 : %s bytes \n", item.getFieldName(),
								item.getName(), item.getSize());

						if (j == 0) {
							file1 = item.getName();
							System.out.println("file1:" + file1);
							j++;
						} else if (j == 1) {
							file2 = item.getName();
							System.out.println("file2:" + file2);

							UserVo authUser = getAuthUser(request); 
							int userNo = authUser.getNo(); 
							
							BoardVo vo = new BoardVo(title, content, userNo, file1, file2);
							BoardDao dao = new BoardDaoImpl();
							dao.insert(vo);

							if (item.getSize() > 0) {
								String separator = File.separator;
								int index = item.getName().lastIndexOf(separator);
								String fileName = item.getName().substring(index + 1);
								File uploadFile = new File(ATTACHES_DIR + separator + fileName);
								item.write(uploadFile);
							}
						}
					} // else

				}
			} catch (Exception e) {
				// 파일 업로드 처리 중 오류가 발생하는 경우
				e.printStackTrace();
			}
			///////////////////////////////////////////////////

		}
		/*
		 * else { String actionName = request.getParameter("a"); if
		 * ("write".equals(actionName)) {
		 * 
		 * System.out.println("write 들어옴"); System.out.println("check point2");
		 * 
		 * 
		 * UserVo authUser = getAuthUser(request);
		 * 
		 * String title = request.getParameter("title");
		 * 
		 * String content = request.getParameter("content");
		 * 
		 * int userNo = authUser.getNo(); System.out.println("userNo : [" + userNo +
		 * "]"); System.out.println("title : [" + title + "]");
		 * System.out.println("content : [" + content + "]");
		 * 
		 * String file1 = request.getParameter("file1"); String file2 =
		 * request.getParameter("file2");
		 * 
		 * BoardVo vo = new BoardVo(title, content, userNo, file1, file2); BoardDao dao
		 * = new BoardDaoImpl(); dao.insert(vo); } // if 문
		 * System.out.println("check point4"); doGet(request, response); } //else
		 */
		doGet(request, response);
	} // dopost 끝

	// 로그인 되어 있는 정보를 가져온다.
	protected UserVo getAuthUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");

		return authUser;
	}

}
