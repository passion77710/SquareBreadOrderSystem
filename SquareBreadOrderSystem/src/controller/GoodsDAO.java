package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.EmployeeVO;
import model.GoodsVO;

public class GoodsDAO {
	// ���
	public GoodsVO getGoodsRegiste(GoodsVO gvo) throws Exception {
		String dml = "insert into goods values(goods_seq.nextval,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		GoodsVO gVo = gvo;

		try { 
			con = DBUtil.getConnection();

			pstmt = con.prepareStatement(dml.toString());
			pstmt.setString(1, gVo.getG_name());
			pstmt.setString(2, gVo.getG_unit());
			pstmt.setString(3, gVo.getG_count());
			pstmt.setString(4, gVo.getG_price());
			pstmt.setString(5, gVo.getG_day());

			int i = pstmt.executeUpdate();

			
		} catch (SQLException e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("��ǰ ���� �Է�");
			alert.setHeaderText("��ǰ ������ ��Ȯ�� �Է��Ͻÿ�.");
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
		return gVo;
	}
	
	// ��ǰ�� name �� �Է¹޾� ���� ��ȸ
	public GoodsVO getGoodsCheck(String name) throws Exception {
		String dml = "select * from goods where name like " + "? order by no desc";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		GoodsVO gVo = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml.toString());
			pstmt.setString(1, "%" + name + "%");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				gVo = new GoodsVO();
				gVo.setNo(rs.getInt("no"));
				gVo.setG_name(rs.getString("g_name"));
				gVo.setG_unit(rs.getString("g_unit"));
				gVo.setG_count(rs.getString("g_count"));
				gVo.setG_price(rs.getString("g_price"));
				gVo.setG_day(rs.getString("g_day"));
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
		return gVo;
	}
	
	// �Ѽ��� �ѱݾ�
	public int[] setTextCalculation(String date) {
	      String dml = "select sum(g_count), sum(g_price) from goods where substr(g_day, 1, 5) = ?";
	      Connection con = null;
	      PreparedStatement pstm = null;
	      ResultSet rs = null;
	      int[] sum = null;
	      
	      System.out.println(date);
	      
	      try {
	         con = DBUtil.getConnection();
	         pstm = con.prepareStatement(dml);
	         pstm.setString(1, date);
	         rs = pstm.executeQuery();
	         while(rs.next()) {
	         sum = new int[] { rs.getInt(1), rs.getInt(2) };
	         }
	      } catch (SQLException e) {
	         // TODO: handle exception
	      } catch (Exception e) {
	         // TODO: handle exception
	      } finally {
	         try {
	            if (rs != null)
	               rs.close();
	            if (pstm != null)
	               pstm.close();
	            if (con != null)
	               con.close();
	         } catch (Exception e2) {
	            // TODO: handle exception
	         }
	      }
	      return sum;
	   }

	// ��ǰ ����
	public GoodsVO getGoodsUpdate(GoodsVO gvo, int no) throws Exception {
		StringBuffer dml = new StringBuffer();
		dml.append("update goods set ");
		dml.append("g_count=?,g_price=?,g_day=?");
		dml.append(" where no = ?");

		Connection con = null;
		PreparedStatement pstmt = null;
		GoodsVO retval = null;

		try {
			// �� DBUtil�̶�� Ŭ������ getConnection( ) �޼���� �����ͺ��̽��� ����
			con = DBUtil.getConnection();
			System.out.println("����DAO DB���Ἲ��");

			// �� ������ �л� ������ �����ϱ� ���Ͽ� SQL ������ ����
			pstmt = con.prepareStatement(dml.toString());
			pstmt.setString(1, gvo.getG_count());
			pstmt.setString(2, gvo.getG_price());
			pstmt.setString(3, gvo.getG_day());
			pstmt.setInt(4, no);

			// �� SQL���� ������ ó�� ����� ����
			int i = pstmt.executeUpdate();
			System.out.println("����DAO NO��" + i);

			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(" ��ǰ ���� ");
				alert.setHeaderText(" ��ǰ ���� �Ϸ� .");
				alert.setContentText(" ��ǰ ���� ���� !!!");
				alert.showAndWait();
				retval = new GoodsVO();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(" ��ǰ ���� ");
				alert.setHeaderText(" ��ǰ ���� ���� .");
				alert.setContentText(" ��ǰ ���� ���� !!!");
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
	
	// ��ǰ ����
	public void getGoodsDelete(int no) throws Exception {
		StringBuffer dml = new StringBuffer();
		dml.append("delete from goods where no = ?");
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
				alert.setTitle(" ��ǰ ���� ");
				alert.setHeaderText(" ��ǰ ���� �Ϸ� .");
				alert.setContentText(" ��ǰ ���� ���� !!!");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(" ��ǰ ���� ");
				alert.setHeaderText(" ��ǰ ���� ���� .");
				alert.setContentText(" ��ǰ ���� ���� !!!");
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

	// �����ͺ��̽����� �л� ���̺��� �÷��� ����
	public ArrayList<String> getColumnName() {
		ArrayList<String> columnName = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from goods");
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

	// ��ǰ ��ü ����Ʈ
	public ArrayList<GoodsVO> getEmployeeTotal() {
		ArrayList<GoodsVO> list = new ArrayList<GoodsVO>();
		String dml = "select * from goods";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		GoodsVO gVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				gVo = new GoodsVO();
				
				gVo.setNo(rs.getInt("no"));
				gVo.setG_name(rs.getString("g_name"));
				gVo.setG_unit(rs.getString("g_unit"));
				gVo.setG_count(rs.getString("g_count"));
				gVo.setG_price(rs.getString("g_price"));
				gVo.setG_day(rs.getString("g_day").substring(0, 10));
				
				list.add(gVo);
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
}
