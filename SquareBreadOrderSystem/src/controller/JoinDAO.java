package controller;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.JoinVO;

public class JoinDAO {
	//������ ���� �Է�
	public boolean getTeacherRegiste(JoinVO tvo) throws Exception {
		String dml = "insert into manager" + " values" + "(?,?)";

		Connection con = null;
		PreparedStatement pstmt = null;
		boolean teacherSucess = false;
		try {
			// DBUtil Ŭ���� getConnection �޼���� ������ ���̽��� ����
			con = DBUtil.getConnection();

			// �Է¹��� ������ ó���ϱ����� SQL �� �ۼ�
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, tvo.getId());
			pstmt.setString(2, tvo.getPassword());

			// SQL���� ������ ó�� ��� �޾ƿ�
			int i = pstmt.executeUpdate();

			if (i == 1) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("��ϼ���");
				alert.setHeaderText("������ ��� �Ϸ�");
				alert.setContentText("������ ��� ����");
				alert.setResizable(false);
				alert.show();

			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("��� ����");
				alert.setHeaderText("������ ��Ͻ���");
				alert.setContentText("�ٽ� �Է��ϼ���.");
				alert.setResizable(false);
				alert.show();
			}

		} catch (ClassNotFoundException e) {
			System.out.println("e={" + e + "}");
		} catch (SQLException e) {
			System.out.println("e={" + e + "}");
		} finally {
			// ������ ���̽� ���� ����
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

	// �ߺ� ���̵� Ȯ�� �޼ҵ�
	public boolean getIdOverlap(String idOverlep) throws Exception {
		String dml = "select * from manager where id = ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean teacherSucess = false;
		try {
			// DBUtil Ŭ���� getConnection �޼���� ������ ���̽��� ����
			con = DBUtil.getConnection();

			// �Է¹��� �л� ������ ó���ϱ����� SQL �� �ۼ�
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, idOverlep);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				teacherSucess = true; // �ߺ��� ���̵� �ִ�.
			}

			// SQL���� ������ ó�� ��� �޾ƿ�
			int i = pstmt.executeUpdate();

		} catch (ClassNotFoundException e) {
			System.out.println("e={" + e + "}");
		} catch (SQLException e) {
			System.out.println("e={" + e + "}");
		} finally {
			// ������ ���̽� ���� ����
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
		// ��� ���
		return teacherSucess;
	}
}
