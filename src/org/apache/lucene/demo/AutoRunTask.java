package org.apache.lucene.demo;

import java.util.Date;
import java.util.TimerTask;

public class AutoRunTask extends TimerTask {
	int i = 0;//全局计数器，记录运行的次数

	@Override
	public void run() {
		//初始化
//		Lucene lucene = new Lucene();
//		Crawler crawler = new Crawler();
//		Htmlparser htmlparser = new Htmlparser();
		
		try{
			if(i == 0){//如果i的值为0，说明为首次运行，需要进行新建索引
				System.out.println("_______爬虫启动________");
				System.out.println((new StringBuilder("时间：")).append(new Date()).toString());
				//crawler.startCrawler();
				
				System.out.println("_______页面分析________");
				System.out.println((new StringBuilder("时间：")).append(new Date()).toString());
				//htmlparser.start();
				
				System.out.println("_______新建索引________");
				System.out.println((new StringBuilder("时间：")).append(new Date()).toString());
				//lucene.IndexLucene("firstRun");//参数为firstRun时表示是首次运行，需要新建索引				
			}
			else if(i == Integer.MAX_VALUE){//如果计数器达到了整数的最大值，则进行归零处理
				i = 0;
			}
			else if(i>0 && i<Integer.MAX_VALUE){//如果计数器为非零值，则表示不是首次运行，需要更新索引
				System.out.println("_______爬虫启动________");
				System.out.println((new StringBuilder("时间：")).append(new Date()).toString());
				//crawler.startCrawler();
				
				System.out.println("_______页面分析________");
				System.out.println((new StringBuilder("时间：")).append(new Date()).toString());
				//htmlparser.start();
								
				System.out.println("_______第" + i++ + "次更新索引________");
				System.out.println((new StringBuilder("时间：")).append(new Date()).toString());
				//lucene.IndexLucene("notFirstRun");//参数为非firstRun是表示要更新现有索引
			}		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			i++;				
		}	

	}

}
