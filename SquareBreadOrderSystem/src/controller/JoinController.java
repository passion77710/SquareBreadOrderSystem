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

		btnOverlep.setOnAction(event -> handlerBtnOverlepAction(event)); // �ߺ�Ȯ��
		btnOk.setOnAction(event -> handlerBtnOkAction(event)); // ���
		// ���� ����޼ҵ�
		btnCancel.setOnAction(event -> {
			try {
				Pane root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
				Scene scene = new Scene(root);

				Stage primaryStage = new Stage();
				primaryStage.setTitle("������ ���� �Է�");
				primaryStage.setScene(scene);
				Stage oldStage = (Stage) btnCancel.getScene().getWindow();
				oldStage.close();
				primaryStage.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	// ������ ���
	public void handlerBtnOkAction(ActionEvent event) {

		JoinDAO tdao = null;
		JoinVO tvo = null;

		boolean joinSucess = false;

		// �н����� Ȯ��
		if (txtPassword.getText().trim().equals(txtPassword2.getText().trim())) {
			tvo = new JoinVO(txtId.getText(), txtPassword.getText());
			tdao = new JoinDAO();
			try {
				joinSucess = tdao.getTeacherRegiste(tvo);
				if(joinSucess) {
					Pane root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
					Scene scene = new Scene(root);

					Stage primaryStage = new Stage();
					primaryStage.setTitle("������ ���� �Է�");
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
			alert.setTitle("�н����� �˻�");
			alert.setHeaderText("�н����尡 �ùٸ��� �ʽ��ϴ�.");
			alert.setContentText("�н����带 �ٽ� �Է��ϼ���");
			alert.showAndWait();
		}

	}

	// ���̵� �ߺ�Ȯ��
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
				alert.setTitle("���̵� �ߺ��˻�");
				alert.setHeaderText("����Ҽ� �ִ� ���̵� �Դϴ�.");
				alert.setContentText("���̵� ����ϼ���.");
				alert.showAndWait();
			} else {
				txtId.clear();
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("���̵� �ߺ��˻�");
				alert.setHeaderText(searchid + "�� ����Ҽ� �����ϴ�.");
				alert.setContentText("�ٸ� ���̵� ����� �ּ���.");
				alert.showAndWait();
			}

		} catch (Exception e) {
			System.out.println("e={" + e + "}");
		}
	}
}
