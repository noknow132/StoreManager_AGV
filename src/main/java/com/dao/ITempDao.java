package com.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.entity.Temp;

@Repository 
public interface ITempDao {
    int insert(Temp record);

    int insertSelective(Temp record);
    
    List<Temp> getAllByDb();
}