package com.webcloud.biz;

import java.util.List;

import com.webcloud.entity.Fileforusr;
import com.webcloud.entity.Shareforusr;

public interface ShareforusrBiz {

	//��ʾĿǰ�ķ���
	public List<Shareforusr> selectByUid(Integer uid);
	
	//����һ������
	public int insertshare(Shareforusr Shareforusr);
	
	//�鿴���ļ��Ƿ�����
	public Shareforusr selectByUfileid(Integer ufileid);
	
	//�鿴�����ļ�
	public Shareforusr showshare(String uuid);
	
	//ɾ����������
	public int deleteshare(Shareforusr Shareforusr);
}
