package com.example.dtp.service;

import com.example.dtp.Repository.DtpRepository;
import com.example.dtp.dto.DtpDto;
import com.example.dtp.entity.DtpEntity;
import com.example.dtp.enums.punishmentClass;
import com.example.dtp.mapper.DtpMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DtpOperationsService {
    private final DtpRepository repository;
    private final DtpMapper mapper;

    public List<DtpDto> getAllDtp() {
        return mapper.toDtoList(repository.findAll());
    }

    public DtpDto getDtpById(UUID id) {
        return mapper.toDtpDto(getDtpEntityById(id));
    }

    public DtpEntity createDtp(DtpDto dto) {
        return repository.save(mapper.toDtpEntity(dto));
    }

    public DtpEntity updateDtp(UUID id, DtpDto dto) {
        DtpEntity dtp = getDtpEntityById(id);
        dtp.setDriverLicense(dto.getDriverLicense());
        dtp.setRegion(dto.getRegion());
        dtp.setTown(dto.getTown());
        dtp.setDistrict(dto.getDistrict());
        dtp.setLocation(dto.getLocation());
        dtp.setPunishment(dto.getPunishment());
        dtp.setPenalty(dto.getPenalty());
        dtp.setActive(dto.getActive());
        dtp.setTimeOfDtp(dto.getTimeOfDtp());
        return repository.save(dtp);
    }

    public DtpEntity setPunishment(UUID id, String punishment) {
        DtpEntity dtp = getDtpEntityById(id);
        dtp.setPunishment(punishmentClass.convert(punishment));
        return repository.save(dtp);
    }

    public DtpEntity set11metrov(UUID id, DtpDto dto, String elevenMetrov) {
        DtpEntity dtp = getDtpEntityById(id);
        if(dto.getPunishment().equals(punishmentClass.PENALTY)){
            dtp.setPenalty(Double.parseDouble(elevenMetrov));
        }
        return repository.save(dtp);
    }

    public DtpEntity set45minutes(UUID id, DtpDto dto, String fortyFiveMinutes) {
        DtpEntity dtp = getDtpEntityById(id);
        if(dto.getPunishment().equals(punishmentClass.ARRESTING) || dto.getPunishment().equals(punishmentClass.LICENSE_DEPRIVATION)){
            dtp.setPenalty(Double.parseDouble(fortyFiveMinutes));
        }
        return repository.save(dtp);
    }

    public List<DtpDto> getDtpByLocation(String region, String town, String district, String street) {
        List<DtpEntity> dtpEntities = repository.findAll();
        List<DtpEntity> dtpFiltered = null;
        if (!region.isBlank()) {
            dtpFiltered = dtpEntities.stream().filter(DtpEntity -> DtpEntity.getRegion().equals(region)).collect(Collectors.toList());
        } else if (!town.isBlank()) {
            dtpFiltered = dtpEntities.stream().filter(DtpEntity -> DtpEntity.getTown().equals(town)).collect(Collectors.toList());
        } else if (!district.isBlank()) {
            dtpFiltered = dtpEntities.stream().filter(DtpEntity -> DtpEntity.getDistrict().equals(district)).collect(Collectors.toList());
        } else if (!street.isBlank()) {
            dtpFiltered = dtpEntities.stream().filter(DtpEntity -> DtpEntity.getStreet().equals(street)).collect(Collectors.toList());
        }
        return mapper.toDtoList(dtpFiltered);
    }

    public List<DtpDto> getDtpByPeriod(LocalDate from, LocalDate to, List<DtpDto> dtpDtoList){
        Timestamp dateFrom = Timestamp.valueOf(from.atTime(LocalTime.MIN));
        Timestamp dateTo = Timestamp.valueOf(to.atTime(LocalTime.MAX));

        List<DtpDto> dtpFiltered = null;
        dtpFiltered = dtpDtoList.stream().filter(DtpDto -> DtpDto.getTimeOfDtp().isAfter(dateFrom.toLocalDateTime())).
                filter(DtpDto -> DtpDto.getTimeOfDtp().isBefore(dateTo.toLocalDateTime())).collect(Collectors.toList());

        return dtpFiltered;
    }

    public DtpEntity getDtpEntityById(UUID id) {
        Optional<DtpEntity> optionalDtp = repository.findById(id);
        if (optionalDtp.isEmpty()) {
            log.error("getDtpById.out - dtp with ID {} not found", id);
        }
        return optionalDtp.get();
    }
}
