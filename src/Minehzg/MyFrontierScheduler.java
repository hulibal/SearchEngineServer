package Minehzg;

import org.archive.crawler.datamodel.CandidateURI;

import org.archive.crawler.postprocessor.FrontierScheduler;

public class MyFrontierScheduler extends FrontierScheduler {
	private static final long serialVersionUID = 1l;	

	public MyFrontierScheduler(String name) {
		super(name);
	}

	protected void schedule(CandidateURI caUri) {
		String uri = caUri.toString();
		if (uri.contains("bupt")||uri.contains("byr")) {
			System.out.println("submitting link: "+uri);		
			getController().getFrontier().schedule(caUri);			
		}
	}
}