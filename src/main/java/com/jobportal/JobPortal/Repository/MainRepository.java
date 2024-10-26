package com.jobportal.JobPortal.Repository;

import com.jobportal.JobPortal.Service.OAMainEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.*;

@Repository
@Mapper
public interface MainRepository {

    @Insert("""
            
            INSERT INTO
            
            
            """)
    void insert(@Param("entity") OAMainEntity entity) ;
}
