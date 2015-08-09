package org.apache.lucene.demo;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Servlet_CrawlerIndex
 */
@WebServlet("/Servlet_CrawlerIndex")
public class Servlet_CrawlerIndex extends HttpServlet {
	private static final long serialVersionUID = 1L;

	Timer timer;
	long seconds = 1000;//自动执行的时间间隔，单位为秒
	
    public Servlet_CrawlerIndex() {
        super();
        timer = new Timer();//初始化
    }

	public void init(ServletConfig config) throws ServletException {
		//指定任务执行的时间，定时运行
		xmlReader xmlreader = new xmlReader();//用于SearchEngine_cfg.xml配置文件中获取参数的类
		//int day_of_month = getDate("day_of_month");//日期
		int hour_of_day = xmlreader.getDate("hour_of_day");//时
		int minute = xmlreader.getDate("minute");//分
		int second = xmlreader.getDate("second");//秒
		
		/*****每隔规定时间间隔自动执行******
		timer.schedule(new AutoRunTask(), 1000, seconds*1000);//每隔second秒执行一次
		//*/
		
		/*****每到规定日期和时间自动执行*******/
		Calendar calendar = Calendar.getInstance();
		//设置时间
		//calendar.set(Calendar.DAY_OF_MONTH, day_of_month);//设定运行时间中的日期分量
		calendar.set(Calendar.HOUR_OF_DAY, hour_of_day);//设定运行时间中的小时分量
		calendar.set(Calendar.MINUTE, minute);//设定运行时间中的分钟分量
		calendar.set(Calendar.SECOND, second);//设定运行时间中的秒分量
		Date time = calendar.getTime();
		//按照设置的时间开始执行
		timer.schedule(new AutoRunTask(), time);//每到规定的时间自动运行程序
		//*/
	}	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//DO NOTHING
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//DO NOTHING
	}

	public void destroy() {
		timer.cancel();//终止Timer线程
	}
}
