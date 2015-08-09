package org.ParserHtml;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;


public class WebInfoDao {
	private Connection conn;

	public void initConnection() {
		try {
			 Class.forName("com.mysql.jdbc.Driver");
			 String URL = "jdbc:mysql://localhost:3306/info";
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			//String URL = "jdbc:oracle:thin:@localhost:1521:XE";
			String user = "root";
			String password = "root";
			conn = DriverManager.getConnection(URL, user, password);			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertWebInfo(WebInfoBean webInfoBean) throws Exception {
		this.initConnection();
		Statement stat = conn.createStatement();
		String sql = "insert into webpage values(" + webInfoBean.getWb_id()+ "," 
		        + webInfoBean.getWb_name()+","
		        + webInfoBean.getWb_address() + ","
		        + webInfoBean.getWb_url() + ","
				+ webInfoBean.getWb_title() + ","
				+ webInfoBean.getWb_time() + ","
				+ webInfoBean.getWb_sort() + ","
				+ webInfoBean.getWb_size()
				+ ",1,"+ webInfoBean.getWb_content() +")";
		stat.executeUpdate(sql);
		this.closeConnection();
	}

	public void insertWebInfos(List<WebInfoBean> webInfoBeans) throws Exception {
		this.initConnection();
		Statement stat = conn.createStatement();
		WebInfoBean webInfoBean = null;
		for (int i = 0; i < webInfoBeans.size(); i++) {
			webInfoBean = webInfoBeans.get(i);
			String sql = "insert into webpage values("
				+ webInfoBean.getWb_id() + ","
				+ webInfoBean.getWb_name()+","
		        + webInfoBean.getWb_address() + ","
		        + webInfoBean.getWb_url() + ","
				+ webInfoBean.getWb_title() + ","
				+ webInfoBean.getWb_time() + ","
				+ webInfoBean.getWb_sort() + ","
				+ webInfoBean.getWb_size()
				+ ",1,"+ webInfoBean.getWb_content()+")";
			stat.executeUpdate(sql);
		}
		this.closeConnection();
	}
	
	//�Ķ�������ݿ����
	public void updateWebInfos(List<WebInfoBean> webInfoBeans) throws Exception {
		this.initConnection();
		Statement stat = conn.createStatement();
		WebInfoBean webInfoBean = null;
		for (int i = 0; i < webInfoBeans.size(); i++) {
			webInfoBean = webInfoBeans.get(i);
			String sql = "update webpage set wb_url = "
				+ webInfoBean.getWb_url() + ",wb_title = "
				+ webInfoBean.getWb_title() + ",wb_time = "
				+ webInfoBean.getWb_time() + ",wb_content= "
				+ webInfoBean.getWb_content() +" where wb_id = "
				+ webInfoBean.getWb_id() + "";
			//System.out.println(sql);
			stat.executeUpdate(sql);			
		}
		this.closeConnection();
	}

	public Vector<WebInfoBean> getwebpage() throws Exception {
		Vector<WebInfoBean> vector = new Vector<WebInfoBean>();
		this.initConnection();
		Statement stat = conn.createStatement();
		String sql = "select * from webpage where state = 1";
		ResultSet rs = stat.executeQuery(sql);
		while (rs.next()) {
			WebInfoBean webInfo = new WebInfoBean();
			webInfo.setWb_id(rs.getString("wb_id"));
			webInfo.setWb_name(rs.getString("wb_name"));
			webInfo.setWb_address(rs.getString("wb_address"));
			webInfo.setWb_url(rs.getString("wb_url"));
			webInfo.setWb_title(rs.getString("wb_title"));
			webInfo.setWb_time(rs.getString("wb_time"));
			webInfo.setWb_sort(rs.getString("wb_sort"));
			webInfo.setWb_size(rs.getInt("wb_size"));
			webInfo.setState(rs.getInt("state"));
			webInfo.setWb_content(rs.getString("wb_content"));
			vector.add(webInfo);
		}
		this.closeConnection();
		return vector;
	}

	public int getMaxId() {
		int maxId = 0;
		try {
			this.initConnection();
			String sql = "select max(wb_id) from webpage";
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(sql);
			if (rs.next()) {
				maxId = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.closeConnection();
		return maxId;
	}

	public double getallsize() {
		double allsize = 0;
		try {
			this.initConnection();
			String sql = "select sum(wb_size) from webpage";
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(sql);
			if (rs.next()) {
				allsize = rs.getDouble(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.closeConnection();
		return allsize;
	}

	public void deleteWebpage()throws Exception {
		this.initConnection();
		Statement stat = conn.createStatement();
		String sql = "delete from webpage";
		stat.executeUpdate(sql);
		this.closeConnection();
	}
	
	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
		}
	}
}
