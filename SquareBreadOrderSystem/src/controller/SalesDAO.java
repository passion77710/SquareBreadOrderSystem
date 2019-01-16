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
	// 등록
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
			alert.setTitle("판매 정보 입력");
			alert.setHeaderText("판매 정보를 정확히 입력하시오.");
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
		return sVo;
	}

	// 수정
	public SalesVO getSalesUpdate(SalesVO svo, int no) throws Exception {
		// ② 데이터 처리를 위한 SQL 문
		StringBuffer dml = new StringBuffer();
		dml.append("update sales set ");
		dml.append("p_count=?,p_price=?,p_day=?");
		dml.append(" where no = ?");

		Connection con = null;
		PreparedStatement pstmt = null;
		SalesVO retval = null;
		try {
			// ③ DBUtil이라는 클래스의 getConnection( ) 메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// ④ 수정한 학생 정보를 수정하기 위하여 SQL 문장을 생성
			pstmt = con.prepareStatement(dml.toString());
			pstmt.setString(1, svo.getP_count());
			pstmt.setString(2, svo.getP_price());
			pstmt.setString(3, svo.getP_day());
			pstmt.setInt(4, no);

			// ⑤ SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(" 판매 수정 ");
				alert.setHeaderText(" 판매 수정 완료 .");
				alert.setContentText(" 판매 수정 성공 !!!");
				alert.showAndWait();
				retval = new SalesVO();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(" 판매 수정 ");
				alert.setHeaderText(" 판매 수정 실패 .");
				alert.setContentText(" 판매 수정 실패 !!!");
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

	// 삭제
	public void getSalesDelete(int no) throws Exception {
		StringBuffer dml = new StringBuffer();
		dml.append("delete from sales where no = ?");
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
				alert.setTitle(" 판매 삭제 ");
				alert.setHeaderText(" 판매 삭제 완료 .");
				alert.setContentText(" 판매 삭제 성공 !!!");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(" 판매 삭제 ");
				alert.setHeaderText(" 판매 삭제 실패 .");
				alert.setContentText(" 판매 삭제 실패 !!!");
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

	// 판매 전체 리스트
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

	// 데이터베이스에서 판매 테이블의 컬럼의 갯수
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