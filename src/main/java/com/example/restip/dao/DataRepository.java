package com.example.restip.dao;

import com.example.restip.model.IpData;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class DataRepository {
    @PersistenceContext
    private EntityManager entityManager;
    private IpApiRepository ipApiRepository;

    public DataRepository(IpApiRepository ipApiRepository) {
        this.ipApiRepository = ipApiRepository;
    }

    @Transactional
    public void insertAll(List<IpData> ipDataList){
        ipApiRepository.saveAll(ipDataList);
    }

    public boolean checkExist(List<IpData> ipDataList) {
        for (IpData ipData : ipDataList) {
            if (ipApiRepository.existsByIp(ipData.getIp())) {
                return true;
            }
        }
        return false;
    }
    public List<IpData> findAll() {
        return ipApiRepository.findAll();
    }


}
