package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import db.DBManager;

public class AppMain extends JFrame implements ActionListener{
	JPanel p_center, p_east, p_south;
	JTable table;
	JScrollPane scroll;
	JButton bt_save, bt_graph;
	
	DBManager manager=DBManager.getInstance();
	Connection con;
	
	MyModel model;
	
	PieChart pieChart;
	
	// Excel 저장에 사용
	JFileChooser chooser;
	FileOutputStream fos;
	
	public AppMain() {
		con=manager.getConnection();
		
		p_center=new JPanel();
		p_east=new JPanel();
		p_south=new JPanel();
		
		table=new JTable(model=new MyModel(con));
		scroll=new JScrollPane(table);
		bt_save=new JButton("Excel로 저장");
		bt_graph=new JButton("그래프 보기");
		
		pieChart=new PieChart(con);
				
		// 그래프를 보여줄 영역
		p_east.setPreferredSize(new Dimension(450, 450));
		
		// JTable 붙이기
		p_center.setLayout(new BorderLayout());
		p_center.add(scroll);
		add(p_east, BorderLayout.EAST);
		add(p_center);
		p_south.add(bt_save);
		p_south.add(bt_graph);
		add(p_south, BorderLayout.SOUTH);
		
		// 윈도우와 listener 연결
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// 윈도우 창을 닫으면 connection 제거 후 종료
				manager.disConnect(con);
				System.exit(0);
			}
		});
		
		// 버튼과 ActionListener 연결
		bt_save.addActionListener(this);
		bt_graph.addActionListener(this);
		
		setSize(950, 500);
		setVisible(true);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	// excel 저장 -> POI 사용
	public void saveExcel(){
		chooser=new JFileChooser("C:/");
		
		// sheet을 만들고 cell 붙이기

			HSSFWorkbook workBook=new HSSFWorkbook();
			HSSFSheet sheet=workBook.createSheet("성적 정보");

		/*
		 * 이중포문을 이용해 cell을 추출
		 * 바깥쪽 : row
		 * 안쪽 : col
		 * */
		for(int i=0; i<table.getRowCount(); i++){
			// HSSFRow 생성
			HSSFRow row=sheet.createRow(i);
			
			for(int j=0; j<table.getColumnCount(); j++){
				// HSSFColumn 생성
				//System.out.print(table.getValueAt(i, j)+",");
				HSSFCell cell=row.createCell(j);
				cell.setCellValue(table.getValueAt(i, j).toString());
			}
			//System.out.println();
		}
		int result=chooser.showSaveDialog(this);
		if(result==JFileChooser.APPROVE_OPTION){
			// 사용자는 확장자 .xls를 붙여야 함!
			File file=chooser.getSelectedFile();
			try {
				fos=new FileOutputStream(file);
				// 워크북에 fos로 가져온 파일 쓰기
				workBook.write(fos);
				
				// 확장자가 .xls가 아니면
				
				
				JOptionPane.showMessageDialog(this, "Excel파일 생성 완료");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				if(fos!=null){
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	// 그래프 보기
	public void showGraph(){
		p_east.add(pieChart.drawChart());
		p_east.updateUI();
	}
	
	// 각 버튼을 누르면
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		
		if(obj==bt_save){
			saveExcel();
		}
		else if(obj==bt_graph){
			showGraph();
		}
		
	}
	
	public static void main(String[] args) {
		new AppMain();

	}

}
