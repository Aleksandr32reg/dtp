package com.example.dtp.entity;

import com.example.dtp.enums.PunishmentClass;
import lombok.*;

import javax.persistence.Entity;
import java.time.LocalDateTime;

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
        private PunishmentClass punishment;
        private Double penalty;
        private Double period;

}
