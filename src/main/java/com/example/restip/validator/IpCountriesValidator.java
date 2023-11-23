package com.example.restip.validator;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.regex.Pattern;

public class IpCountriesValidator {

    private static final String IP_REGEX = "\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b";

    public static boolean isValidIpAddress(String ip) {
        return Pattern.matches(IP_REGEX, ip);
    }

    public static boolean hasValidIpAddresses(List<String> ips) {
        for (String ip : ips) {
            if (!isValidIpAddress(ip)) {
                return false;
            }
        }
        return true;
    }

    public static ResponseEntity<?> validateIpAddresses(List<String> ips) {
        if (!hasValidIpAddresses(ips)) {
            return ResponseEntity.badRequest().body("Invalid IP");
        }

        if (ips.size() > 10) {
            return ResponseEntity.badRequest().body("Exceeded maximum limit of 10 IP addresses");
        }

        return null;
    }
}
