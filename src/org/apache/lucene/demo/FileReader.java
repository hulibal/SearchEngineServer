package org.apache.lucene.demo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileReader {
	public FileReader(){//空构造函数
	}
	
	/**********以下功能函数用于Lucene.java中的public void IndexLucene()********************/
	//删除文件夹中原有的内容
	public void delAllFile(String path){
		File file = new File(path);			
		String[] tempList = file.list();
		File temp = null;
		for(int i = 0; i<tempList.length; i++){
			if(path.endsWith(File.separator)){
				temp = new File(path + tempList[i]);
			}else{
				temp = new File(path + File.separator + tempList[i]);
			}
			if(temp.isFile()){
				temp.delete();
			}
			if(temp.isDirectory()){
				delAllFile(path + "/" + tempList[i]);
			}
		}		
	}
	
	/**********以下功能函数用于Lucene.java中的static void indexDocs(,,)********************/
	//判断文档的编码格式
	public String Encoding(InputStream in) throws IOException{
		byte []head=new byte[3];
		String code = "";
		in.read(head);
		//code="gb2312";
		if(head[0]==-1&&head[1]==-2){
			code="UTF-16";
		}
		if(head[0]==-2&&head[1]==-1){
			code="Unicode";
		}
		if(head[0]==-17&&head[1]==-69){
			code="UTF-8";
		}else{
			return code;
		}
		//System.out.println("Encoding:" + code);	
		return code;		
	}
	}
