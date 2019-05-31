package com.webcloud.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.webcloud.biz.FileforusrBiz;
import com.webcloud.entity.Fileforusr;


@Controller
@RequestMapping("file")
public class UploadController {
	@Autowired
	CommonsMultipartResolver multipartResolver;
	
	@Autowired
	private FileforusrBiz fileforusrbiz;
	
	
	//�ļ��ϴ�
		@RequestMapping("upload")
		public String upload(@RequestParam("file") CommonsMultipartFile files[],Fileforusr fileforusr,HttpServletRequest request,HttpServletResponse response,HttpSession session)
		throws IllegalStateException,IOException{
			session.removeAttribute("msg");
		    //��¼�ϴ�������ʼʱ��ʱ�䣬���������ϴ�ʱ��
		    int pre = (int) System.currentTimeMillis();
			for(int i=0;i<files.length;i++){
			    if(!files[i].isEmpty()){
			        //ȡ�õ�ǰ�ϴ��ļ����ļ�����
			        String myFileName = files[i].getOriginalFilename();
			        System.out.println(myFileName);
			        //�������ϴ�����ļ���
			        String fileName = UUID.randomUUID().toString() + "_"  + files[i].getOriginalFilename();
			        //��ȡ�ļ���׺��
			        String fileExtName = myFileName.substring(myFileName.lastIndexOf(".")+1);
			        //�����ϴ�·��
			        String path = request.getSession().getServletContext().getRealPath("/WEB-INF/upload/") + fileName;
			        System.out.println(path);
			        File localFile = new File(path);
			        try {
			        	files[i].transferTo(localFile);
	                    //�������ݿ�
	                    int uid = (Integer)session.getAttribute("uid");
	                    fileforusr.setUid(uid);
	                    fileforusr.setUfilename(myFileName);
	                    fileforusr.setUfilenameSave(fileName);
	                    fileforusr.setUfileformat(fileExtName);
	                    fileforusr.setUploadtime(new Date());
	                    String subpath = path.substring(path.indexOf("upload")+7);
	                    fileforusr.setUpathSave(subpath);
	                    BigDecimal size = new BigDecimal(files[i].getSize());
	                    fileforusr.setUsize(size);
	                    fileforusr.setIsrecycle(false);
	                    fileforusr.setIsshare(false);
	                    fileforusrbiz.uploadFile(fileforusr);
	                    System.out.println("�浽���ݿ��idΪ"+fileforusr.getUfileid());
	                 } catch (Exception e) {
	                	 e.printStackTrace();
	                	 session.setAttribute("msg", "�ļ��ϴ�ʧ�ܣ�������");
	                     return "upload";
	                     }
			        }
			    else{
			    	session.setAttribute("msg","��ѡ���ļ�");
	                return "upload";
	                }
			    }
			//��¼�ϴ����ļ����ʱ��
			int finaltime = (int) System.currentTimeMillis();
			System.out.println(finaltime - pre);
			return "success";
		}
		
		
		//��ȡ�ļ���С
		public static String getPrintSize(long size) {	
			// ����ֽ�������1024����ֱ����BΪ��λ�������ȳ���1024����3λ��̫��������	
			double value = (double) size;		
			if (value < 1024) {
				return String.valueOf(value) + "B";		
			}
			else {	
				value = new BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();	
			}		
			// ���ԭ�ֽ�������1024֮������1024�������ֱ����KB��Ϊ��λ		
			// ��Ϊ��û�е���Ҫʹ����һ����λ��ʱ��		
			// ����ȥ�Դ�����		
			if (value < 1024) {		
				return String.valueOf(value) + "KB";		
			} 
			else {	
				value = new BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();	
				}		
			if (value < 1024) {	
				return String.valueOf(value) + "MB";
				} 
			else {	
					// �������Ҫ��GBΪ��λ�ģ��ȳ���1024����ͬ���Ĵ���	
					value = new BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();	
					return String.valueOf(value) + "GB";		
			}	
		}
		
		
		//�ļ�����
		@RequestMapping("download")
		public ResponseEntity<byte[]> download(HttpServletRequest request,HttpServletResponse response) 
		throws IllegalStateException,IOException{
			try{
				Fileforusr fileforusr = fileforusrbiz.selectByUfileID(Integer.parseInt(request.getParameter("key")));
				String path = request.getSession().getServletContext().getRealPath("/WEB-INF/upload/") + fileforusr.getUpathSave();
				File file = new File(path);
				System.out.println(path);
				HttpHeaders headers = new HttpHeaders();
		        String filename = this.getFilename(request, fileforusr.getUfilename());
				headers.setContentDispositionFormData("attachment", filename);
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers, HttpStatus.OK);
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}		
		}
		
		//���ص��ļ����Ʋ�Ҫ����
		public String getFilename(HttpServletRequest request,String filename) throws Exception{
	        String[] IEBrowserKeyWord = {"MSIE","Trident","Edge"};
	        String userAgent = request.getHeader("User-Agent");
	        for(String keyWord:IEBrowserKeyWord){
	            if(userAgent.contains(keyWord)){
	                return URLEncoder.encode(filename,"UTF-8");
	            }
	        }
	        return new String(filename.getBytes("UTF-8"),"ISO-8859-1");
	    }
		
		//�ļ��ӷ�������ɾ��
		@RequestMapping("deltotal")
		public String delete(HttpServletRequest request){
			Fileforusr fileforusr = fileforusrbiz.selectByUfileID(Integer.parseInt(request.getParameter("key")));
			String path = request.getSession().getServletContext().getRealPath("/WEB-INF/upload/") + fileforusr.getUpathSave();
			File file = new File(path);
			if(file.exists()){
				file.delete();
				fileforusrbiz.deletefile(fileforusr);
			}
			else{
				System.out.print("���ִ���");
			}
			return "redirect:/main/recycle.do";
		}
}
