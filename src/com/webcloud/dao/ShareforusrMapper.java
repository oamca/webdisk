package com.webcloud.dao;

import java.util.List;

import com.webcloud.entity.Shareforusr;
import com.webcloud.entity.ShareforusrKey;

public interface ShareforusrMapper {
    int deleteByPrimaryKey(ShareforusrKey key);

    int insert(Shareforusr record);

    int insertSelective(Shareforusr record);

    Shareforusr selectByPrimaryKey(ShareforusrKey key);

    int updateByPrimaryKeySelective(Shareforusr record);

    int updateByPrimaryKey(Shareforusr record);
    
    //�����б�
    public List<Shareforusr> selectByUid(Integer uid);
    
    //�����ļ�id�鿴������Ϣ
    public Shareforusr selectByUfileid(Integer uid);
    
    //����uuid�鿴�����ļ�
    public Shareforusr selectByToken(String uuid); 
}