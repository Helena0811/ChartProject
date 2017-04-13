package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class MyModel extends AbstractTableModel{
	Vector<String> columnName;
	
	// table의 크기와 관련되어 있으므로 이미 올려놓고 사용 가능하도록 구현
	Vector<Vector> data=new Vector<Vector>();
	
	Connection con;
	
	// 생성자 주입 : 생성자에 객체를 넣어줌
	public MyModel(Connection con) {
		this.con=con;
		
		columnName=new Vector<String>();
		columnName.add("score_id");
		columnName.add("학년");
		columnName.add("성별");
		columnName.add("국어");
		columnName.add("영어");
		columnName.add("수학");
		
		getList();
	}
	
	// 모든 레코드 가져오기, 메소드로 구현하여 원할 때마다 사용 가능
	public void getList(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select * from score order by score_id asc";
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				// DTO 역할 -> 레코드 1건 정보 저장
				Vector vector=new Vector();		
				vector.add(rs.getString("score_id"));
				vector.add(rs.getString("grade"));
				vector.add(rs.getString("gender"));
				vector.add(rs.getString("kor"));
				vector.add(rs.getString("eng"));
				vector.add(rs.getString("math"));
				
				data.add(vector);
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
	
	public boolean isCellEditable(int row, int col) {
		boolean flag=true;
		
		// PK인 id는 변경 불가
		if(col==0){
			flag=false;
		}
		return flag;
	}
	
	public String getColumnName(int col) {
		return columnName.elementAt(col);
	}
	
	public int getRowCount() {
		return data.size();
	}

	public int getColumnCount() {
		return columnName.size();
	}

	public Object getValueAt(int row, int col) {
		return data.elementAt(row).elementAt(col);
	}
	
	// JTable에서 편집 시 그 값이 적용됨
	public void setValueAt(Object value, int row, int col) {
		Vector vec=data.get(row);
		vec.set(col,value);
	}

}
