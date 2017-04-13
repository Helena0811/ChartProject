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
	
	// Excel ���忡 ���
	JFileChooser chooser;
	FileOutputStream fos;
	
	public AppMain() {
		con=manager.getConnection();
		
		p_center=new JPanel();
		p_east=new JPanel();
		p_south=new JPanel();
		
		table=new JTable(model=new MyModel(con));
		scroll=new JScrollPane(table);
		bt_save=new JButton("Excel�� ����");
		bt_graph=new JButton("�׷��� ����");
		
		pieChart=new PieChart(con);
				
		// �׷����� ������ ����
		p_east.setPreferredSize(new Dimension(450, 450));
		
		// JTable ���̱�
		p_center.setLayout(new BorderLayout());
		p_center.add(scroll);
		add(p_east, BorderLayout.EAST);
		add(p_center);
		p_south.add(bt_save);
		p_south.add(bt_graph);
		add(p_south, BorderLayout.SOUTH);
		
		// ������� listener ����
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// ������ â�� ������ connection ���� �� ����
				manager.disConnect(con);
				System.exit(0);
			}
		});
		
		// ��ư�� ActionListener ����
		bt_save.addActionListener(this);
		bt_graph.addActionListener(this);
		
		setSize(950, 500);
		setVisible(true);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	// excel ���� -> POI ���
	public void saveExcel(){
		chooser=new JFileChooser("C:/");
		
		// sheet�� ����� cell ���̱�

			HSSFWorkbook workBook=new HSSFWorkbook();
			HSSFSheet sheet=workBook.createSheet("���� ����");

		/*
		 * ���������� �̿��� cell�� ����
		 * �ٱ��� : row
		 * ���� : col
		 * */
		for(int i=0; i<table.getRowCount(); i++){
			// HSSFRow ����
			HSSFRow row=sheet.createRow(i);
			
			for(int j=0; j<table.getColumnCount(); j++){
				// HSSFColumn ����
				//System.out.print(table.getValueAt(i, j)+",");
				HSSFCell cell=row.createCell(j);
				cell.setCellValue(table.getValueAt(i, j).toString());
			}
			//System.out.println();
		}
		int result=chooser.showSaveDialog(this);
		if(result==JFileChooser.APPROVE_OPTION){
			// ����ڴ� Ȯ���� .xls�� �ٿ��� ��!
			File file=chooser.getSelectedFile();
			try {
				fos=new FileOutputStream(file);
				// ��ũ�Ͽ� fos�� ������ ���� ����
				workBook.write(fos);
				
				// Ȯ���ڰ� .xls�� �ƴϸ�
				
				
				JOptionPane.showMessageDialog(this, "Excel���� ���� �Ϸ�");
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
	
	// �׷��� ����
	public void showGraph(){
		p_east.add(pieChart.drawChart());
		p_east.updateUI();
	}
	
	// �� ��ư�� ������
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
