package controller;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import model.SalesVO;

public class SalesController<X, Y> implements Initializable {
	@FXML
	private TableView<SalesVO> tableView;
	@FXML
	private TextField txtp_count;
	@FXML
	private TextField txtp_price;
	@FXML
	private DatePicker dp_day;
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
	private Button btnBarChart;
	@FXML
	private Button btnExcel;
	@FXML
	private Button btnSaveFileDir;
	@FXML
	private TextField txtSaveFileDir;
	@FXML
	private Button btnPDF;

	// 상품에서 판매로 넘기기
	public static String count = null;
	public static String price = null;

	SalesVO salesVO = new SalesVO();
	ObservableList<SalesVO> data = FXCollections.observableArrayList(); //
	ObservableList<SalesVO> selectSales; // 테이블에서 선택한 정보 저장
	boolean editDelete = false; // 수정할 때 확인 버튼 상태 설정
	int selectIndex; // 테이블에서 선택한 학생 정보 인덱스 저장
	int no; // 삭제시 테이블에서 선택한 학생의 번호 저장

	// 파일저장
	private Window primaryStage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 버튼 초기화
		btnOk.setDisable(false);
		btnEdit.setDisable(true);
		btnDelete.setDisable(true);
		btnCancel.setDisable(false);
		dp_day.setValue(LocalDate.now());

		// 상품에서 판매로 넘기기
		txtp_count.setText(count);
		txtp_price.setText(price);

		//
		tableView.setEditable(false);

		// 판매 테이블에 컬럼등록
		TableColumn colNo = new TableColumn("NO");
		colNo.setMaxWidth(40);
		colNo.setStyle("-fx-allignment: CENTER");
		colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
		TableColumn colp_Count = new TableColumn(" 판매수량 ");
		colp_Count.setMaxWidth(200);
		colp_Count.setCellValueFactory(new PropertyValueFactory<>("p_count"));
		TableColumn colp_Price = new TableColumn(" 판매금액 ");
		colp_Price.setMaxWidth(200);
		colp_Price.setCellValueFactory(new PropertyValueFactory<>("p_price"));
		TableColumn colp_Day = new TableColumn(" 판매일 ");
		colp_Day.setMaxWidth(400);
		colp_Day.setCellValueFactory(new PropertyValueFactory<>("p_day"));

		tableView.getColumns().addAll(colNo, colp_Count, colp_Price, colp_Day);

		// 판매 전체 정보
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

