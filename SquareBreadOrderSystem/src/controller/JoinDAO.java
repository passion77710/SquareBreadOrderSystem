package controller;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.JoinVO;

public class JoinDAO {
	//선생님 정보 입력
	public boolean getTeacherRegiste(JoinVO tvo) throws Exception {
		String dml = "insert into manager" + " values" + "(?,?)";

		Connection con = null;
		PreparedStatement pstmt = null;
		boolean teacherSucess = false;
		try {
			// DBUtil 클라스의 getConnection 메서드로 데이터 베이스와 연결
			con = DBUtil.getConnection();

			// 입력받은 정보를 처리하기위한 SQL 문 작성
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, tvo.getId());
			pstmt.setString(2, tvo.getPassword());

			// SQL문을 수행후 처리 결과 받아옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("등록성공");
				alert.setHeaderText("관리자 등록 완료");
				alert.setContentText("관리자 등록 성공");
				alert.setResizable(false);
				alert.show();

			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("등록 실패");
				alert.setHeaderText("관리자 등록실패");
				alert.setContentText("다시 입력하세요.");
				alert.setResizable(false);
				alert.show();
			}

		} catch (ClassNotFoundException e) {
			System.out.println("e={" + e + "}");
		} catch (SQLException e) {
			System.out.println("e={" + e + "}");
		} finally {
			// 데이터 베이스 연결 해제
			try {
				if (pstmt != null) {
					pstmt.close();
				} else if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		return teacherSucess;
	}

	// 중복 아이디 확인 메소드
	public boolean getIdOverlap(String idOverlep) throws Exception {
		String dml = "select * from manager where id = ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean teacherSucess = false;
		try {
			// DBUtil 클라스의 getConnection 메서드로 데이터 베이스와 연결
			con = DBUtil.getConnection();

			// 입력받은 학생 정보를 처리하기위한 SQL 문 작성
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, idOverlep);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				teacherSucess = true; // 중복된 아이디가 있다.
			}

			// SQL문을 수행후 처리 결과 받아옴
			int i = pstmt.executeUpdate();

		} catch (ClassNotFoundException e) {
			System.out.println("e={" + e + "}");
		} catch (SQLException e) {
			System.out.println("e={" + e + "}");
		} finally {
			// 데이터 베이스 연결 해제
			try {
				if (rs != null) {
					rs.close();
				} else if (pstmt != null) {
					pstmt.close();
				} else if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
			}
		}
		// 결과 출력
		return teacherSucess;
	}
}
