package com.webcloud.biz;

import java.util.List;

import com.webcloud.entity.Fileforusr;

public interface FileforusrBiz {
	//���Ҹ������������ļ�
	public List<Fileforusr> selectByUid(Integer uid);
	
	//�鿴����վ
	public List<Fileforusr> selectByUidr(Integer uid);
	
	//�ϴ��ļ�
	public int uploadFile(Fileforusr fileforusr);
	
	//����idѡ���ļ�
	public Fileforusr selectByUfileID(Integer ufileid);
	
	//�����ļ�״̬
	public int updateFile(Fileforusr fileforusr);
	
	//ɾ���ļ�
	public int deletefile(Fileforusr fileforusr);
	
	//�����ļ�
	public List<Fileforusr> selectByName(Fileforusr fileforusr);
}
