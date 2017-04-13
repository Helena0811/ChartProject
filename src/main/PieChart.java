package main;

import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public class PieChart {
	JFreeChart chart;
	DefaultPieDataset dataSet;
	
	Connection con;
	
	// DB ������ �ʿ��� connection ��������
	public PieChart(Connection con) {
		this.con=con;
	}
	
	// ���� ���� �м�
	public void getGenderAnalysis(){		
		// DB ����
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		// select gender as ����, count(gender) as �����ڼ�, (select count(*) from score) as ��ü�л���, count(gender)/(select count(*) from score)*100 as ���� from score group by gender;
		StringBuffer sql=new StringBuffer();
		sql.append("select gender as ����, count(gender) as �����ڼ�,");
		sql.append(" (select count(*) from score) as ��ü�л���, count(gender)/(select count(*) from score)*100 as ����");
		sql.append(" from score group by gender");
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery();
			dataSet=new DefaultPieDataset();
			
			// DB�κ��� ���� �����ͷ� �׷��� ����
			while(rs.next()){
				dataSet.setValue(rs.getString("����"), rs.getInt("����"));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	
		/*
		dataSet.setValue("male", 65);
		dataSet.setValue("female", 35);
		*/
	}
	
	// �г⺰ ��� ���� �м�
	public void getGradeAnalysis(){
		// DB ����
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		// select gender as ����, count(gender) as �����ڼ�, (select count(*) from score) as ��ü�л���, count(gender)/(select count(*) from score)*100 as ���� from score group by gender;
		StringBuffer sql=new StringBuffer();
		sql.append("select grade as �г�, count(grade) as �����ڼ�,");
		sql.append(" (select count(*) from score) as ��ü�л���, count(grade)/(select count(*) from score)*100 as ����");
		sql.append(" from score group by grade");
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery();
			dataSet=new DefaultPieDataset();
			
			// DB�κ��� ���� �����ͷ� �׷��� ����
			while(rs.next()){
				dataSet.setValue(rs.getString("�г�")+"�г�", rs.getInt("����"));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
			
	}
	
	public ChartPanel drawChart(){
		/*
		 * JFreeChart -> ChartFactory
		 * public static JFreeChart createPieChart(java.lang.String title,
                                        PieDataset dataset,
                                        boolean legend,
                                        boolean tooltips,
                                        boolean urls)
		 * */
		//getGenderAnalysis();
		getGradeAnalysis();
		
		chart=ChartFactory.createPieChart(
				"���� ������ �м�", 
				dataSet, 
				true, 
				true, 
				false);
		
		// ���� ��Ʈ�� ������ ��Ʈ�� �ѱ� ��Ʈ�� �ٲ��� ������ ����� ������ ����
		System.out.println(chart.getTitle().getFont().getFontName());
		
		// ������ �� ���
		Font ori_title=chart.getTitle().getFont();														// title
		Font ori_leg=chart.getLegend().getItemFont();												// legend
		PiePlot plot=(PiePlot)(chart.getPlot());															// plot
		plot.setLabelFont(new Font("����", ori_leg.getStyle(), ori_leg.getSize()));
		
		chart.getTitle().setFont(new Font("����", ori_title.getStyle(), ori_title.getSize()));
		chart.getLegend().setItemFont(new Font("����", ori_leg.getStyle(), ori_leg.getSize()));
		
		ChartPanel chartPanel=new ChartPanel(chart);
		
		// ������ ChartPanel�� �Ѱܼ� AppMain�� panel�� ���̱�
		return chartPanel;
	}
}
