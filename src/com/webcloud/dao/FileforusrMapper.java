package com.webcloud.dao;

import java.util.List;

import com.webcloud.entity.Fileforusr;
import com.webcloud.entity.FileforusrKey;

public interface FileforusrMapper {
    int deleteByPrimaryKey(FileforusrKey key);

    int insert(Fileforusr record);

    int insertSelective(Fileforusr record);

    Fileforusr selectByPrimaryKey(FileforusrKey key);

    int updateByPrimaryKeySelective(Fileforusr record);

    int updateByPrimaryKey(Fileforusr record);
    
    //��ѯ�ļ�
    public List<Fileforusr> selectByFileName(Fileforusr record);
    
    //�����û�id��ʾ�ļ��б�
    public List<Fileforusr> selectByUid(Integer uid);
    
    //�����û�id��ʾ�����ļ�
    public List<Fileforusr> selectByUidr(Integer uid);
    
    //�����ļ�id
    public Fileforusr selectByUfileID(Integer ufileid);
}