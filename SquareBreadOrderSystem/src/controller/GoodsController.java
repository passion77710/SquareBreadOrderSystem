package controller;

import java.net.URL;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.GoodsVO;

public class GoodsController implements Initializable {
	@FXML
	private TableView<GoodsVO> tableView;
	@FXML
	private ComboBox<String> cbGoods;
	@FXML
	private TextField txtg_unit; // �ܰ�
	@FXML
	private TextField txtg_count; // ����
	@FXML
	private TextField txtg_price; // �Ѿ�
	@FXML
	private TextField txtSearch;
	@FXML
	private TextField txtAllg_count;
	@FXML
	private TextField txtAllg_price;
	@FXML
	private DatePicker dpg_day;
	@FXML
	private Button btnSearch;
	@FXML
	private Button btnTotalList;
	@FXML
	private Button btnOk;
	@FXML
	private Button btnEdit;
	@FXML
	private Button btnDelete;
	@FXML
	private Button btnCancel;
	@FXML
	private Button btnInit;
	@FXML
	private Button btnGyesan;
	@FXML
	private Button btnTotal;

	GoodsVO goodsVO = new GoodsVO();
	ObservableList<GoodsVO> data = FXCollections.observableArrayList(); //
	ObservableList<GoodsVO> selectGoods; // ���̺��� ������ ���� ����
	boolean editDelete = false; // ������ �� Ȯ�� ��ư ���� ����
	int selectedIndex; // ���̺��� ������ �л� ���� �ε��� ����
	int no; // ������ ���̺��� ������ �л��� ��ȣ ����

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// ��ư �ʱ�ȭ
		btnOk.setDisable(false);
		btnEdit.setDisable(true);
		btnDelete.setDisable(true);
		btnCancel.setDisable(false);
		dpg_day.setValue(LocalDate.now());

		cbGoods.setItems(
				FXCollections.observableArrayList("��", "����ġ��", "��ũ��", "�÷���", "����", "����", "��ũ��", "���ýó���", "ũ������", "��纣��",
						"���ýĻ�", "������", "���ڿ���500ml", "�ٳ�������500ml", "Ŀ�ǿ���500ml", "�������500ml", "�����500ml", "�����1000ml"));

		//
		tableView.setEditable(false);

		// ��ǰ ���̺� �÷����
		TableColumn colNo = new TableColumn("NO");
		colNo.setMaxWidth(40);
		colNo.setStyle("-fx-allignment: CENTER");
		colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
		TableColumn colg_Name = new TableColumn(" ��ǰ�� ");
		colg_Name.setMaxWidth(300);
		colg_Name.setCellValueFactory(new PropertyValueFactory<>("g_name"));
		TableColumn colg_Unit = new TableColumn(" �ܰ� ");
		colg_Unit.setMaxWidth(50);
		colg_Unit.setCellValueFactory(new PropertyValueFactory<>("g_unit"));
		TableColumn colg_Count = new TableColumn(" ���� ");
		colg_Count.setMaxWidth(50);
		colg_Count.setCellValueFactory(new PropertyValueFactory<>("g_count"));
		TableColumn colg_Price = new TableColumn(" �ݾ� ");
		colg_Price.setMaxWidth(100);
		colg_Price.setCellValueFactory(new PropertyValueFactory<>("g_price"));
		TableColumn colg_Day = new TableColumn(" �ֹ��� ");
		colg_Day.setMaxWidth(200);
		colg_Day.setCellValueFactory(new PropertyValueFactory<>("g_day"));

		tableView.getColumns().addAll(colNo, colg_Name, colg_Unit, colg_Count, colg_Price, colg_Day);

		// ��ǰ ��ü ����
		totalList();
		tableView.setItems(data);

