/*
 * ���� ī�װ��� ��ϵ� ��ǰ ������ �����ϴ� ��
 * */
package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class DownModel extends AbstractTableModel{
	Vector<String> columnName=new Vector<String>();
	public Vector<Vector> data=new Vector<Vector>();
	
	Connection con;
	
	public DownModel(Connection con) {
		this.con=con;
		
		// ���� column�� �������̱� ������ tableModel�� new�� ������ �� column�� �������ѳ���
		columnName.add("product_id");
		columnName.add("subcategory_id");
		columnName.add("product_name");
		columnName.add("price");
		columnName.add("img");
	}
	
	// ���콺�� ����ڰ� Ŭ���� �� ���� id���� �ٲ�Ƿ�, �Ʒ��� �޼ҵ带 Ŭ�� �ø��� ȣ��
	public void getList(int subcategory_id){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		// ���� ī�װ��� ������ ���� ��ǰ ��� ���
		// select * from product where subcategory_id=���õ� ��;
		String sql="select * from product where subcategory_id=?";
		
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, subcategory_id);
			rs=pstmt.executeQuery();
			
			// Vector �ʱ�ȭ
			columnName.removeAll(columnName);
			data.removeAll(data);
			
			/*
			 * ���ʿ� �����ڿ��� ������Ű�� �����Ƿ� �ּ� ó��
			// columnName ä���
			ResultSetMetaData meta=rs.getMetaData();
			for(int i=1; i<=meta.getColumnCount(); i++){
				columnName.add(meta.getColumnName(i));
			}
			*/
			//System.out.println("getList �÷��� ũ��� "+columnName.size());
			
			while(rs.next()){
				Vector vec=new Vector();
				// boxing : �⺻�ڷ������� ��
				vec.add(rs.getString("product_id"));
				vec.add(rs.getString("subcategory_id"));
				vec.add(rs.getString("product_name"));
				vec.add(rs.getString("price"));
				vec.add(rs.getString("img"));
				
				data.add(vec);
			}
			//System.out.println("getList ���ڵ��� ũ��� "+data.size());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getColumnName(int col) {
		return columnName.get(col);
	}
	
	public int getRowCount() {
		//System.out.println("���ڵ��� ������ "+data.size());
		return data.size();
	}

	public int getColumnCount() {
		// updateUI()�� �����ص� �� �޼ҵ�� tableModel�� new�� ������ �� �÷��� 0�̶�� �Ǵ��ϱ� ������ ��µ��� ����
		// System.out.println("�÷��� ������ "+data.size());
		return columnName.size();
	}

	public Object getValueAt(int row, int col) {
		Object value=data.get(row).get(col);
		// updateUI()�� �����ص� �� �޼ҵ�� tableModel�� new�� ������ �� �÷��� 0�̶�� �Ǵ��ϱ� ������ ��µ��� ����
		// System.out.println("�� ���� "+value);
		return data.get(row).get(col);
	}
	
}
