package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.SalesVO;

public class SalesDAO {
	// ���
	public SalesVO getSalesRegiste(SalesVO svo) throws Exception {
		String day = svo.getP_day();
		String dml = "insert into sales" + "(no,p_count,p_price,p_day)" + "values(sales_seq.nextval,?,?,to_date('"+day+"','YYYY-MM-DD'))";
		Connection con = null;
		PreparedStatement pstmt = null;
		SalesVO sVo = svo;
		try {
			con = DBUtil.getConnection();

			pstmt = con.prepareStatement(dml.toString());
			pstmt.setString(1, sVo.getP_count());
			pstmt.setString(2, sVo.getP_price());

			int i = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("�Ǹ� ���� �Է�");
			alert.setHeaderText("�Ǹ� ������ ��Ȯ�� �Է��Ͻÿ�.");
			alert.setContentText("�������� �����ϼ���!");
			alert.showAndWait();

		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return sVo;
	}

	// ����
	public SalesVO getSalesUpdate(SalesVO svo, int no) throws Exception {
		// �� ������ ó���� ���� SQL ��
		StringBuffer dml = new StringBuffer();
		dml.append("update sales set ");
		dml.append("p_count=?,p_price=?,p_day=?");
		dml.append(" where no = ?");

		Connection con = null;
		PreparedStatement pstmt = null;
		SalesVO retval = null;
		try {
			// �� DBUtil�̶�� Ŭ������ getConnection( ) �޼���� �����ͺ��̽��� ����
			con = DBUtil.getConnection();

			// �� ������ �л� ������ �����ϱ� ���Ͽ� SQL ������ ����
			pstmt = con.prepareStatement(dml.toString());
			pstmt.setString(1, svo.getP_count());
			pstmt.setString(2, svo.getP_price());
			pstmt.setString(3, svo.getP_day());
			pstmt.setInt(4, no);

			// �� SQL���� ������ ó�� ����� ����
			int i = pstmt.executeUpdate();

			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(" �Ǹ� ���� ");
				alert.setHeaderText(" �Ǹ� ���� �Ϸ� .");
				alert.setContentText(" �Ǹ� ���� ���� !!!");
				alert.showAndWait();
				retval = new SalesVO();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(" �Ǹ� ���� ");
				alert.setHeaderText(" �Ǹ� ���� ���� .");
				alert.setContentText(" �Ǹ� ���� ���� !!!");
				alert.showAndWait();
				System.out.println("���� ���ް� :" + i);
			}
		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				// �� �����ͺ��̽����� ���ῡ ���Ǿ��� ������Ʈ�� ����
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return retval;
	}

	// ����
	public void getSalesDelete(int no) throws Exception {
		StringBuffer dml = new StringBuffer();
		dml.append("delete from sales where no = ?");
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			// �� DBUtil�̶�� Ŭ������ getConnection( ) �޼���� �����ͺ��̽��� ����
			con = DBUtil.getConnection();

			// �� SQL���� ������ ó�� ����� ����
			pstmt = con.prepareStatement(dml.toString());
			pstmt.setInt(1, no);

			// �� SQL���� ������ ó�� ����� ����
			int i = pstmt.executeUpdate();

			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(" �Ǹ� ���� ");
				alert.setHeaderText(" �Ǹ� ���� �Ϸ� .");
				alert.setContentText(" �Ǹ� ���� ���� !!!");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(" �Ǹ� ���� ");
				alert.setHeaderText(" �Ǹ� ���� ���� .");
				alert.setContentText(" �Ǹ� ���� ���� !!!");
				alert.showAndWait();
			}
		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				// �� �����ͺ��̽����� ���ῡ ���Ǿ��� ������Ʈ�� ����
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
	}

	// �Ǹ� ��ü ����Ʈ
	public ArrayList<SalesVO> getSalesTotal() {
		ArrayList<SalesVO> list = new ArrayList<SalesVO>();
		String dml = "select * from sales order by p_day";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalesVO sVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				sVo = new SalesVO();
				sVo.setNo(rs.getInt("no"));
				sVo.setP_count(rs.getString("p_count"));
				sVo.setP_price(rs.getString("p_price"));
				sVo.setP_day(rs.getDate("p_day") + "");
				list.add(sVo);
			}
		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
			}
		}
		return list;
	}

	// �����ͺ��̽����� �Ǹ� ���̺��� �÷��� ����
	public ArrayList<String> getColumnName() {
		ArrayList<String> columnName = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from sales");
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());

		} catch (SQLException se) {
			System.out.println(se);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException se) {
			}
		}
		return columnName;
	}

}