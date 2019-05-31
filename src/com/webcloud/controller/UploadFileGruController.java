package com.webcloud.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
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
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.webcloud.biz.FileforgruBiz;
import com.webcloud.entity.Fileforgru;
import com.webcloud.entity.Fileforusr;

@Controller
@RequestMapping("gfile")
public class UploadFileGruController {
	@Autowired
	CommonsMultipartResolver multipartResolver;
	
	@Autowired
	private FileforgruBiz fileforgrubiz;
	
	//�ļ��ϴ�
	@RequestMapping("upload")
	public String upload(@RequestParam("file") CommonsMultipartFile files[],Fileforgru fileforgru,HttpServletRequest request,HttpServletResponse response,HttpSession session)
			throws IllegalStateException,IOException{
				session.removeAttribute("msg");
				int uid = (Integer)session.getAttribute("uid");
	            int gid = (Integer)session.getAttribute("gid");
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
				            fileforgru.setGid(gid);
				            fileforgru.setGfilename(myFileName);
				            fileforgru.setGfilenameSave(fileName);
				            fileforgru.setUfilformat(fileExtName);
				            fileforgru.setGuploadtime(new Date());
				            String subpath = path.substring(path.indexOf("upload")+7);
				            fileforgru.setGpathSave(subpath);
				            BigDecimal size = new BigDecimal(files[i].getSize());
				            fileforgru.setGfilsize(size);
				            fileforgru.setIsrecycle(false);
				            fileforgru.setGuploadusrid(uid);
				            fileforgru.setGdownloadtimes((short) 0);
				            fileforgrubiz.uploadFile(fileforgru);
				            System.out.println("�浽���ݿ��idΪ"+fileforgru.getGfileid());
				        } catch (Exception e) {
				            e.printStackTrace();
				            session.setAttribute("msg", "�ļ��ϴ�ʧ�ܣ�������");
				            return "groupupload";
				        }
				    }else{
				    	session.setAttribute("msg","��ѡ���ļ�");
				        return "groupupload";
				        }
				    }
				    //��¼�ϴ����ļ����ʱ��
				int finaltime = (int) System.currentTimeMillis();
				System.out.println(finaltime - pre);
				    
				return "groupsuccess";
				}
	
	
			//�ļ�����
			@RequestMapping("download")
			public ResponseEntity<byte[]> download(HttpServletRequest request,HttpServletResponse response) 
			throws IllegalStateException,IOException{
				try{
					System.out.println(request.getParameter("key"));
					Fileforgru fileforgru = fileforgrubiz.selectByGFileid(Integer.parseInt(request.getParameter("key")));
					fileforgru.setGdownloadtimes((short) (fileforgru.getGdownloadtimes()+1));
					fileforgrubiz.updateFile(fileforgru);
					String path = request.getSession().getServletContext().getRealPath("/WEB-INF/upload/") + fileforgru.getGpathSave();
					File file = new File(path);
					System.out.println(path);
					HttpHeaders headers = new HttpHeaders();
			        String filename = this.getFilename(request, fileforgru.getGfilename());
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
				Fileforgru fileforgru = fileforgrubiz.selectByGFileid(Integer.parseInt(request.getParameter("key")));
				String path = request.getSession().getServletContext().getRealPath("/WEB-INF/upload/") + fileforgru.getGpathSave();
				File file = new File(path);
				if(file.exists()){
					file.delete();
					fileforgrubiz.deletefile(fileforgru);
				}
				else{
					System.out.print("���ִ���");
				}
				return "redirect:/groupfile/recycle.do";
			}
			
}
