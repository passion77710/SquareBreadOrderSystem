package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.JoinVO;

public class JoinController implements Initializable {
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtPassword;
	@FXML
	private TextField txtPassword2;
	@FXML
	private Button btnOverlep;
	@FXML
	private Button btnOk;
	@FXML
	private Button btnCancel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnOverlep.setOnAction(event -> handlerBtnOverlepAction(event)); // 중복확인
		btnOk.setOnAction(event -> handlerBtnOkAction(event)); // 등록
		// 종료 무명메소드
		btnCancel.setOnAction(event -> {
			try {
				Pane root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
				Scene scene = new Scene(root);

				Stage primaryStage = new Stage();
				primaryStage.setTitle("관리자 정보 입력");
				primaryStage.setScene(scene);
				Stage oldStage = (Stage) btnCancel.getScene().getWindow();
				oldStage.close();
				primaryStage.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	// 선생님 등록
	public void handlerBtnOkAction(ActionEvent event) {

		JoinDAO tdao = null;
		JoinVO tvo = null;

		boolean joinSucess = false;

		// 패스워드 확인
		if (txtPassword.getText().trim().equals(txtPassword2.getText().trim())) {
			tvo = new JoinVO(txtId.getText(), txtPassword.getText());
			tdao = new JoinDAO();
			try {
				joinSucess = tdao.getTeacherRegiste(tvo);
				if(joinSucess) {
					Pane root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
					Scene scene = new Scene(root);

					Stage primaryStage = new Stage();
					primaryStage.setTitle("관리자 정보 입력");
					primaryStage.setScene(scene);
					Stage oldStage = (Stage) btnCancel.getScene().getWindow();
					oldStage.close();
					primaryStage.show();
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			txtPassword2.clear();
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("패스워드 검사");
			alert.setHeaderText("패스워드가 올바르지 않습니다.");
			alert.setContentText("패스워드를 다시 입력하세요");
			alert.showAndWait();
		}

	}

	// 아이디 중복확인
	public void handlerBtnOverlepAction(ActionEvent event) {
		String searchid = "";
		JoinDAO tdao = null;
		boolean searchResult = true;

		try {
			searchid = txtId.getText().trim();
			tdao = new JoinDAO();
			searchResult = (boolean) tdao.getIdOverlap(searchid);

			if (!searchResult && !searchid.equals("")) {
				txtId.setDisable(true);
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("아이디 중복검사");
				alert.setHeaderText("사용할수 있는 아이디 입니다.");
				alert.setContentText("아이디를 등록하세요.");
				alert.showAndWait();
			} else {
				txtId.clear();
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("아이디 중복검사");
				alert.setHeaderText(searchid + "를 사용할수 없습니다.");
				alert.setContentText("다른 아이디를 사용해 주세요.");
				alert.showAndWait();
			}

		} catch (Exception e) {
			System.out.println("e={" + e + "}");
		}
	}
}
