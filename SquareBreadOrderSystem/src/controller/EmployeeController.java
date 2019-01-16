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
	ObservableList<EmployeeVO> selectEmployee; // 테이블에서 선택한 정보 저장
	boolean editDelete = false; // 수정할 때 확인 버튼 상태 설정
	int selectedIndex; // 테이블에서 선택한 학생 정보 인덱스 저장
	int no; // 삭제시 테이블에서 선택한 학생의 번호 저장

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// 버튼 초기화
		btnOk.setDisable(false);
		btnEdit.setDisable(true);
		btnDelete.setDisable(true);
		btnCancel.setDisable(false);
		dpj_Day.setValue(LocalDate.now());
		dpj_DeleteDay.setValue(LocalDate.of(9999, 01, 01));

		//
		tableView.setEditable(false);
		
		// 직원 테이블에 컬럼등록
		TableColumn colNo = new TableColumn("NO");
		colNo.setMaxWidth(40);
		colNo.setStyle("-fx-allignment: CENTER");
		colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
		TableColumn colj_Name = new TableColumn(" 성명 ");
		colj_Name.setMaxWidth(50);
		colj_Name.setCellValueFactory(new PropertyValueFactory<>("j_name"));
		TableColumn colGender = new TableColumn(" 성별 ");
		colGender.setMaxWidth(50);
		colGender.setCellValueFactory(new PropertyValueFactory<>("j_gender"));
		TableColumn colj_Age = new TableColumn(" 나이 ");
		colj_Age.setMaxWidth(50);
		colj_Age.setCellValueFactory(new PropertyValueFactory<>("j_age"));
		TableColumn colj_Phone = new TableColumn(" 핸드폰번호 ");
		colj_Phone.setMaxWidth(200);
		colj_Phone.setCellValueFactory(new PropertyValueFactory<>("j_phone"));
		TableColumn colj_Addr = new TableColumn(" 주소 ");
		colj_Addr.setMaxWidth(500);
		colj_Addr.setCellValueFactory(new PropertyValueFactory<>("j_addr"));
		TableColumn colj_Day = new TableColumn(" 입사일 ");
		colj_Day.setMaxWidth(600);
		colj_Day.setCellValueFactory(new PropertyValueFactory<>("j_day"));
		TableColumn colj_DeleteDay = new TableColumn(" 퇴사일 ");
		colj_DeleteDay.setMaxWidth(600);
		colj_DeleteDay.setCellValueFactory(new PropertyValueFactory<>("j_deleteday"));

		tableView.getColumns().addAll(colNo, colj_Name, colGender, colj_Age, colj_Phone, colj_Addr, colj_Day,
				colj_DeleteDay);

		// 직원 전체 정보
		totalList();
		tableView.setItems(data);

		// 전체 리스트
		btnTotalList.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					data.removeAll(data);
					// 직원 전체 정보
					totalList();
				} catch (Exception e) {
				}
			}
		});

		// 직원 등록
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
				alert.setTitle("직원 정보 입력");
				alert.setHeaderText("직원 정보를 정확히 입력하시오.");
				alert.setContentText("다음에는 주의하세요!");
				alert.showAndWait();
			}
		});

		btnEdit.setOnAction(event -> handlerBtnEditAction(event)); // 직원수정
		btnDelete.setOnAction(event -> handlerBtnDeleteAction(event)); // 직원삭제
		btnSearch.setOnAction(event -> handlerBtnSearchAction(event)); // 검색 이벤트
		btnInit.setOnAction(event -> handlerBtnInitAction(event)); // 초기화
		btnCancel.setOnAction(event -> handlerBtnCancelAction(event)); // 닫기
		tableView.setOnMouseClicked(event -> handlerBtntableViewAction(event)); // 테이블의 직원선택

		// 테이블 이벤트처리
		tableView.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				selectEmployee = tableView.getSelectionModel().getSelectedItems();
				selectedIndex = tableView.getSelectionModel().getSelectedIndex();

				try {
					txtj_Name.setText(selectEmployee.get(0).getJ_name());
					if (selectEmployee.get(0).getJ_gender().equals("남성")) {
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
					alert.setTitle("직원 정보 수정 삭제");
					alert.setHeaderText("직원 정보를 입력하시오.");
					alert.setContentText("다음에는 주의하세요!");
					alert.showAndWait();
				}
			}
		});
	}

	// 테이블의 직원선택
	public void handlerBtntableViewAction(MouseEvent event) {
		try {
			dpj_DeleteDay.setValue(LocalDate.now());
			btnEdit.setDisable(false);
			btnDelete.setDisable(false);
		} catch (Exception e) {
		}
	}

	// 초기화 메소드
	public void handlerBtnInitAction(ActionEvent event) {
		init();
	}

	// 초기화 버튼
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

	// 직원 전체 리스트
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

	// 직원수정
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
			alert.setTitle("직원 정보 수정");
			alert.setHeaderText("수정되는 직원 정보를 정확히 입력하시오.");
			alert.setContentText("다음에는 주의하세요!");
			alert.showAndWait();
		}
	}

	// 직원 삭제
	public void handlerBtnDeleteAction(ActionEvent event) {

		EmployeeDAO eDao = null;
		eDao = new EmployeeDAO();

		try {
			eDao.getEmployeeDelete(tableView.getSelectionModel().getSelectedItem().getNo());
			data.removeAll(data); // 직원 전체 정보
			totalList();
			handlerBtnInitAction(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
		editDelete = true;
	}

	// 검색 이벤트 처리 메소드
	public void handlerBtnSearchAction(ActionEvent event) {
		for (EmployeeVO list : data) {
			if ((list.getJ_name().equals(txtSearch.getText()))) {
				tableView.getSelectionModel().select(list);
			}
		}
	}

	// 닫기
	private void handlerBtnCancelAction(ActionEvent event) {
		try {
			Pane root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
			Scene scene = new Scene(root);
			Stage passionStage = new Stage();
			passionStage.setTitle("네모식빵주문시스템");
			passionStage.setScene(scene);
			Stage oldStage = (Stage) btnCancel.getScene().getWindow();
			passionStage.show();
			oldStage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
