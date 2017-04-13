/*
 * ���� ī�װ��� �� ī�װ��� ��ϵ� ��ǰ�� �� ������ �����ϴ� ��
 * */
package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class UpModel extends AbstractTableModel{
	Vector<String> columnName=new Vector<String>();
	Vector<Vector> data=new Vector<Vector>();					// 2���� ����
	Connection con;
	
	public UpModel(Connection con) {
		this.con=con;
		getList();		// �����ڿ� ����, �޼ҵ�� �ʿ��� �� ������ �ٽ� ȣ�� ����
	}
	
	// ��� ��������
	public void getList(){
		/*
		 * select s.subcategory_id, sub_name, count(product_name) from subcategory s left outer join product p 
			on s.subcategory_id=p.subcategory_id
			group by s.subcategory_id, sub_name;
			-> s.subcategory_id �տ� �������� ���� ���� : �ߺ��� �÷��̱� ������ �����ϱ� ���ؼ� ���
			-> sub_name�� ��� �ߺ����� ���� �÷��̱� ������ �������� �ٿ���, ������ �ʾƵ� �������
		 * */
		StringBuffer sql=new StringBuffer();
		sql.append("select s.subcategory_id as subcategory_id, sub_name as ī�װ���, count(product_name) as ����");
		sql.append(" from subcategory s left outer join product p");
		sql.append(" on s.subcategory_id=p.subcategory_id");
		sql.append(" group by s.subcategory_id, sub_name");
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery();
			
			// ���̺� ���� -> ��� �����Ǿ� ��µǴ� ���� ����
			// columnName�� data Vector �ʱ�ȭ
			columnName.removeAll(columnName);
			data.removeAll(data);
			
			// �÷��� ����
			ResultSetMetaData meta=rs.getMetaData();
			for(int i=1; i<=meta.getColumnCount(); i++){
				// �÷������ vector�� ����
				columnName.add(meta.getColumnName(i));
			}
			
			while(rs.next()){
				// ���ڵ� 1���� vector�� ���� -> ���⼭�� ���ʹ� DTO ����
				Vector vec=new Vector();
				// JTable�� ����, ���� ������ �ʿ� ���� ��� String���� ���� ��(Generic Type�� ����� ��� String����)
				vec.add(rs.getString("subcategory_id"));
				vec.add(rs.getString("ī�װ���"));
				vec.add(rs.getString("����"));
				
				data.add(vec);
				
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
	
	public String getColumnName(int col) {
		return columnName.get(col);
	}
	
	public int getRowCount() {
		return data.size();
	}

	public int getColumnCount() {
		return columnName.size();
	}

	public Object getValueAt(int row, int col) {
		return data.get(row).get(col);
	}
	
}