		// 판매 등록
		btnOk.setOnAction(event -> {
			try {
				data.removeAll(data);
				SalesVO svo = null;
				SalesDAO sdao = null;

				if (event.getSource().equals(btnOk)) {

					svo = new SalesVO(txtp_count.getText(), txtp_price.getText(), dp_day.getValue().toString());

					sdao = new SalesDAO();
					sdao.getSalesRegiste(svo);

					if (sdao != null) {

						totalList();

						txtp_price.setEditable(true);
						handlerBtnInitAction(event);
					}
				}

			} catch (Exception e) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("판매 정보 입력");
				alert.setHeaderText("판매 정보를 정확히 입력하시오.");
				alert.setContentText("다음에는 주의하세요!");
				alert.showAndWait();
			}
		});

		btnEdit.setOnAction(event -> handlerBtnEditAction(event)); // 직원수정
		btnDelete.setOnAction(event -> handlerBtnDeleteAction(event)); // 직원삭제
		btnInit.setOnAction(event -> handlerBtnInitAction(event)); // 초기화
		btnCancel.setOnAction(event -> handlerBtnCancelAction(event)); // 닫기
		tableView.setOnMouseClicked(event -> handlerBtntableViewAction(event)); // 테이블의 판매선택
		btnBarChart.setOnAction(event -> handlerBtnBarChartAction(event)); // 매출현황
		btnExcel.setOnAction(event -> handlerBtnExcelAction(event)); // 엑셀파일생성
		btnSaveFileDir.setOnAction(event -> handlerBtnSaveFileDirAction(event)); // 파일저장폴더
		btnPDF.setOnAction(event -> handlerBtnPDFAction(event)); // PDF파일생성

		// 테이블 이벤트처리
		tableView.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				selectSales = tableView.getSelectionModel().getSelectedItems();
				selectIndex = tableView.getSelectionModel().getSelectedIndex();

				try {
					txtp_count.setText(selectSales.get(0).getP_count());
					txtp_price.setText(selectSales.get(0).getP_price());

					btnDelete.setDisable(false);
					btnOk.setDisable(true);
					btnCancel.setDisable(true);

					editDelete = true;
				} catch (Exception e) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("판매 정보 수정 삭제");
					alert.setHeaderText("판매 정보를 입력하시오.");
					alert.setContentText("다음에는 주의하세요!");
					alert.showAndWait();
				}
			}
		});
	}

	// 테이블의 판매선택
	public void handlerBtntableViewAction(MouseEvent event) {
		try {
			btnEdit.setDisable(false);
			btnDelete.setDisable(false);
		} catch (Exception e) {
		}
	}

	// 판매 전체 리스트
	public void totalList() {

		Object[][] totalData;

		SalesDAO sDao = new SalesDAO();
		SalesVO sVo = new SalesVO();
		ArrayList<String> title;

		ArrayList<SalesVO> list;

		title = sDao.getColumnName();
		int columnCount = title.size();

		list = sDao.getSalesTotal();
		int rowCount = list.size();

		totalData = new Object[rowCount][columnCount];

		for (int index = 0; index < rowCount; index++) {

			sVo = list.get(index);
			data.add(sVo);

		}
	}

	// 판매 수정
	public void handlerBtnEditAction(ActionEvent event) {
		SalesVO svo = null;
		SalesDAO sdao = null;

		try {
			svo = new SalesVO(tableView.getSelectionModel().getSelectedIndex(), txtp_count.getText(),
					txtp_price.getText(), dp_day.getValue().toString());

			sdao = new SalesDAO();

			sdao.getSalesUpdate(svo, tableView.getSelectionModel().getSelectedItem().getNo());

			data.removeAll(data);
			totalList();

			txtp_price.setEditable(true);

		} catch (Exception e) {
			System.out.println(e);
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("판매 정보 수정");
			alert.setHeaderText("수정되는 판매 정보를 정확히 입력하시오.");
			alert.setContentText("다음에는 주의하세요!");
			alert.showAndWait();
		}

	}

	// 판매 삭제
	public void handlerBtnDeleteAction(ActionEvent event) {
		SalesDAO sDao = null;
		sDao = new SalesDAO();

		try {
			sDao.getSalesDelete(tableView.getSelectionModel().getSelectedItem().getNo());
			data.removeAll(data); // 상품 전체 정보
			totalList();
			handlerBtnInitAction(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
		editDelete = true;
	}

	// 매출통계
	public void handlerBtnBarChartAction(ActionEvent event) {
		try {
			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(btnOk.getScene().getWindow());
			dialog.setTitle("매출현황");

			Parent parent = FXMLLoader.load(getClass().getResource("/view/barchart.fxml"));

			BarChart barChart = (BarChart) parent.lookup("#barChart");

			// 월별 통계
			XYChart.Series seriesAugust = new XYChart.Series();

			ObservableList AugustList = FXCollections.observableArrayList();
			for (int i = 0; i < data.size(); i++) {
				AugustList.add(new XYChart.Data(data.get(i).getP_day().toString().substring(5, 7) + "월",
						Integer.parseInt(data.get(i).getP_price())));
			}
			seriesAugust.setData(AugustList);

			barChart.getData().add(seriesAugust);

			// 매출 통계 종료
			Button btnClose = (Button) parent.lookup("#btnClose");
			btnClose.setOnAction(e -> dialog.close());

			Scene scene = new Scene(parent);
			dialog.setScene(scene);
			dialog.show();

			WritableImage snapShot = scene.snapshot(null);
			ImageIO.write(SwingFXUtils.fromFXImage(snapShot, null), "png", new File("chartImage/salesBarChart.png"));
		} catch (IOException e) {
		}
	}

	// 초기화 메소드
	public void handlerBtnInitAction(ActionEvent event) {
		try {
			txtp_count.clear();
			txtp_price.clear();
			btnOk.setDisable(false);
			btnCancel.setDisable(false);
			btnEdit.setDisable(true);
			btnDelete.setDisable(true);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// 닫기 메소드
	public void handlerBtnCancelAction(ActionEvent event) {
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

	// 엑셀파일생성
	public void handlerBtnExcelAction(ActionEvent event) {
		SalesDAO sDao = new SalesDAO();
		boolean saveSuccess;
		ArrayList<SalesVO> list;
		list = sDao.getSalesTotal();
		SalesExcel excelWriter = new SalesExcel();

		// xlsx 파일 쓰기
		saveSuccess = excelWriter.xlsxWiter(list, txtSaveFileDir.getText());
		if (saveSuccess) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(" 엑셀 파일 생성 ");
			alert.setHeaderText(" 판매 목록 엑셀 파일 생성 성공 .");
			alert.setContentText(" 판매 목록 엑셀 파일 .");
			alert.showAndWait();
		}

		txtSaveFileDir.clear();
		btnExcel.setDisable(true);
		btnPDF.setDisable(true);

	}

	// 파일저장폴더
	public void handlerBtnSaveFileDirAction(ActionEvent event) {
		final DirectoryChooser directoryChooser = new DirectoryChooser();
		final File selectedDirectory = directoryChooser.showDialog(primaryStage);

		if (selectedDirectory != null) {
			txtSaveFileDir.setText(selectedDirectory.getAbsolutePath());
			btnExcel.setDisable(false);
			btnPDF.setDisable(false);
		}
	}

	// PDF
	public void handlerBtnPDFAction(ActionEvent event) {
		try {
			// 차트 이미지 포함
			try {
				// pdf document 선언 .
				// (Rectangle pageSize, float marginLeft, float
				// marginRight, float
				// marginTop, float marginBottom)

				Document document = new Document(PageSize.A4, 0, 0, 30, 30);

				// pdf 파일을 저장할 공간을 선언 . pdf 파일이 생성된다 . 그후 스트림으로 저장.
				String strReportPDFName = "sales_" + System.currentTimeMillis() + ".pdf";
				PdfWriter.getInstance(document,
						new FileOutputStream(txtSaveFileDir.getText() + "\\" + strReportPDFName));

				// document 를 열어 pdf 문서를 쓸수있도록한다.
				document.open();

				// 한글지원폰트 설정 ..

				BaseFont bf = BaseFont.createFont("font/MALGUN.TTF", BaseFont.IDENTITY_H,

						BaseFont.EMBEDDED);

				Font font = new Font(bf, 8, Font.NORMAL);
				Font font2 = new Font(bf, 14, Font.BOLD);

				// 타이틀
				Paragraph title = new Paragraph(" 판매 매출 ", font2);

				// 중간정렬
				title.setAlignment(Element.ALIGN_CENTER);

				// 문서에 추가
				document.add(title);
				document.add(new Paragraph("\r\n"));

				// 생성 날짜
				LocalDate date = dp_day.getValue();
				Paragraph writeDay = new Paragraph(date.toString(), font);

				// 오른쪽 정렬
				writeDay.setAlignment(Element.ALIGN_RIGHT);

				// 문서에 추가
				document.add(writeDay);
				document.add(new Paragraph("\r\n"));

				// 테이블생성 Table 객체보다 PdfPTable 객체가 더 정교하게 테이블을 만들수 있다 .

				// 생성자에 컬럼수를 써준다 .
				PdfPTable table = new PdfPTable(4);

				// 각각의 컬럼에 width 를 정한다 .
				table.setWidths(new int[] { 30, 50, 30, 30 });

				// 컬럼 타이틀 ..
				PdfPCell header1 = new PdfPCell(new Paragraph(" 번호 ", font));
				PdfPCell header2 = new PdfPCell(new Paragraph(" 판매수량 ", font));
				PdfPCell header3 = new PdfPCell(new Paragraph(" 판매금액 ", font));
				PdfPCell header4 = new PdfPCell(new Paragraph(" 판매일 ", font));

				// 가로정렬
				header1.setHorizontalAlignment(Element.ALIGN_CENTER);
				header2.setHorizontalAlignment(Element.ALIGN_CENTER);
				header3.setHorizontalAlignment(Element.ALIGN_CENTER);
				header4.setHorizontalAlignment(Element.ALIGN_CENTER);

				// 세로정렬
				header1.setVerticalAlignment(Element.ALIGN_MIDDLE);
				header2.setVerticalAlignment(Element.ALIGN_MIDDLE);
				header3.setVerticalAlignment(Element.ALIGN_MIDDLE);
				header4.setVerticalAlignment(Element.ALIGN_MIDDLE);

				// 테이블에 추가 .

				table.addCell(header1);
				table.addCell(header2);
				table.addCell(header3);
				table.addCell(header4);

				// DB 연결 및 리스트 선택
				SalesDAO sDao = new SalesDAO();
				SalesVO sVo = new SalesVO();
				ArrayList<SalesVO> list;
				list = sDao.getSalesTotal();
				int rowCount = list.size();
				PdfPCell cell1 = null;
				PdfPCell cell2 = null;
				PdfPCell cell3 = null;
				PdfPCell cell4 = null;

				for (int index = 0; index < rowCount; index++) {
					sVo = list.get(index);
					cell1 = new PdfPCell(new Paragraph(sVo.getNo() + "", font));
					cell2 = new PdfPCell(new Paragraph(sVo.getP_count(), font));
					cell3 = new PdfPCell(new Paragraph(sVo.getP_price(), font));
					cell4 = new PdfPCell(new Paragraph(sVo.getP_day(), font));

					// 가로정렬
					cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell4.setHorizontalAlignment(Element.ALIGN_CENTER);

					// 세로정렬
					cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

					// 테이블에 셀 추가
					table.addCell(cell1);
					table.addCell(cell2);
					table.addCell(cell3);
					table.addCell(cell4);
				}

				// 문서에 테이블추가 ..
				document.add(table);
				document.add(new Paragraph("\r\n"));
				Alert alert = new Alert(AlertType.INFORMATION);
				// if (cbBarImage.isSelected()) {

				// 막대 그래프 이미지 추가
				Paragraph barImageTitle = new Paragraph(" 판매 매출 막대 그래프 ", font);
				barImageTitle.setAlignment(Element.ALIGN_CENTER);
				document.add(barImageTitle);
				document.add(new Paragraph("\r\n"));
				final String barImageUrl = "chartImage/salesBarChart.png";

				// 기존에 javafx.scene.image.Image 객체을 사용하고 있어 충돌이 생겨 아래와 같이 사용함 .
				com.itextpdf.text.Image barImage;
				try {
					if (com.itextpdf.text.Image.getInstance(barImageUrl) != null) {
						barImage = com.itextpdf.text.Image.getInstance(barImageUrl);
						barImage.setAlignment(Element.ALIGN_CENTER);
						barImage.scalePercent(30f);
						document.add(barImage);
						document.add(new Paragraph("\r\n"));
					}
				} catch (IOException ee) {
				}

				/*
				 * // 막대 그래프 이미지 추가 Paragraph barImageTitle = new Paragraph(" 판매 테이블 막대 그래프 ",
				 * font); barImageTitle.setAlignment(Element.ALIGN_CENTER);
				 * document.add(barImageTitle); document.add(new Paragraph("\r\n")); final
				 * String barImageUrl = "chartImage/salesBarChart.png"; com.itextpdf.text.Image
				 * barImage; try { if (com.itextpdf.text.Image.getInstance(barImageUrl) != null)
				 * { barImage = com.itextpdf.text.Image.getInstance(barImageUrl);
				 * barImage.setAlignment(Element.ALIGN_CENTER); barImage.scalePercent(30f);
				 * document.add(barImage);
				 * 
				 * }
				 * 
				 * } catch (IOException ee) { }
				 * 
				 * }
				 */
				// }

				// 문서를 닫는다 .. 쓰기 종료 ..
				document.close();
				txtSaveFileDir.clear();
				btnPDF.setDisable(true);
				btnExcel.setDisable(true);
				alert.setTitle("PDF 파일 생성 ");
				alert.setHeaderText(" 판매 목록 PDF 파일 생성 성공 .");
				alert.setContentText(" 판매 목록 PDF 파일 .");
				alert.showAndWait();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (DocumentException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}