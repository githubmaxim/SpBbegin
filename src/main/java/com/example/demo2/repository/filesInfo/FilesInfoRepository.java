package com.example.demo2.repository.filesInfo;

import com.example.demo2.entity.filesInfo.FilesInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesInfoRepository extends JpaRepository<FilesInfo, Integer> {
}

