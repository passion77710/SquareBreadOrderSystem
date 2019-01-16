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

	// ��ǰ���� �Ǹŷ� �ѱ��
	public static String count = null;
	public static String price = null;

	SalesVO salesVO = new SalesVO();
	ObservableList<SalesVO> data = FXCollections.observableArrayList(); //
	ObservableList<SalesVO> selectSales; // ���̺��� ������ ���� ����
	boolean editDelete = false; // ������ �� Ȯ�� ��ư ���� ����
	int selectIndex; // ���̺��� ������ �л� ���� �ε��� ����
	int no; // ������ ���̺��� ������ �л��� ��ȣ ����

	// ��������
	private Window primaryStage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// ��ư �ʱ�ȭ
		btnOk.setDisable(false);
		btnEdit.setDisable(true);
		btnDelete.setDisable(true);
		btnCancel.setDisable(false);
		dp_day.setValue(LocalDate.now());

		// ��ǰ���� �Ǹŷ� �ѱ��
		txtp_count.setText(count);
		txtp_price.setText(price);

		//
		tableView.setEditable(false);

		// �Ǹ� ���̺� �÷����
		TableColumn colNo = new TableColumn("NO");
		colNo.setMaxWidth(40);
		colNo.setStyle("-fx-allignment: CENTER");
		colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
		TableColumn colp_Count = new TableColumn(" �Ǹż��� ");
		colp_Count.setMaxWidth(200);
		colp_Count.setCellValueFactory(new PropertyValueFactory<>("p_count"));
		TableColumn colp_Price = new TableColumn(" �Ǹűݾ� ");
		colp_Price.setMaxWidth(200);
		colp_Price.setCellValueFactory(new PropertyValueFactory<>("p_price"));
		TableColumn colp_Day = new TableColumn(" �Ǹ��� ");
		colp_Day.setMaxWidth(400);
		colp_Day.setCellValueFactory(new PropertyValueFactory<>("p_day"));

		tableView.getColumns().addAll(colNo, colp_Count, colp_Price, colp_Day);

		// �Ǹ� ��ü ����
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

		// �Ǹ� ���
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
				alert.setTitle("�Ǹ� ���� �Է�");
				alert.setHeaderText("�Ǹ� ������ ��Ȯ�� �Է��Ͻÿ�.");
				alert.setContentText("�������� �����ϼ���!");
				alert.showAndWait();
			}
		});

		btnEdit.setOnAction(event -> handlerBtnEditAction(event)); // ��������
		btnDelete.setOnAction(event -> handlerBtnDeleteAction(event)); // ��������
		btnInit.setOnAction(event -> handlerBtnInitAction(event)); // �ʱ�ȭ
		btnCancel.setOnAction(event -> handlerBtnCancelAction(event)); // �ݱ�
		tableView.setOnMouseClicked(event -> handlerBtntableViewAction(event)); // ���̺��� �Ǹż���
		btnBarChart.setOnAction(event -> handlerBtnBarChartAction(event)); // ������Ȳ
		btnExcel.setOnAction(event -> handlerBtnExcelAction(event)); // �������ϻ���
		btnSaveFileDir.setOnAction(event -> handlerBtnSaveFileDirAction(event)); // ������������
		btnPDF.setOnAction(event -> handlerBtnPDFAction(event)); // PDF���ϻ���

		// ���̺� �̺�Ʈó��
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
					alert.setTitle("�Ǹ� ���� ���� ����");
					alert.setHeaderText("�Ǹ� ������ �Է��Ͻÿ�.");
					alert.setContentText("�������� �����ϼ���!");
					alert.showAndWait();
				}
			}
		});
	}

	// ���̺��� �Ǹż���
	public void handlerBtntableViewAction(MouseEvent event) {
		try {
			btnEdit.setDisable(false);
			btnDelete.setDisable(false);
		} catch (Exception e) {
		}
	}

	// �Ǹ� ��ü ����Ʈ
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

	// �Ǹ� ����
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
			alert.setTitle("�Ǹ� ���� ����");
			alert.setHeaderText("�����Ǵ� �Ǹ� ������ ��Ȯ�� �Է��Ͻÿ�.");
			alert.setContentText("�������� �����ϼ���!");
			alert.showAndWait();
		}

	}

	// �Ǹ� ����
	public void handlerBtnDeleteAction(ActionEvent event) {
		SalesDAO sDao = null;
		sDao = new SalesDAO();

		try {
			sDao.getSalesDelete(tableView.getSelectionModel().getSelectedItem().getNo());
			data.removeAll(data); // ��ǰ ��ü ����
			totalList();
			handlerBtnInitAction(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
		editDelete = true;
	}

	// �������
	public void handlerBtnBarChartAction(ActionEvent event) {
		try {
			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(btnOk.getScene().getWindow());
			dialog.setTitle("������Ȳ");

			Parent parent = FXMLLoader.load(getClass().getResource("/view/barchart.fxml"));

			BarChart barChart = (BarChart) parent.lookup("#barChart");

			// ���� ���
			XYChart.Series seriesAugust = new XYChart.Series();

			ObservableList AugustList = FXCollections.observableArrayList();
			for (int i = 0; i < data.size(); i++) {
				AugustList.add(new XYChart.Data(data.get(i).getP_day().toString().substring(5, 7) + "��",
						Integer.parseInt(data.get(i).getP_price())));
			}
			seriesAugust.setData(AugustList);

			barChart.getData().add(seriesAugust);

			// ���� ��� ����
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

	// �ʱ�ȭ �޼ҵ�
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

	// �ݱ� �޼ҵ�
	public void handlerBtnCancelAction(ActionEvent event) {
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

	// �������ϻ���
	public void handlerBtnExcelAction(ActionEvent event) {
		SalesDAO sDao = new SalesDAO();
		boolean saveSuccess;
		ArrayList<SalesVO> list;
		list = sDao.getSalesTotal();
		SalesExcel excelWriter = new SalesExcel();

		// xlsx ���� ����
		saveSuccess = excelWriter.xlsxWiter(list, txtSaveFileDir.getText());
		if (saveSuccess) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(" ���� ���� ���� ");
			alert.setHeaderText(" �Ǹ� ��� ���� ���� ���� ���� .");
			alert.setContentText(" �Ǹ� ��� ���� ���� .");
			alert.showAndWait();
		}

		txtSaveFileDir.clear();
		btnExcel.setDisable(true);
		btnPDF.setDisable(true);

	}

	// ������������
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
			// ��Ʈ �̹��� ����
			try {
				// pdf document ���� .
				// (Rectangle pageSize, float marginLeft, float
				// marginRight, float
				// marginTop, float marginBottom)

				Document document = new Document(PageSize.A4, 0, 0, 30, 30);

				// pdf ������ ������ ������ ���� . pdf ������ �����ȴ� . ���� ��Ʈ������ ����.
				String strReportPDFName = "sales_" + System.currentTimeMillis() + ".pdf";
				PdfWriter.getInstance(document,
						new FileOutputStream(txtSaveFileDir.getText() + "\\" + strReportPDFName));

				// document �� ���� pdf ������ �����ֵ����Ѵ�.
				document.open();

				// �ѱ�������Ʈ ���� ..

				BaseFont bf = BaseFont.createFont("font/MALGUN.TTF", BaseFont.IDENTITY_H,

						BaseFont.EMBEDDED);

				Font font = new Font(bf, 8, Font.NORMAL);
				Font font2 = new Font(bf, 14, Font.BOLD);

				// Ÿ��Ʋ
				Paragraph title = new Paragraph(" �Ǹ� ���� ", font2);

				// �߰�����
				title.setAlignment(Element.ALIGN_CENTER);

				// ������ �߰�
				document.add(title);
				document.add(new Paragraph("\r\n"));

				// ���� ��¥
				LocalDate date = dp_day.getValue();
				Paragraph writeDay = new Paragraph(date.toString(), font);

				// ������ ����
				writeDay.setAlignment(Element.ALIGN_RIGHT);

				// ������ �߰�
				document.add(writeDay);
				document.add(new Paragraph("\r\n"));

				// ���̺���� Table ��ü���� PdfPTable ��ü�� �� �����ϰ� ���̺��� ����� �ִ� .

				// �����ڿ� �÷����� ���ش� .
				PdfPTable table = new PdfPTable(4);

				// ������ �÷��� width �� ���Ѵ� .
				table.setWidths(new int[] { 30, 50, 30, 30 });

				// �÷� Ÿ��Ʋ ..
				PdfPCell header1 = new PdfPCell(new Paragraph(" ��ȣ ", font));
				PdfPCell header2 = new PdfPCell(new Paragraph(" �Ǹż��� ", font));
				PdfPCell header3 = new PdfPCell(new Paragraph(" �Ǹűݾ� ", font));
				PdfPCell header4 = new PdfPCell(new Paragraph(" �Ǹ��� ", font));

				// ��������
				header1.setHorizontalAlignment(Element.ALIGN_CENTER);
				header2.setHorizontalAlignment(Element.ALIGN_CENTER);
				header3.setHorizontalAlignment(Element.ALIGN_CENTER);
				header4.setHorizontalAlignment(Element.ALIGN_CENTER);

				// ��������
				header1.setVerticalAlignment(Element.ALIGN_MIDDLE);
				header2.setVerticalAlignment(Element.ALIGN_MIDDLE);
				header3.setVerticalAlignment(Element.ALIGN_MIDDLE);
				header4.setVerticalAlignment(Element.ALIGN_MIDDLE);

				// ���̺� �߰� .

				table.addCell(header1);
				table.addCell(header2);
				table.addCell(header3);
				table.addCell(header4);

				// DB ���� �� ����Ʈ ����
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

					// ��������
					cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell4.setHorizontalAlignment(Element.ALIGN_CENTER);

					// ��������
					cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

					// ���̺� �� �߰�
					table.addCell(cell1);
					table.addCell(cell2);
					table.addCell(cell3);
					table.addCell(cell4);
				}

				// ������ ���̺��߰� ..
				document.add(table);
				document.add(new Paragraph("\r\n"));
				Alert alert = new Alert(AlertType.INFORMATION);
				// if (cbBarImage.isSelected()) {

				// ���� �׷��� �̹��� �߰�
				Paragraph barImageTitle = new Paragraph(" �Ǹ� ���� ���� �׷��� ", font);
				barImageTitle.setAlignment(Element.ALIGN_CENTER);
				document.add(barImageTitle);
				document.add(new Paragraph("\r\n"));
				final String barImageUrl = "chartImage/salesBarChart.png";

				// ������ javafx.scene.image.Image ��ü�� ����ϰ� �־� �浹�� ���� �Ʒ��� ���� ����� .
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
				 * // ���� �׷��� �̹��� �߰� Paragraph barImageTitle = new Paragraph(" �Ǹ� ���̺� ���� �׷��� ",
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

				// ������ �ݴ´� .. ���� ���� ..
				document.close();
				txtSaveFileDir.clear();
				btnPDF.setDisable(true);
				btnExcel.setDisable(true);
				alert.setTitle("PDF ���� ���� ");
				alert.setHeaderText(" �Ǹ� ��� PDF ���� ���� ���� .");
				alert.setContentText(" �Ǹ� ��� PDF ���� .");
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