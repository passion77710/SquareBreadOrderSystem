package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class RootController implements Initializable {
	@FXML
	private Button btnEmployee;
	@FXML
	private Button btnGoods;
	@FXML
	private Button btnSales;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		btnEmployee.setOnAction(event -> handlerBtnEmployeeAction(event));
		btnGoods.setOnAction(event -> handlerBtnGoodsAction(event));
		btnSales.setOnAction(event -> handlerBtnSalesAction(event));
	}

	public void handlerBtnEmployeeAction(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/employee.fxml"));
			Scene scene = new Scene(root);
			Stage passionStage = new Stage();

			passionStage.setTitle("流盔包府");
			passionStage.setScene(scene);
			passionStage.setResizable(false);
			Stage oldStage = (Stage) btnEmployee.getScene().getWindow();
			oldStage.close();
			passionStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handlerBtnGoodsAction(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/goods.fxml"));
			Scene scene = new Scene(root);
			Stage passionStage = new Stage();

			passionStage.setTitle("惑前包府");
			passionStage.setScene(scene);
			passionStage.setResizable(false);
			Stage oldStage = (Stage) btnGoods.getScene().getWindow();
			oldStage.close();
			passionStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handlerBtnSalesAction(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/sales.fxml"));
			Scene scene = new Scene(root);
			Stage passionStage = new Stage();

			passionStage.setTitle("概免泅炔包府");
			passionStage.setScene(scene);
			passionStage.setResizable(false);
			Stage oldStage = (Stage) btnSales.getScene().getWindow();
			oldStage.close();
			passionStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
