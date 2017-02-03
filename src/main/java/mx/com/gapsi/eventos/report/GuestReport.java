package mx.com.gapsi.eventos.report;

import java.util.List;

import mx.com.gapsi.eventos.model.Invitado;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

public class GuestReport extends AbstractExcelReport {

	private final static String[] TITLES = {"Id", "Nombre", "Ap Paterno", "Ap Materno", "Email", "Tipo", "Empresa", "Invitador", "No. Boletos", "Estatus"};

	private List<Invitado> invitados;
	private List<Invitado> invitadosStatusOcupado;
	
	/**
	 * Constructor.
	 * @param invitados
	 */
	public GuestReport(List<Invitado> invitados, List<Invitado> invitadosStatusOcupado) {
		super();
		this.invitados = invitados;
		this.invitadosStatusOcupado = invitadosStatusOcupado;
		this.generateReport();
	}

	public void generateReport() {
		createGuestsReport();
		createTotalReport();
		setStyleSheet0();
		setStyleSheet1();
	}

	private void createGuestsReport() {
		
		createSheet("Invitados");
		
		nextRow();
		for (String title : TITLES) {
			createCell(title);
		}

		for (Invitado invitado : invitados) {
			nextRow();
			createCell(invitado.getId());
			createCell(invitado.getNombre());
			createCell(invitado.getApPaterno());
			createCell(invitado.getApMaterno());
			createCell(invitado.getEmail());
			createCell(invitado.getTipoInvitado().getNombre());
			createCell(invitado.getEmpresa());
			createCell(invitado.getInvitador());
			createCell(invitado.getEventoInvitado().getNoBoletos());
			createCell(invitado.getEventoInvitado().getEstatus());
		}
	}

	private void createTotalReport() {
		createSheet("Totales");
		
		nextRow();
		createCell("Total de invitados");
		createCell(invitados.size());
		
		nextRow();
		createCell("Total de invitados confirmados");
		createCell(invitadosStatusOcupado.size());
		
	}
	
	private void setStyleSheet0() {
		HSSFCellStyle titleStyle = getTitleStyle(workbook);

        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);
        for(int jdx = 0; jdx < header.getPhysicalNumberOfCells(); jdx++) {
        	HSSFCell cell = header.getCell(jdx);
        	cell.setCellStyle(titleStyle);
        	
        	sheet.setColumnWidth(jdx, 12 * CHARACTER_SIZE);
        }
	}

	private void setStyleSheet1() {
		HSSFSheet sheet0 = workbook.getSheetAt(1);
		sheet0.setColumnWidth(0, 28 * CHARACTER_SIZE);
	}
	
	private Font getTitleFont(HSSFWorkbook wb) {
	    Font font = wb.createFont();
	    font.setBold(true);
	    return font;
	}

	private HSSFCellStyle getTitleStyle(HSSFWorkbook wb) {
		HSSFCellStyle styleTitle = wb.createCellStyle();
	    styleTitle.setFont(getTitleFont(wb));
	    styleTitle.setAlignment(CellStyle.ALIGN_CENTER);
	    return styleTitle;
	}
	
}
