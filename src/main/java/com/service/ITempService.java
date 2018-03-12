package com.service;

import java.util.List;

import com.entity.Temp;

public interface ITempService {
    boolean insert(Temp record);
    
    boolean insertSelective(Temp record);
    
    List<Temp> getAllByDb();
}