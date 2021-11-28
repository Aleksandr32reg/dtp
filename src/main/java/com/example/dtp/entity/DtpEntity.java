package com.example.dtp.entity;

import com.example.dtp.enums.punishmentClass;
import lombok.*;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "dtps")
public class DtpEntity extends BaseEntity {

        private String driverLicense;
        private String region;
        private String town;
        private String district;
        private String street;
        private String location;
        private LocalDateTime timeOfDtp;
        private Boolean active;
        private punishmentClass punishment;
        private Double penalty;
        private Double period;

}
