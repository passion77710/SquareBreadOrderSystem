package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import model.SalesVO;

public class SalesExcel {
	public boolean xlsxWiter(List<SalesVO> list, String saveDir) {
		// 워크북 생성
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 워크시트 생성
		XSSFSheet sheet = workbook.createSheet();
		// 행 생성
		XSSFRow row = sheet.createRow(0);
		// 셀 생성
		XSSFCell cell;
		// 헤더 정보 구성
		cell = row.createCell(0);
		cell.setCellValue(" 번호 ");

		cell = row.createCell(1);
		cell.setCellValue(" 판매수량 ");

		cell = row.createCell(2);
		cell.setCellValue(" 판매금액 ");

		cell = row.createCell(3);
		cell.setCellValue(" 판매일 ");

		// 리스트의 size 만큼 row 를 생성
		SalesVO vo;
		for (int rowIdx = 0; rowIdx < list.size(); rowIdx++) {
			vo = list.get(rowIdx);
			// 행 생성
			row = sheet.createRow(rowIdx + 1);

			cell = row.createCell(0);
			cell.setCellValue(vo.getNo() + "");

			cell = row.createCell(1);
			cell.setCellValue(vo.getP_count());

			cell = row.createCell(2);
			cell.setCellValue(vo.getP_price());

			cell = row.createCell(3);
			cell.setCellValue(vo.getP_day());

		}

		// 입력된 내용 파일로 쓰기
		String strReportPDFName = "sales_" + System.currentTimeMillis() + ".xlsx";
		File file = new File(saveDir + "\\" + strReportPDFName);
		FileOutputStream fos = null;

		boolean saveSuccess;
		saveSuccess = false;
		try {
			fos = new FileOutputStream(file);
			if (fos != null) {
				workbook.write(fos);
				saveSuccess = true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (workbook != null)
					workbook.close();
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return saveSuccess;
	}
}
