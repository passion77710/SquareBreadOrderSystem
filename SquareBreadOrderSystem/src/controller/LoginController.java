package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LoginController implements Initializable {
	@FXML
	private TextField txtId;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private Button btnCancel;
	@FXML
	private Button btnLogin;
	@FXML
	private Button btnOk;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnLogin.setOnAction(event -> handlerBtnLoginAction(event));
		btnCancel.setOnAction(event -> handlerBtnCancelAction(event));
		btnOk.setOnAction(evet -> handlerBtnOkAction(evet));

		txtPassword.setOnKeyPressed(event -> handlerTextPasswordKeyPressed(event));
		txtId.setOnKeyPressed(event -> handlerTextIdKeyPressed(event));

	}

	public void handlerBtnOkAction(ActionEvent evet) {
		try {
			Pane root = FXMLLoader.load(getClass().getResource("/view/join.fxml"));
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
	}

	public void handlerTextIdKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			txtPassword.requestFocus();
		}
	}

	public void handlerTextPasswordKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			login();
		}
	}
	
	private void login() {
		
		LoginDAO login = new LoginDAO();
		boolean sucess = false;
		try {
			sucess = login.getLogin(txtId.getText().trim(), txtPassword.getText().trim());
		} catch (Exception e1) {
			e1.printStackTrace();
			System.out.println("���Ӽ���");
		}
		Alert alert;
		if (txtId.getText().equals("") || txtPassword.getText().equals("")) {
			alert = new Alert(AlertType.WARNING);
			alert.setTitle("�α��ν���");
			alert.setHeaderText("���̵�� ��й�ȣ ���Է�");
			alert.setContentText("���̵�� ��й�ȣ�� �Է����� ���� �׸��� �ֽ��ϴ�." + "\n �ٽ� ����� �Է��Ͻÿ�.");
			alert.setResizable(false);
			alert.showAndWait();
		}

		// �α��� ������ ���� �������� �̵�
		if (sucess) {
			try
			{
				System.out.println("���Ӽ���");
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
				Parent mainView = (Parent) loader.load();
				Scene scane = new Scene(mainView);
				Stage mainMtage = new Stage();
				mainMtage.setTitle("�׸�Ļ� �ֹ��ý���");
				mainMtage.setResizable(false);
				mainMtage.setScene(scane);
				Stage oldStage = (Stage) btnLogin.getScene().getWindow();
				oldStage.close();
				mainMtage.show();
			} catch (IOException e) {
				System.err.println(" ���� " + e);
			}
		} else {
			// ���̵��н����� Ȯ���϶�� â
			alert = new Alert(AlertType.WARNING);
			alert.setTitle(" �α��� ���� ");
			alert.setHeaderText(" ���̵�� ��й�ȣ ����ġ ");
			alert.setContentText("���̵�� ��й�ȣ�� ��ġ���� �ʽ��ϴ�." + "\n �ٽ� ����� �Է��Ͻÿ�.");
			alert.setResizable(false);
			alert.showAndWait();
			txtId.clear();
			txtPassword.clear();
			System.out.println("���Ӽ���");
		}
	}

	private void handlerBtnCancelAction(ActionEvent event) {
		Platform.exit();
	}

	private void handlerBtnLoginAction(ActionEvent event) {
			login();
			System.out.println("���Ӽ���");
		
		
	}

}
