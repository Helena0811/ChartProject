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
	
	// DB 연동에 필요한 connection 가져오기
	public PieChart(Connection con) {
		this.con=con;
	}
	
	// 성별 비율 분석
	public void getGenderAnalysis(){		
		// DB 연동
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		// select gender as 성별, count(gender) as 응시자수, (select count(*) from score) as 전체학생수, count(gender)/(select count(*) from score)*100 as 비율 from score group by gender;
		StringBuffer sql=new StringBuffer();
		sql.append("select gender as 성별, count(gender) as 응시자수,");
		sql.append(" (select count(*) from score) as 전체학생수, count(gender)/(select count(*) from score)*100 as 비율");
		sql.append(" from score group by gender");
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery();
			dataSet=new DefaultPieDataset();
			
			// DB로부터 얻은 데이터로 그래프 설정
			while(rs.next()){
				dataSet.setValue(rs.getString("성별"), rs.getInt("비율"));
				
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
	
	// 학년별 평균 성적 분석
	public void getGradeAnalysis(){
		// DB 연동
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		// select gender as 성별, count(gender) as 응시자수, (select count(*) from score) as 전체학생수, count(gender)/(select count(*) from score)*100 as 비율 from score group by gender;
		StringBuffer sql=new StringBuffer();
		sql.append("select grade as 학년, count(grade) as 응시자수,");
		sql.append(" (select count(*) from score) as 전체학생수, count(grade)/(select count(*) from score)*100 as 비율");
		sql.append(" from score group by grade");
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery();
			dataSet=new DefaultPieDataset();
			
			// DB로부터 얻은 데이터로 그래프 설정
			while(rs.next()){
				dataSet.setValue(rs.getString("학년")+"학년", rs.getInt("비율"));
				
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
				"성적 데이터 분석", 
				dataSet, 
				true, 
				true, 
				false);
		
		// 현재 차트에 설정된 폰트를 한글 폰트로 바꾸지 않으면 제대로 나오지 않음
		System.out.println(chart.getTitle().getFont().getFontName());
		
		// 기존의 값 얻기
		Font ori_title=chart.getTitle().getFont();														// title
		Font ori_leg=chart.getLegend().getItemFont();												// legend
		PiePlot plot=(PiePlot)(chart.getPlot());															// plot
		plot.setLabelFont(new Font("돋움", ori_leg.getStyle(), ori_leg.getSize()));
		
		chart.getTitle().setFont(new Font("돋움", ori_title.getStyle(), ori_title.getSize()));
		chart.getLegend().setItemFont(new Font("돋움", ori_leg.getStyle(), ori_leg.getSize()));
		
		ChartPanel chartPanel=new ChartPanel(chart);
		
		// 생성된 ChartPanel을 넘겨서 AppMain의 panel에 붙이기
		return chartPanel;
	}
}