		// ��ü ����Ʈ
		btnTotalList.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					data.removeAll(data);
					// ��ǰ ��ü ����
					totalList();
				} catch (Exception e) {
				}
			}
		});

		// ��ǰ ���
		btnOk.setOnAction(event -> {
			try {
				data.removeAll(data);
				GoodsVO gvo = null;
				GoodsDAO gdao = null;

				if (event.getSource().equals(btnOk)) {

					gvo = new GoodsVO(cbGoods.getSelectionModel().getSelectedItem(), txtg_unit.getText(),
							txtg_count.getText(), txtg_price.getText(), dpg_day.getValue().toString());

					gdao = new GoodsDAO();
					gdao.getGoodsRegiste(gvo);

					if (gdao != null) {

						totalList();

						txtg_unit.setEditable(true);
						txtg_count.setEditable(true);
						txtg_price.setEditable(true);
						handlerBtnInitAction(event);
					}
				}
			}catch (NullPointerException e) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("��ǰ ���� �Է�");
				alert.setHeaderText("��ǰ ������ �Է��Ͻÿ�.");
				alert.setContentText("�������� �����ϼ���!");
				alert.showAndWait();
				
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("��ǰ ���� �Է�");
				alert.setHeaderText("��ǰ ������ ��Ȯ�� �Է��Ͻÿ�.");
				alert.setContentText("�������� �����ϼ���!");
				alert.showAndWait();
			}
		});

		btnGyesan.setOnAction(event -> handlerbtngyesanAction(event)); // �ݾ� ��ư �޼ҵ�
		cbGoods.setOnAction(event -> handlerGoodsAction(event)); // �޺��ڽ� �޼ҵ� ����
		btnEdit.setOnAction(event -> handlerBtnEditAction(event)); // ��������
		btnDelete.setOnAction(event -> handlerBtnDeleteAction(event)); // ��������
		btnSearch.setOnAction(event -> handlerBtnSearchAction(event)); // �˻� �̺�Ʈ
		btnInit.setOnAction(event -> handlerBtnInitAction(event)); // �ʱ�ȭ
		btnCancel.setOnAction(event -> handlerBtnCancelAction(event)); // �ݱ�
		btnTotal.setOnAction(event -> handlerBtnTotalAction(event)); // �Ѽ��� �� �ݾ׹�ư �޼ҵ�
		tableView.setOnMouseClicked(event -> handlerBtntableViewAction(event)); // ���̺��� ��ǰ����

		// ���̺� �̺�Ʈó��
		tableView.setOnMousePressed(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent me) {
				selectGoods = tableView.getSelectionModel().getSelectedItems();
				selectedIndex = tableView.getSelectionModel().getSelectedIndex();

				try {
					txtg_unit.setText(selectGoods.get(0).getG_unit());
					txtg_count.setText(selectGoods.get(0).getG_count());
					txtg_price.setText(selectGoods.get(0).getG_price());

					btnDelete.setDisable(false);
					btnOk.setDisable(true);
					btnCancel.setDisable(true);

					editDelete = true;
				} catch (Exception e) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("��ǰ ���� ���� ����");
					alert.setHeaderText("��ǰ ������ �Է��Ͻÿ�.");
					alert.setContentText("�������� �����ϼ���!");
					alert.showAndWait();
				}
			}

		});
	}

	// ���̺��� ��ǰ����
	public void handlerBtntableViewAction(MouseEvent event) {
		try {
			btnEdit.setDisable(false);
			btnDelete.setDisable(false);
		} catch (Exception e) {
		}
	}

	// �޺��ڽ� �޼ҵ�
	public void handlerGoodsAction(ActionEvent event) {
		if (cbGoods.getSelectionModel().getSelectedItem() == null) {
			txtg_unit.setText("0");
		} else if (cbGoods.getValue().toString().equals("��") || cbGoods.getValue().toString() == "����ġ��"
				|| cbGoods.getValue().toString() == "��ũ��" || cbGoods.getValue().toString() == "�÷���"
				|| cbGoods.getValue().toString() == "����" || cbGoods.getValue().toString() == "����"
				|| cbGoods.getValue().toString() == "��ũ��" || cbGoods.getValue().toString() == "���ýó���"
				|| cbGoods.getValue().toString() == "ũ������" || cbGoods.getValue().toString() == "��纣��"
				|| cbGoods.getValue().toString() == "���ýĻ�" || cbGoods.getValue().toString() == "������") {
			txtg_unit.setText("2900");
		} else if (cbGoods.getValue().toString() == "���ڿ���500ml" || cbGoods.getValue().toString() == "�ٳ�������500ml"
				|| cbGoods.getValue().toString() == "Ŀ�ǿ���500ml" || cbGoods.getValue().toString() == "�����500ml") {
			txtg_unit.setText("1400");
		} else if (cbGoods.getValue().toString() == "�����1000ml") {
			txtg_unit.setText("2800");
		}
	}

	// ��� ��ư �޼ҵ�
	public void handlerbtngyesanAction(ActionEvent event) {
		try {
			int unit = Integer.parseInt(txtg_unit.getText().trim());
			int count = Integer.parseInt(txtg_count.getText().trim());

			int multy;

			multy = unit * count;
			txtg_price.setText(multy + "");

		} catch (Exception e) {
		}

	}

	// �Ѽ��� �� �ݾ׹�ư �޼ҵ�
	public void handlerBtnTotalAction(ActionEvent event) {
		GoodsDAO mDao = new GoodsDAO();
		int sum[] = mDao.setTextCalculation(dpg_day.getValue().toString().substring(2, 4) + "/" + dpg_day.getValue().toString().substring(5, 7));
		txtAllg_count.setText(sum[0] + "");
		txtAllg_price.setText(sum[1] + "");
		
		// ��ǰ���� �Ǹŷ� �ѱ��
		SalesController.count = txtAllg_count.getText();
		SalesController.price = txtAllg_price.getText();
		
	}

	// ��ǰ ��ü ����Ʈ �޼ҵ�
	public void totalList() {
		Object[][] totalData;

		GoodsDAO gDao = new GoodsDAO();
		GoodsVO gVo = new GoodsVO();
		ArrayList<String> title;

		ArrayList<GoodsVO> list;

		title = gDao.getColumnName();
		int columnCount = title.size();

		list = gDao.getEmployeeTotal();
		int rowCount = list.size();

		totalData = new Object[rowCount][columnCount];

		for (int index = 0; index < rowCount; index++) {

			gVo = list.get(index);
			data.add(gVo);

		}
	}

	// ��ǰ ���� �޼ҵ�
	public void handlerBtnEditAction(ActionEvent event) {
		GoodsVO gvo = null;
		GoodsDAO gdao = null;
		try {
			gvo = new GoodsVO(tableView.getSelectionModel().getSelectedIndex(),
					cbGoods.getSelectionModel().getSelectedItem(), txtg_unit.getText(), txtg_count.getText(),
					txtg_price.getText(), dpg_day.getValue().toString());
			System.out.println(cbGoods);
			System.out.println(txtg_unit);
			System.out.println(txtg_count);

			gdao = new GoodsDAO();

			gdao.getGoodsUpdate(gvo, tableView.getSelectionModel().getSelectedItem().getNo());

			data.removeAll(data);
			totalList();

			cbGoods.setDisable(true);
			txtg_unit.setEditable(true);
			txtg_count.setEditable(true);
			txtg_price.setEditable(true);

		} catch (Exception e) {
			System.out.println(e);
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("��ǰ ���� ����");
			alert.setHeaderText("�����Ǵ� ��ǰ ������ ��Ȯ�� �Է��Ͻÿ�.");
			alert.setContentText("�������� �����ϼ���!");
			alert.showAndWait();
		}
	}

	// ��ǰ ���� �޼ҵ�
	public void handlerBtnDeleteAction(ActionEvent event) {
		GoodsDAO eDao = null;
		eDao = new GoodsDAO();

		try {
			eDao.getGoodsDelete(tableView.getSelectionModel().getSelectedItem().getNo());
			data.removeAll(data); // ��ǰ ��ü ����
			totalList();
			handlerBtnInitAction(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
		editDelete = true;
	}

	// ��ǰ �˻� �޼ҵ�
	public void handlerBtnSearchAction(ActionEvent event) {
		for (GoodsVO list : data) {
			if ((list.getG_name().equals(txtSearch.getText()))) {
				tableView.getSelectionModel().select(list);
			}
		}
	}

	// �ʱ�ȭ �޼ҵ�
	public void handlerBtnInitAction(ActionEvent event) {
		try {
			cbGoods.getSelectionModel().select(null);
			txtg_count.clear();
			txtg_price.clear();
			btnOk.setDisable(false);
			btnCancel.setDisable(false);
			btnEdit.setDisable(true);
			btnDelete.setDisable(true);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// �ݱ� �޼ҵ�
	private void handlerBtnCancelAction(ActionEvent event) {
		try {
			Pane root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
			Scene scene = new Scene(root);
			Stage passionStage = new Stage();
			passionStage.setTitle("�׸�Ļ� �ֹ��ý���");
			passionStage.setScene(scene);
			Stage oldStage = (Stage) btnCancel.getScene().getWindow();
			passionStage.show();
			oldStage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
