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
	private TextField txtg_unit; // 단가
	@FXML
	private TextField txtg_count; // 수량
	@FXML
	private TextField txtg_price; // 총액
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
	ObservableList<GoodsVO> selectGoods; // 테이블에서 선택한 정보 저장
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
		dpg_day.setValue(LocalDate.now());

		cbGoods.setItems(
				FXCollections.observableArrayList("밤", "더블치즈", "생크림", "플레인", "초코", "단팥", "슈크림", "애플시나몬", "크렌베리", "블루베리",
						"마늘식빵", "뉴델라", "초코우유500ml", "바나나우유500ml", "커피우유500ml", "딸기우유500ml", "흰우유500ml", "흰우유1000ml"));

		//
		tableView.setEditable(false);

		// 상품 테이블에 컬럼등록
		TableColumn colNo = new TableColumn("NO");
		colNo.setMaxWidth(40);
		colNo.setStyle("-fx-allignment: CENTER");
		colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
		TableColumn colg_Name = new TableColumn(" 상품명 ");
		colg_Name.setMaxWidth(300);
		colg_Name.setCellValueFactory(new PropertyValueFactory<>("g_name"));
		TableColumn colg_Unit = new TableColumn(" 단가 ");
		colg_Unit.setMaxWidth(50);
		colg_Unit.setCellValueFactory(new PropertyValueFactory<>("g_unit"));
		TableColumn colg_Count = new TableColumn(" 수량 ");
		colg_Count.setMaxWidth(50);
		colg_Count.setCellValueFactory(new PropertyValueFactory<>("g_count"));
		TableColumn colg_Price = new TableColumn(" 금액 ");
		colg_Price.setMaxWidth(100);
		colg_Price.setCellValueFactory(new PropertyValueFactory<>("g_price"));
		TableColumn colg_Day = new TableColumn(" 주문일 ");
		colg_Day.setMaxWidth(200);
		colg_Day.setCellValueFactory(new PropertyValueFactory<>("g_day"));

		tableView.getColumns().addAll(colNo, colg_Name, colg_Unit, colg_Count, colg_Price, colg_Day);

		// 상품 전체 정보
		totalList();
		tableView.setItems(data);

		// 전체 리스트
		btnTotalList.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					data.removeAll(data);
					// 상품 전체 정보
					totalList();
				} catch (Exception e) {
				}
			}
		});

		// 상품 등록
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
				alert.setTitle("상품 정보 입력");
				alert.setHeaderText("상품 정보를 입력하시오.");
				alert.setContentText("다음에는 주의하세요!");
				alert.showAndWait();
				
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("상품 정보 입력");
				alert.setHeaderText("상품 정보를 정확히 입력하시오.");
				alert.setContentText("다음에는 주의하세요!");
				alert.showAndWait();
			}
		});

		btnGyesan.setOnAction(event -> handlerbtngyesanAction(event)); // 금액 버튼 메소드
		cbGoods.setOnAction(event -> handlerGoodsAction(event)); // 콤보박스 메소드 생성
		btnEdit.setOnAction(event -> handlerBtnEditAction(event)); // 직원수정
		btnDelete.setOnAction(event -> handlerBtnDeleteAction(event)); // 직원삭제
		btnSearch.setOnAction(event -> handlerBtnSearchAction(event)); // 검색 이벤트
		btnInit.setOnAction(event -> handlerBtnInitAction(event)); // 초기화
		btnCancel.setOnAction(event -> handlerBtnCancelAction(event)); // 닫기
		btnTotal.setOnAction(event -> handlerBtnTotalAction(event)); // 총수량 총 금액버튼 메소드
		tableView.setOnMouseClicked(event -> handlerBtntableViewAction(event)); // 테이블의 상품선택

		// 테이블 이벤트처리
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
					alert.setTitle("상품 정보 수정 삭제");
					alert.setHeaderText("상품 정보를 입력하시오.");
					alert.setContentText("다음에는 주의하세요!");
					alert.showAndWait();
				}
			}

		});
	}

	// 테이블의 상품선택
	public void handlerBtntableViewAction(MouseEvent event) {
		try {
			btnEdit.setDisable(false);
			btnDelete.setDisable(false);
		} catch (Exception e) {
		}
	}

	// 콤보박스 메소드
	public void handlerGoodsAction(ActionEvent event) {
		if (cbGoods.getSelectionModel().getSelectedItem() == null) {
			txtg_unit.setText("0");
		} else if (cbGoods.getValue().toString().equals("밤") || cbGoods.getValue().toString() == "더블치즈"
				|| cbGoods.getValue().toString() == "생크림" || cbGoods.getValue().toString() == "플레인"
				|| cbGoods.getValue().toString() == "초코" || cbGoods.getValue().toString() == "단팥"
				|| cbGoods.getValue().toString() == "슈크림" || cbGoods.getValue().toString() == "애플시나몬"
				|| cbGoods.getValue().toString() == "크렌베리" || cbGoods.getValue().toString() == "블루베리"
				|| cbGoods.getValue().toString() == "마늘식빵" || cbGoods.getValue().toString() == "뉴델라") {
			txtg_unit.setText("2900");
		} else if (cbGoods.getValue().toString() == "초코우유500ml" || cbGoods.getValue().toString() == "바나나우유500ml"
				|| cbGoods.getValue().toString() == "커피우유500ml" || cbGoods.getValue().toString() == "흰우유500ml") {
			txtg_unit.setText("1400");
		} else if (cbGoods.getValue().toString() == "흰우유1000ml") {
			txtg_unit.setText("2800");
		}
	}

	// 계산 버튼 메소드
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

	// 총수량 총 금액버튼 메소드
	public void handlerBtnTotalAction(ActionEvent event) {
		GoodsDAO mDao = new GoodsDAO();
		int sum[] = mDao.setTextCalculation(dpg_day.getValue().toString().substring(2, 4) + "/" + dpg_day.getValue().toString().substring(5, 7));
		txtAllg_count.setText(sum[0] + "");
		txtAllg_price.setText(sum[1] + "");
		
		// 상품에서 판매로 넘기기
		SalesController.count = txtAllg_count.getText();
		SalesController.price = txtAllg_price.getText();
		
	}

	// 상품 전체 리스트 메소드
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

	// 상품 수정 메소드
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
			alert.setTitle("상품 정보 수정");
			alert.setHeaderText("수정되는 상품 정보를 정확히 입력하시오.");
			alert.setContentText("다음에는 주의하세요!");
			alert.showAndWait();
		}
	}

	// 상품 삭제 메소드
	public void handlerBtnDeleteAction(ActionEvent event) {
		GoodsDAO eDao = null;
		eDao = new GoodsDAO();

		try {
			eDao.getGoodsDelete(tableView.getSelectionModel().getSelectedItem().getNo());
			data.removeAll(data); // 상품 전체 정보
			totalList();
			handlerBtnInitAction(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
		editDelete = true;
	}

	// 상품 검색 메소드
	public void handlerBtnSearchAction(ActionEvent event) {
		for (GoodsVO list : data) {
			if ((list.getG_name().equals(txtSearch.getText()))) {
				tableView.getSelectionModel().select(list);
			}
		}
	}

	// 초기화 메소드
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

	// 닫기 메소드
	private void handlerBtnCancelAction(ActionEvent event) {
		try {
			Pane root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
			Scene scene = new Scene(root);
			Stage passionStage = new Stage();
			passionStage.setTitle("네모식빵 주문시스템");
			passionStage.setScene(scene);
			Stage oldStage = (Stage) btnCancel.getScene().getWindow();
			passionStage.show();
			oldStage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
