package com.webcloud.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.webcloud.biz.FileforusrBiz;
import com.webcloud.entity.Fileforusr;

@Controller
@RequestMapping("main")
public class FileforuserController {

	@Autowired
	private FileforusrBiz fileforusrbiz;

	//��ʾ�ļ��б�
	@RequestMapping(value="init")
	public String init(Map map,HttpSession session){
		int uid = (Integer)session.getAttribute("uid");
		session.removeAttribute("msg");
//		System.out.println(uid);
		List<Fileforusr> fileforusrlist = fileforusrbiz.selectByUid(uid);
		try{	//��׽û���ļ������쳣
			System.out.println(fileforusrlist.get(0).getUfilename());
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("fileforusrlist", fileforusrlist);
		return "forward:/index.jsp";
	}

	//��ʾ����վ
	@RequestMapping(value="recycle")
	public String recycle(Map map,HttpSession session) {
		int uid = (Integer)session.getAttribute("uid");
		List<Fileforusr> fileforusrlist = fileforusrbiz.selectByUidr(uid);
		try{
			System.out.println(fileforusrlist.get(0).getUfilename());
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("fileforusrlist", fileforusrlist);
		return "forward:/recycle.jsp";
	}
	
	@RequestMapping(value="del")
	public String del(HttpServletRequest request){
		Fileforusr fileforusr = fileforusrbiz.selectByUfileID(Integer.parseInt(request.getParameter("key")));
		System.out.println(fileforusr.getUfilename());
		fileforusr.setIsrecycle(true);
		fileforusr.setUlastmodtime(new Date());		//���ø����ݿ�������͵���ɾ��ʱ����
		fileforusrbiz.updateFile(fileforusr);
		return "redirect:/main/init.do";
	}
	
	@RequestMapping(value="back")
	public String back(HttpServletRequest request){
		Fileforusr fileforusr = fileforusrbiz.selectByUfileID(Integer.parseInt(request.getParameter("key")));
		System.out.println(fileforusr.getUfilename());
		fileforusr.setIsrecycle(false);
		fileforusr.setUlastmodtime(null);		//���ø����ݿ�������͵���ɾ��ʱ����
		fileforusrbiz.updateFile(fileforusr);
		return "redirect:/main/recycle.do";
	}
	
	//�����ļ�
	@RequestMapping(value="search")
	public String search(Map map,HttpSession session,String filename){
		int uid = (Integer)session.getAttribute("uid");
		Fileforusr fileforusr = new Fileforusr();
		fileforusr.setUid(uid);
		fileforusr.setUfilename(filename);
		List<Fileforusr> fileforusrlist = fileforusrbiz.selectByName(fileforusr);
		for(int i=0;i<fileforusrlist.size();i++){
			if(fileforusrlist.get(i).getIsrecycle()==true){
				fileforusrlist.remove(i);
			}
		}
		try{
			System.out.println(fileforusrlist.get(0).getUfilename());
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("fileforusrlist", fileforusrlist);
		return "forward:/index.jsp";
	}
	
}
