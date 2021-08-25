package com.example.demo2.repository.filesInfo;

import com.example.demo2.entity.filesInfo.FilesInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FilesInfoRepository extends JpaRepository<FilesInfo, Integer> {
    @Modifying(clearAutomatically = true) //добавляется для создания модифицирующих БД запросов с параметром обновления данных после удаления (иначе при тестировании будет показывать, что данные не удалены)
    @Query("delete from FilesInfo f where f.name = ?1")
    @Transactional     //Модифицирующие запросы должны быть транзакционными.
                       // @Transactional(timeout = 10) //это приводит к тому, что метод становится транзакционным + запускается с таймаутом в 10 секунд
                       //и без флага readOnly. !!! При этом все стандартные методы в репозитории уже являются тразакционными!!!
    void deleteByName(String name); //тип в модифицирующем БД запросе может быть только "void/int/Integer"
}

