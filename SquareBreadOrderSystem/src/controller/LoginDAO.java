package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {
	// �α��ν� �ߺ��� ã������ �޼ҵ�
	public boolean getLogin(String id, String password) {
		String dml = "select * from manager"
				+ " where id=? and password=?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean loginid = false;

		try {
			// DBUtil Ŭ���� getConnection �޼���� ������ ���̽��� ����
			con = DBUtil.getConnection();

			// �Է¹��� �л� ������ ó���ϱ����� SQL �� �ۼ�
			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, id);
			pstmt.setString(2, password);

			// SQL���� ������ ó�� ��� �޾ƿ�
			rs = pstmt.executeQuery();
			if (rs.next()) {
				System.out.println("���Ӽ���");
				loginid = true; // ��ġ�ϴ�
			}

		} catch (ClassNotFoundException e) {
			System.out.println("e={" + e + "}");
		} catch (SQLException e) {
			System.out.println("e={" + e + "}");
		} finally {
			// ������ ���̽� ���� ����
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
