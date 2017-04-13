package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class MyModel extends AbstractTableModel{
	Vector<String> columnName;
	
	// table�� ũ��� ���õǾ� �����Ƿ� �̹� �÷����� ��� �����ϵ��� ����
	Vector<Vector> data=new Vector<Vector>();
	
	Connection con;
	
	// ������ ���� : �����ڿ� ��ü�� �־���
	public MyModel(Connection con) {
		this.con=con;
		
		columnName=new Vector<String>();
		columnName.add("score_id");
		columnName.add("�г�");
		columnName.add("����");
		columnName.add("����");
		columnName.add("����");
		columnName.add("����");
		
		getList();
	}
	
	// ��� ���ڵ� ��������, �޼ҵ�� �����Ͽ� ���� ������ ��� ����
	public void getList(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select * from score order by score_id asc";
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				// DTO ���� -> ���ڵ� 1�� ���� ����
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
		
		// PK�� id�� ���� �Ұ�
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
	
	// JTable���� ���� �� �� ���� �����
	public void setValueAt(Object value, int row, int col) {
		Vector vec=data.get(row);
		vec.set(col,value);
	}

}
