package com.example.dtp.dto;

import com.example.dtp.enums.punishmentClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DtpDto {
    private String driverLicense;
    private String region;
    private String town;
    private String district;
    private String location;
    private LocalDateTime timeOfDtp;
    private Boolean active;
    private punishmentClass punishment;
    private Double penalty;
}
