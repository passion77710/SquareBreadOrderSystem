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
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.EmployeeVO;

public class EmployeeController implements Initializable {
	@FXML
	private TableView<EmployeeVO> tableView = new TableView<>();
	@FXML
	private TextField txtj_Name;
	@FXML
	private ToggleGroup genderGroup;
	@FXML
	private RadioButton rbMale;
	@FXML
	private RadioButton rbFemale;
	@FXML
	private TextField txtj_Age;
	@FXML
	private TextField txtj_Phone;
	@FXML
	private TextField txtj_Addr;
	@FXML
	private DatePicker dpj_Day;
	@FXML
	private DatePicker dpj_DeleteDay;
	@FXML
	private TextField txtSearch;
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

	EmployeeVO employeeVO = new EmployeeVO();
	ObservableList<EmployeeVO> data = FXCollections.observableArrayList();
	ObservableList<EmployeeVO> selectEmployee; // ���̺��� ������ ���� ����
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
		dpj_Day.setValue(LocalDate.now());
		dpj_DeleteDay.setValue(LocalDate.of(9999, 01, 01));

		//
		tableView.setEditable(false);
		
		// ���� ���̺� �÷����
		TableColumn colNo = new TableColumn("NO");
		colNo.setMaxWidth(40);
		colNo.setStyle("-fx-allignment: CENTER");
		colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
		TableColumn colj_Name = new TableColumn(" ���� ");
		colj_Name.setMaxWidth(50);
		colj_Name.setCellValueFactory(new PropertyValueFactory<>("j_name"));
		TableColumn colGender = new TableColumn(" ���� ");
		colGender.setMaxWidth(50);
		colGender.setCellValueFactory(new PropertyValueFactory<>("j_gender"));
		TableColumn colj_Age = new TableColumn(" ���� ");
		colj_Age.setMaxWidth(50);
		colj_Age.setCellValueFactory(new PropertyValueFactory<>("j_age"));
		TableColumn colj_Phone = new TableColumn(" �ڵ�����ȣ ");
		colj_Phone.setMaxWidth(200);
		colj_Phone.setCellValueFactory(new PropertyValueFactory<>("j_phone"));
		TableColumn colj_Addr = new TableColumn(" �ּ� ");
		colj_Addr.setMaxWidth(500);
		colj_Addr.setCellValueFactory(new PropertyValueFactory<>("j_addr"));
		TableColumn colj_Day = new TableColumn(" �Ի��� ");
		colj_Day.setMaxWidth(600);
		colj_Day.setCellValueFactory(new PropertyValueFactory<>("j_day"));
		TableColumn colj_DeleteDay = new TableColumn(" ����� ");
		colj_DeleteDay.setMaxWidth(600);
		colj_DeleteDay.setCellValueFactory(new PropertyValueFactory<>("j_deleteday"));

		tableView.getColumns().addAll(colNo, colj_Name, colGender, colj_Age, colj_Phone, colj_Addr, colj_Day,
				colj_DeleteDay);

		// ���� ��ü ����
		totalList();
		tableView.setItems(data);

