package Minehzg;

import java.io.File;

import javax.management.InvalidAttributeValueException;

import org.archive.crawler.event.CrawlStatusListener;
import org.archive.crawler.framework.CrawlController;
import org.archive.crawler.framework.exceptions.InitializationException;
import org.archive.crawler.settings.XMLSettingsHandler;

public class StartCrawler_byIDE {
	private String orderFile = null;
	private CrawlController controller = new CrawlController(); // Heritrix的控制器
	CrawlStatusListener listener = null;// 监听器

	public StartCrawler_byIDE() {
		orderFile = "E:/spider/job/order.xml";
	}

	public StartCrawler_byIDE(String orderfile_path) {
		orderFile = orderfile_path;
	}

	public void startCrawler() throws InterruptedException {
		XMLSettingsHandler handler = null; // 读取order.xml文件的处理器
		try {
			File file = new File(orderFile); // order.xml文件
			handler = new XMLSettingsHandler(file);
			handler.initialize();// 读取order.xml中的各个配置
			//
			controller.initialize(handler);// 从读取的order.xml中的各个配置来初始化控制器

			if (listener != null) {
				controller.addCrawlStatusListener(listener);// 控制器添加监听器
			}
			controller.requestCrawlStart();// 开始抓取
			
			/*
			 * 如果Heritrix还一直在运行则等待
			 */
			int i = 0;
			while (true) {
				if (controller.isRunning() == false) {
					break;
				}
				i++;
				System.out.println(i);
				Thread.sleep(1000);
			}

			// 如果Heritrix不再运行则停止
			controller.requestCrawlStop();

		} catch (InvalidAttributeValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void pauseCrawler() throws InterruptedException {
		controller.requestCrawlPause();
	}

	public void resumeCrawler() throws InterruptedException {
		controller.requestCrawlResume();
	}

	public void stopCrawler() throws InterruptedException {
		controller.requestCrawlStop();
	}
}
