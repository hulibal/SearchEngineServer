package org.ParserHtml;

public class WebInfoBean {
	private String wb_id;
	private String wb_name;   //html������(����ֱ��get)
	private String wb_address;//���ڱ��ص�html��ַ(����ֱ��get)
	private String wb_url;    //ȡ��ҳ�е�����url
	private String wb_title;  //ȡtitle��ǩ������
	private String wb_time;   //ȡ��ҳ�е�ʱ����Ϣ
	private String wb_sort;
	private long wb_size;
	private int state;        //ȥ�غ�״̬��0����ɾ��   ��1��δ��ɾ��
	private String wb_content;  //ȡ��ҳ����������

	public String getWb_content() {
		return wb_content;
	}

	public void setWb_content(String wb_content) {
		this.wb_content = wb_content;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getWb_id() {
		return wb_id;
	}

	public void setWb_id(String wb_id) {
		this.wb_id = wb_id;
	}

	public String getWb_url() {
		return wb_url;
	}

	public void setWb_url(String wb_url) {
		this.wb_url = wb_url;
	}
	public String getWb_name() {
		return wb_name;
	}

	public void setWb_name(String wb_name) {
		this.wb_name = wb_name;
	}

	public String getWb_title() {
		return wb_title;
	}

	public void setWb_title(String wb_title) {
		this.wb_title = wb_title;
	}
	public String getWb_time() {
		return wb_time;
	}

	public void setWb_time(String wb_time) {
		this.wb_time = wb_time;
	}
	public String getWb_address() {
		return wb_address;
	}

	public void setWb_address(String wb_address) {
		this.wb_address = wb_address;
	}

	public String getWb_sort() {
		return wb_sort;
	}

	public void setWb_sort(String wb_sort) {
		this.wb_sort = wb_sort;
	}

	public long getWb_size() {
		return wb_size;
	}

	public void setWb_size(long web_size) {
		this.wb_size = web_size;
	}

}
