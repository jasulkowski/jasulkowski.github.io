package com.example.restip.dao;

import com.example.restip.model.IpData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpApiRepository extends JpaRepository<IpData, Long> {
    boolean existsByIp(String ip);
}