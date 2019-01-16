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
	// 등록
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
			alert.setTitle("상품 정보 입력");
			alert.setHeaderText("상품 정보를 정확히 입력하시오.");
			alert.setContentText("다음에는 주의하세요!");
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
	
	// 상품의 name 을 입력받아 정보 조회
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
	
	// 총수량 총금액
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

	// 상품 수정
	public GoodsVO getGoodsUpdate(GoodsVO gvo, int no) throws Exception {
		StringBuffer dml = new StringBuffer();
		dml.append("update goods set ");
		dml.append("g_count=?,g_price=?,g_day=?");
		dml.append(" where no = ?");

		Connection con = null;
		PreparedStatement pstmt = null;
		GoodsVO retval = null;

		try {
			// ③ DBUtil이라는 클래스의 getConnection( ) 메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();
			System.out.println("수정DAO DB연결성공");

			// ④ 수정한 학생 정보를 수정하기 위하여 SQL 문장을 생성
			pstmt = con.prepareStatement(dml.toString());
			pstmt.setString(1, gvo.getG_count());
			pstmt.setString(2, gvo.getG_price());
			pstmt.setString(3, gvo.getG_day());
			pstmt.setInt(4, no);

			// ⑤ SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();
			System.out.println("수정DAO NO값" + i);

			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(" 상품 수정 ");
				alert.setHeaderText(" 상품 수정 완료 .");
				alert.setContentText(" 상품 수정 성공 !!!");
				alert.showAndWait();
				retval = new GoodsVO();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(" 상품 수정 ");
				alert.setHeaderText(" 상품 수정 실패 .");
				alert.setContentText(" 상품 수정 실패 !!!");
				alert.showAndWait();
				System.out.println("수정 전달값 :" + i);
			}
		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				// ⑥ 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return retval;
	}
	
	// 상품 삭제
	public void getGoodsDelete(int no) throws Exception {
		StringBuffer dml = new StringBuffer();
		dml.append("delete from goods where no = ?");
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			// ③ DBUtil이라는 클래스의 getConnection( ) 메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// ⑤ SQL문을 수행후 처리 결과를 얻어옴
			pstmt = con.prepareStatement(dml.toString());
			pstmt.setInt(1, no);

			// ⑤ SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(" 상품 삭제 ");
				alert.setHeaderText(" 상품 삭제 완료 .");
				alert.setContentText(" 상품 삭제 성공 !!!");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(" 상품 삭제 ");
				alert.setHeaderText(" 상품 삭제 실패 .");
				alert.setContentText(" 상품 삭제 실패 !!!");
				alert.showAndWait();
			}
		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
		} catch (Exception e) {
			System.out.println("e=[" + e + "]");
		} finally {
			try {
				// ⑥ 데이터베이스와의 연결에 사용되었던 오브젝트를 해제
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
	}

	// 데이터베이스에서 학생 테이블의 컬럼의 갯수
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

	// 상품 전체 리스트
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
