package com.example.restip.controller;

import com.example.restip.dao.DataRepository;
import com.example.restip.model.IpData;
import com.example.restip.dto.CountriesDto;
import com.example.restip.validator.IpCountriesValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class IpController {
    private static final String IP_API_BATCH_URL = "http://ip-api.com/batch";
    private final DataRepository dataRepository;
    IpData ipData = new IpData();


    public IpController(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }


    @GetMapping(value = "/countriesbatch", produces = "application/json")
    public ResponseEntity<?> getCountriesBatch(@RequestParam List<String> ip) throws URISyntaxException {

        ResponseEntity<?> validationResponse = IpCountriesValidator.validateIpAddresses(ip);
        if (validationResponse != null) {
            return validationResponse;
        }

        List<IpData> ipDataList = new ArrayList<>();
        CountriesDto countriesDto = new CountriesDto();

        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(IP_API_BATCH_URL);
        ResponseEntity<List> result = restTemplate.postForEntity(uri, ip, List.class);
        if (result.getStatusCode().is2xxSuccessful()) {
            List responseBody = result.getBody();
            for (Object responseElement : responseBody) {
                Map ipInfo = (Map) responseElement;
                String countryName = (String) ipInfo.get("country");
                Double latitude = (Double) ipInfo.get("lat");
                String ipQuery = (String) ipInfo.get("query");
                if (countryName != null && !countryName.isEmpty() && latitude != null && latitude > 0) {
                    countriesDto.add(countryName);
                    System.out.println(result);
                    ipData.setIp(ipQuery);
                    ipData.setCountryName(countryName);
                    ipDataList.add(ipData);
                }

                if (!ipDataList.isEmpty() && !dataRepository.checkExist(ipDataList)) {
                    dataRepository.insertAll(ipDataList);
                    System.out.println(dataRepository.checkExist(ipDataList));
                }
            }
        }

        return ResponseEntity.ok(countriesDto);
    }
    @GetMapping(value = "/ipdata", produces = "application/json")
    public ResponseEntity<List<IpData>> getAllIpData() {
        List<IpData> ipDataList = dataRepository.findAll();
        return ResponseEntity.ok(ipDataList);
    }
}