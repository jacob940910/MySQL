package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vo.Buy;

@WebServlet("*.db")
public class DBController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DBController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 요청 주소에서 공통된 부분을 제외한 부분을 추출
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String command = requestURI.substring(contextPath.length() + 1);
		switch(command) {
		case "mysql.db":
			
			try {
				// 드라이버 클래스 로드
				Class.forName("com.mysql.jdbc.Driver");
				
				try {
					// 데이터베이스 연결
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jacob?characterEncoding=UTF-8&serverTimezone=UTC", "root", "12345678");
					System.out.println(con);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			break;
		case "oracle.db" :
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.10.10:1521:xe","scott","tiger");
				System.out.println(con);
				con.close();
			}catch(Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			break;
		case "insert.db" :
			Connection con1 = null;
			PreparedStatement pstmt = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/jacob?characterEncoding=UTF-8&serverTimezone=UTC", "root", "12345678");
				pstmt = con1.prepareStatement("insert into usertbl(userid, name, birthyear, addr, mobile, mdate) values(?,?,?,?,?,?)");
				pstmt.setString(1, "jacob");
				pstmt.setString(2, "안중근");
				pstmt.setInt(3, 1905);
				pstmt.setString(4, "서울");
				pstmt.setString(5, "01094416351");
				pstmt.setDate(6, new Date(5, 11 ,3)); //연도는 19를 뺴고 뒤에자리만 작성, 월은 -1 만큼작성 import는 sql.Date로 import
				
				//sql을 실행하고 결과 저장하기
				int result = pstmt.executeUpdate();
				if(result > 0) {
					System.out.println("삽입성공");
				}
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}finally {
				try {
					if(con1 != null)con1.close();
					if(pstmt != null)pstmt.close();
				}catch(Exception e) {
					
				}
			}
			break;
		case "delete.db" :
			Connection con3 = null;
			PreparedStatement pstmt3 = null;
			try {
				// 드라이버 클래스 로드
				Class.forName("com.mysql.jdbc.Driver");
				// 데이터베이스 연결
				con3 = DriverManager.getConnection("jdbc:mysql://localhost:3306/jacob?characterEncoding=UTF-8&serverTimezone=UTC", "root", "12345678");
				pstmt3 = con3.prepareStatement("delete from usertbl where userid=?");
				pstmt3.setString(1, "jacob");
				
				//sql을 실행하고 결과 저장하기
				int result3 = pstmt3.executeUpdate();
				if(result3 > 0) {
					System.out.println("삭제성공");
				}else {
					System.out.println("에러는 없지만 삭제는 하지않음");
				}
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}finally {
				try {
					if(con3 != null)con3.close();
					if(pstmt3 != null)pstmt3.close();
				}catch(Exception e) {
					
				}
			}
			break;
		case "selectlist.db" :
			Connection con4 = null;
			PreparedStatement pstmt4 = null;
			ResultSet rs4 = null;   //sql.ResultSet 을 import
			//select 구문은 결과를 저장할 변수가 필요
			List<Buy> list = new ArrayList<Buy>();
			//list.size();
			try {
				// 드라이버 클래스 로드
				Class.forName("com.mysql.jdbc.Driver");
				// 데이터베이스 연결
				con4 = DriverManager.getConnection("jdbc:mysql://localhost:3306/jacob?characterEncoding=UTF-8&serverTimezone=UTC", "root", "12345678");
				pstmt4 = con4.prepareStatement("select * from buytbl");
				
				
				//sql을 실행하고 결과 저장하기
				rs4 = pstmt4.executeQuery();
				
				//리턴된 데이터를 읽어서 list에 저장 
				while(rs4.next()) {
					Buy buy = new Buy();
					buy.setNum(rs4.getInt("num"));
					buy.setUserid(rs4.getString("userid"));
					buy.setProductname(rs4.getString("productname"));
					buy.setGroupname(rs4.getString("groupname"));
					buy.setPrice(rs4.getInt("price"));
					buy.setAmount(rs4.getInt("amount"));
					
					list.add(buy);
				}
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}finally {
				try {
					if(con4 != null)con4.close();
					if(pstmt4 != null)pstmt4.close();
					if(rs4 != null)rs4.close();
				}catch(Exception e) {}
			}
			//System.out.println(list);		//한줄로 길게 나옵니다.
			for(Buy buy : list) {			//list로 정렬해서 나옵니다.
				System.out.println(buy);
			}
			break;
		case "selectone.db" :
			Connection con5 = null;
			PreparedStatement pstmt5 = null;
			ResultSet rs5 = null;   //sql.ResultSet 을 import
			//데이터 1개를 리턴하는 경우는 변수만 생성
			Buy buy1 = null;
			try {
				// 드라이버 클래스 로드
				Class.forName("com.mysql.jdbc.Driver");
				// 데이터베이스 연결
				con5 = DriverManager.getConnection("jdbc:mysql://localhost:3306/jacob?characterEncoding=UTF-8&serverTimezone=UTC", "root", "12345678");
				pstmt5 = con5.prepareStatement("select * from buytbl where num=?");
				String num = request.getParameter("num");
				//문자열을 정수로 변환해서 ?에 바인딩
				pstmt5.setInt(1, Integer.parseInt(num));
				
				
				//sql을 실행하고 결과 저장하기
				rs5 = pstmt5.executeQuery();		
				
				//리턴된 데이터를 읽어서 list에 저장 
				if(rs5.next()) {
					buy1 = new Buy();
					buy1.setNum(rs5.getInt("num"));
					buy1.setUserid(rs5.getString("userid"));
					buy1.setProductname(rs5.getString("productname"));
					buy1.setGroupname(rs5.getString("groupname"));
					buy1.setPrice(rs5.getInt("price"));
					buy1.setAmount(rs5.getInt("amount"));
					
		
				}
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}finally {
				try {
					if(con5 != null)con5.close();
					if(pstmt5 != null)pstmt5.close();
					if(rs5 != null)rs5.close();
					
					
				}catch(Exception e) {}
			}
			System.out.println(buy1);
			break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}