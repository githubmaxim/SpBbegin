package com.example.demo2.repository.filesInfo;

import com.example.demo2.entity.filesInfo.FilesInfo;
import com.example.demo2.entity.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FilesInfoRepository extends JpaRepository<FilesInfo, Integer> {
    @Modifying //добавляется для создания модифицирующих БД запросов
    @Query("delete from FilesInfo where name = ?1")
    @Transactional     //@Transactional(timeout = 10) //это приводит к тому, что метод становится транзакционным + запускается с таймаутом в 10 секунд
                       //и без флага readOnly. !!! При этом все стандартные методы в репозитории уже являются тразакционными!!!
    void deleteByName(String name); //тип в модифицирующем БД запросе может быть только "void/int/Integer"
}

