package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.EmployeeVO;

public class EmployeeDAO {
	// 등록
	public EmployeeVO getEmployeeRegiste(EmployeeVO evo) throws Exception {
		String day = evo.getJ_day();
		String dml = "insert into employee" + "(no,j_name,j_gender,j_age,j_phone,j_addr,j_day,j_deleteday)"
				+ "values(employee_seq.nextval,?,?,?,?,?,to_date('"+day+"','YYYY-MM-DD'),?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		EmployeeVO eVo = evo;

		try {
			con = DBUtil.getConnection();

			pstmt = con.prepareStatement(dml.toString());
			pstmt.setString(1, eVo.getJ_name());
			pstmt.setString(2, eVo.getJ_gender());
			pstmt.setString(3, eVo.getJ_age());
			pstmt.setString(4, eVo.getJ_phone());
			pstmt.setString(5, eVo.getJ_addr());
			pstmt.setString(6, eVo.getJ_deleteday());

			int i = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
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
		return eVo;
	}

	// 직원의 name 을 입력받아 정보 조회
	public EmployeeVO getEmployeeCheck(String name) throws Exception {
		String dml = "select * from employee where name like " + "? order by no desc";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		EmployeeVO eVo = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml.toString());
			pstmt.setString(1, "%" + name + "%");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				eVo = new EmployeeVO();
				eVo.setNo(rs.getInt("no"));
				eVo.setJ_gender(rs.getString("j_name"));
				eVo.setJ_age(rs.getString("j_age"));
				eVo.setJ_phone(rs.getString(" j_phone"));
				eVo.setJ_addr(rs.getString("j_addr"));
				eVo.setJ_deleteday(rs.getString("j_deleteday"));
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
		return eVo;
	}

	// 직원 수정
	public EmployeeVO getEmployeeUpdate(EmployeeVO evo, int no) throws Exception {
		// ② 데이터 처리를 위한 SQL 문
		StringBuffer dml = new StringBuffer();
		dml.append("update employee set ");
		dml.append("j_age=?, j_phone=?, j_addr=?, j_day=?, j_deleteday=?");
		dml.append("where no = ?");

		Connection con = null;
		PreparedStatement pstmt = null;
		EmployeeVO retval = null;
		
		try {
			// ③ DBUtil이라는 클래스의 getConnection( ) 메서드로 데이터베이스와 연결
			con = DBUtil.getConnection();

			// ④ 수정한 학생 정보를 수정하기 위하여 SQL 문장을 생성
			pstmt = con.prepareStatement(dml.toString());
			pstmt.setString(1, evo.getJ_age());
			pstmt.setString(2, evo.getJ_phone());
			pstmt.setString(3, evo.getJ_addr());
			pstmt.setString(4, evo.getJ_day());
			pstmt.setString(5, evo.getJ_deleteday());
			pstmt.setInt(6, no);

			// ⑤ SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(" 직원 수정 ");
				alert.setHeaderText(" 직원 수정 완료 .");
				alert.setContentText(" 직원 수정 성공 !!!");
				alert.showAndWait();
				retval = new EmployeeVO();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(" 직원 수정 ");
				alert.setHeaderText(" 직원 수정 실패 .");
				alert.setContentText(" 직원 수정 실패 !!!");
				alert.showAndWait();
				System.out.println("수정 전달값 :" + i);
			}
		} catch (SQLException e) {
			System.out.println("e=[" + e + "]");
			System.out.println("직원DAO 수정 SQL 에러");
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

	// 직원 삭제
	public void getEmployeeDelete(int no) throws Exception {
		StringBuffer dml = new StringBuffer();
		dml.append("delete from employee where no = ?");
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
				alert.setTitle(" 직원 삭제 ");
				alert.setHeaderText(" 직원 삭제 완료 .");
				alert.setContentText(" 직원 삭제 성공 !!!");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(" 직원 삭제 ");
				alert.setHeaderText(" 직원 삭제 실패 .");
				alert.setContentText(" 직원 삭제 실패 !!!");
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

	// 직원 전체 리스트
	public ArrayList<EmployeeVO> getEmployeeTotal() {
		ArrayList<EmployeeVO> list = new ArrayList<EmployeeVO>();
		String dml = "select * from employee";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		EmployeeVO eVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(dml.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				eVo = new EmployeeVO();
				eVo.setNo(rs.getInt("no"));
				eVo.setJ_name(rs.getString("j_name"));
				eVo.setJ_gender(rs.getString("j_gender"));
				eVo.setJ_age(rs.getString("j_age"));
				eVo.setJ_phone(rs.getString("j_phone"));
				eVo.setJ_addr(rs.getString("j_addr"));
				eVo.setJ_day(rs.getDate("j_day") + "");
				eVo.setJ_deleteday(rs.getString("j_deleteday") + "");
				list.add(eVo);
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

	// 데이터베이스에서 학생 테이블의 컬럼의 갯수
	public ArrayList<String> getColumnName() {
		ArrayList<String> columnName = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from employee");
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
