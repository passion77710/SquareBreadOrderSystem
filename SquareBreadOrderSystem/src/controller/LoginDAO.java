package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {
	// 로그인시 중복을 찾기위한 메소드
	public boolean getLogin(String id, String password) {
		String dml = "select * from manager"
				+ " where id=? and password=?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean loginid = false;

		try {
			// DBUtil 클라스의 getConnection 메서드로 데이터 베이스와 연결
			con = DBUtil.getConnection();

			// 입력받은 학생 정보를 처리하기위한 SQL 문 작성
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, id);
			pstmt.setString(2, password);

			// SQL문을 수행후 처리 결과 받아옴
			rs = pstmt.executeQuery();
			if (rs.next()) {
				System.out.println("접속성공");
				loginid = true; // 일치하다
			}

		} catch (ClassNotFoundException e) {
			System.out.println("e={" + e + "}");
		} catch (SQLException e) {
			System.out.println("e={" + e + "}");
		} finally {
			// 데이터 베이스 연결 해제
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				} else if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		} 
		return loginid;
	}

}
