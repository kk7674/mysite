package com.javaex.controller.ktu;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.ktu.UserDao;
import com.javaex.dao.ktu.UserDaoImpl;
import com.javaex.util.ktu.WebUtil;
import com.javaex.vo.ktu.UserVo;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
	
		String actionName =request.getParameter("a");
		System.out.println("user:" + actionName);
		
		if("joinform".equals(actionName)) {
			
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinform.jsp");
			
			/*
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/joinform.jsp");
			rd.forward(request, response);
			*/
			
		} else if("join".equals(actionName)) {
      String name = request.getParameter("name");
      String email = request.getParameter("email");
      String password = request.getParameter("password");
      String gender = request.getParameter("gender");
      UserVo vo = new UserVo(name,email,password,gender);
      
      UserDao dao = new UserDaoImpl();
      dao.insert(vo);
      
      RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/joinsuccess.jsp");
      rd.forward(request, response);
    
    // id(email) 중복검사 : api/emailCheck.jsp 대신 적용
    } else if("idcheck".equals(actionName)) {
      String email = request.getParameter("email");
      UserDao dao = new UserDaoImpl();
      
//      Enumeration e = request.getParameterNames();
//      while ( e.hasMoreElements() ){
//        String name = (String) e.nextElement();
//        String[] values = request.getParameterValues(name);   
//        for (String value : values) {
//          System.out.println(this.getClass().getName() + ".name=" + name + ",value=" + value);
//        }   
//      }
      
      response.setContentType("text/html; charset=UTF-8");

      // "true", "false" 문자열이 반환되므로 ajax에서 결과값으로 받아서 처리
      response.getWriter().write(dao.idCheck(email)); 
      
    } else if("modify".equals(actionName)){
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");
			UserVo vo = new UserVo();
			vo.setName(name);
			vo.setPassword(password);
			vo.setGender(gender);
			
			HttpSession session = request.getSession(); 
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			
			int no = authUser.getNo();
			vo.setNo(no);
			
			UserDao dao = new UserDaoImpl();
			dao.update(vo);
			
			authUser.setName(name);
			
			WebUtil.forward(request, response, "/WEB-INF/views/main/index.jsp");
			
		} else if("modifyform".equals(actionName)) { // 회원정보 수정 과정.
			
			HttpSession session = request.getSession(); // 세션 정보 가져오기
			UserVo authUser = (UserVo)session.getAttribute("authUser"); // authUser 이름의 세션 정보 가져와서 세션을 좌측에 넣어줌, (UserVo)는 타입.
			int no = authUser.getNo(); // UserVo의 회원번호 가져와서 no에 저장
			
			UserDao dao = new UserDaoImpl(); // dao 객체 생성
			UserVo userVo = dao.getUser(no); // no에 해당하는 번호의 User를 가져와서 userVo 객체에 저장.( 객체 새로 만들어서 변경된 내용 갱신시키는 것과 동일)
			System.out.println(userVo.toString()); // 출력
			
			request.setAttribute("userVo", userVo); // 
			WebUtil.forward(request, response, "/WEB-INF/views/user/modifyform.jsp");
			
		} else if("loginform".equals(actionName)){
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/loginform.jsp");
			rd.forward(request, response);
			
		} else if("login".equals(actionName)){
			String email = request.getParameter("email");
			String password =request.getParameter("password");
			
			UserDao dao = new UserDaoImpl();
			UserVo vo = dao.getUser(email, password);
			
			if(vo==null) {
				System.out.println("실패");
				response.sendRedirect("/mysite/user?a=loginform&result=fail");
			} else {
				System.out.println("성공");
				//로그인 정보 저장할 세션 만들기
				HttpSession session = request.getSession(true);
				//세션 정보에 vo(번호, 이름) 객체 넣기
				session.setAttribute("authUser", vo);
				//서블릿으로 돌아가기
				response.sendRedirect("/mysite/main");
				return;
			}
			
		} else if("logout".equals(actionName)){
			HttpSession session = request.getSession();  // session 정보 몽땅 다지우는 과정.
			session.removeAttribute("authUser");
			session.invalidate();
			response.sendRedirect("/mysite/main"); // main으로 다시 돌려버리기~ (url)
			
		} else {
			
			WebUtil.redirect(request, response, "/mysite/main");
			/*response.sendRedirect("/mysite/main");*/
		}
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