		// ��ü ����Ʈ
		btnTotalList.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					data.removeAll(data);
					// ���� ��ü ����
					totalList();
				} catch (Exception e) {
				}
			}
		});

		// ���� ���
		btnOk.setOnAction(event -> {
			try {
				data.removeAll(data);
				EmployeeVO evo = null;
				EmployeeDAO edao = null;

				if (event.getSource().equals(btnOk)) {

					evo = new EmployeeVO(txtj_Name.getText(), genderGroup.getSelectedToggle().getUserData().toString(),
							txtj_Age.getText(), txtj_Phone.getText(), txtj_Addr.getText(),
							dpj_Day.getValue().toString(), dpj_DeleteDay.getValue().toString());

					if(dpj_DeleteDay.getValue().equals(LocalDate.of(9999, 01, 01))) {
						evo.setJ_deleteday("-");
					}
					
					edao = new EmployeeDAO();

					edao.getEmployeeRegiste(evo);

					if (edao != null) {

						totalList();

						txtj_Name.setEditable(true);
						txtj_Age.setEditable(true);
						txtj_Phone.setEditable(true);
						txtj_Addr.setEditable(true);
						handlerBtnInitAction(event);
					}
				}
				
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("���� ���� �Է�");
				alert.setHeaderText("���� ������ ��Ȯ�� �Է��Ͻÿ�.");
				alert.setContentText("�������� �����ϼ���!");
				alert.showAndWait();
			}
		});

		btnEdit.setOnAction(event -> handlerBtnEditAction(event)); // ��������
		btnDelete.setOnAction(event -> handlerBtnDeleteAction(event)); // ��������
		btnSearch.setOnAction(event -> handlerBtnSearchAction(event)); // �˻� �̺�Ʈ
		btnInit.setOnAction(event -> handlerBtnInitAction(event)); // �ʱ�ȭ
		btnCancel.setOnAction(event -> handlerBtnCancelAction(event)); // �ݱ�
		tableView.setOnMouseClicked(event -> handlerBtntableViewAction(event)); // ���̺��� ��������

		// ���̺� �̺�Ʈó��
		tableView.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				selectEmployee = tableView.getSelectionModel().getSelectedItems();
				selectedIndex = tableView.getSelectionModel().getSelectedIndex();

				try {
					txtj_Name.setText(selectEmployee.get(0).getJ_name());
					if (selectEmployee.get(0).getJ_gender().equals("����")) {
						rbMale.setSelected(true);
						rbFemale.setDisable(true);
					} else {
						rbFemale.setSelected(true);
						rbMale.setDisable(true);
					}

					txtj_Phone.setText(selectEmployee.get(0).getJ_phone());
					txtj_Addr.setText(selectEmployee.get(0).getJ_addr());
					txtj_Age.setText(selectEmployee.get(0).getJ_age());

					txtj_Name.setEditable(false);
					btnDelete.setDisable(false);
					btnOk.setDisable(true);
					btnCancel.setDisable(true);

					editDelete = true;
				} catch (Exception e) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("���� ���� ���� ����");
					alert.setHeaderText("���� ������ �Է��Ͻÿ�.");
					alert.setContentText("�������� �����ϼ���!");
					alert.showAndWait();
				}
			}
		});
	}

	// ���̺��� ��������
	public void handlerBtntableViewAction(MouseEvent event) {
		try {
			dpj_DeleteDay.setValue(LocalDate.now());
			btnEdit.setDisable(false);
			btnDelete.setDisable(false);
		} catch (Exception e) {
		}
	}

	// �ʱ�ȭ �޼ҵ�
	public void handlerBtnInitAction(ActionEvent event) {
		init();
	}

	// �ʱ�ȭ ��ư
	public void init() {
		txtj_Name.clear();
		txtj_Name.setEditable(true);
		rbFemale.setDisable(false);
		rbMale.setDisable(false);
		genderGroup.selectToggle(null);
		txtj_Age.clear();
		txtj_Phone.clear();
		txtj_Addr.clear();
		dpj_DeleteDay.setValue(LocalDate.of(9999, 01, 01));
		btnOk.setDisable(false);
		btnCancel.setDisable(false);
		btnEdit.setDisable(true);
		btnDelete.setDisable(true);
	}

	// ���� ��ü ����Ʈ
	public void totalList() {

		Object[][] totalData;

		EmployeeDAO eDao = new EmployeeDAO();
		EmployeeVO eVo = new EmployeeVO();
		ArrayList<String> title;

		ArrayList<EmployeeVO> list;

		title = eDao.getColumnName();
		int columnCount = title.size();

		list = eDao.getEmployeeTotal();
		int rowCount = list.size();

		totalData = new Object[rowCount][columnCount];

		for (int index = 0; index < rowCount; index++) {

			eVo = list.get(index);
			data.add(eVo);

		}
	}

	// ��������
	public void handlerBtnEditAction(ActionEvent event) {
		EmployeeVO evo = null;
		EmployeeDAO edao = null;
		try {
			evo = new EmployeeVO(tableView.getSelectionModel().getSelectedIndex(), txtj_Name.getText(),
					genderGroup.getSelectedToggle().getUserData().toString(), txtj_Age.getText(), txtj_Addr.getText(),
					txtj_Phone.getText(), dpj_Day.getValue().toString(), dpj_DeleteDay.getValue().toString());

			edao = new EmployeeDAO();

			edao.getEmployeeUpdate(evo, tableView.getSelectionModel().getSelectedItem().getNo());

			data.removeAll(data);
			totalList();

			txtj_Age.setEditable(true);
			txtj_Phone.setEditable(true);
			txtj_Addr.setEditable(true);
			handlerBtnInitAction(event);

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���� ���� ����");
			alert.setHeaderText("�����Ǵ� ���� ������ ��Ȯ�� �Է��Ͻÿ�.");
			alert.setContentText("�������� �����ϼ���!");
			alert.showAndWait();
		}
	}

	// ���� ����
	public void handlerBtnDeleteAction(ActionEvent event) {

		EmployeeDAO eDao = null;
		eDao = new EmployeeDAO();

		try {
			eDao.getEmployeeDelete(tableView.getSelectionModel().getSelectedItem().getNo());
			data.removeAll(data); // ���� ��ü ����
			totalList();
			handlerBtnInitAction(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
		editDelete = true;
	}

	// �˻� �̺�Ʈ ó�� �޼ҵ�
	public void handlerBtnSearchAction(ActionEvent event) {
		for (EmployeeVO list : data) {
			if ((list.getJ_name().equals(txtSearch.getText()))) {
				tableView.getSelectionModel().select(list);
			}
		}
	}

	// �ݱ�
	private void handlerBtnCancelAction(ActionEvent event) {
		try {
			Pane root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
			Scene scene = new Scene(root);
			Stage passionStage = new Stage();
			passionStage.setTitle("�׸�Ļ��ֹ��ý���");
			passionStage.setScene(scene);
			Stage oldStage = (Stage) btnCancel.getScene().getWindow();
			passionStage.show();
			oldStage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
