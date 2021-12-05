package com.example.dtp.service;

import com.example.dtp.Repository.DtpRepository;
import com.example.dtp.dto.DtpDto;
import com.example.dtp.dto.LocationDto;
import com.example.dtp.entity.DtpEntity;
import com.example.dtp.enums.PunishmentClass;
import com.example.dtp.mapper.DtpMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DtpOperationsService {
    private final DtpRepository repository;
    private final LocationRepository locationRepository;
    private final DtpMapper mapper;

    public List<DtpDto> getAllDtp() {
        return mapper.toDtpDtoList(repository.findAll());
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

    public DtpEntity updateDtpLocation(UUID id, LocationDto dto) {
        DtpEntity dtp = getDtpEntityById(id);
        dtp.setRegion(dto.getRegion());
        dtp.setTown(dto.getTown());
        dtp.setDistrict(dto.getDistrict());
        dtp.setLocation(dto.getLocation());
        return repository.save(dtp);
    }

    public DtpEntity setPunishment(UUID id, String punishment) {
        DtpEntity dtp = getDtpEntityById(id);
        dtp.setPunishment(PunishmentClass.convert(punishment));
        return repository.save(dtp);
    }

    public DtpEntity setPenalty(UUID id, Double penalty) {
        DtpEntity dtp = getDtpEntityById(id);
        if (dtp.getPunishment().equals(PunishmentClass.PENALTY)) {
            dtp.setPenalty(penalty);
        }
        return repository.save(dtp);
    }

    public DtpEntity setPeriod(UUID id, Double period) {
        DtpEntity dtp = getDtpEntityById(id);
        if (dtp.getPunishment().equals(PunishmentClass.ARRESTING) || dtp.getPunishment().equals(PunishmentClass.LICENSE_DEPRIVATION)) {
            dtp.setPenalty(period);
        }
        return repository.save(dtp);
    }

    public List<DtpDto> getDtpByLocation(LocationDto locationDto) {

        List<DtpEntity> dtpEntities = repository.findAll();
        List<DtpEntity> dtpFiltered = null;

        var region = locationDto.getRegion();
        var town = locationDto.getTown();
        var district = locationDto.getDistrict();
        var street = locationDto.getStreet();
        var location = locationDto.getLocation();

        if (!region.isBlank()) {
            dtpFiltered = dtpEntities.stream().filter(DtpEntity -> DtpEntity.getRegion().equals(region)).collect(Collectors.toList());
        } else if (!town.isBlank()) {
            dtpFiltered = dtpEntities.stream().filter(DtpEntity -> DtpEntity.getTown().equals(town)).collect(Collectors.toList());
        } else if (!district.isBlank()) {
            dtpFiltered = dtpEntities.stream().filter(DtpEntity -> DtpEntity.getDistrict().equals(district)).collect(Collectors.toList());
        } else if (!street.isBlank()) {
            dtpFiltered = dtpEntities.stream().filter(DtpEntity -> DtpEntity.getStreet().equals(street)).collect(Collectors.toList());
        } else if (!location.isBlank()) {
            dtpFiltered = dtpEntities.stream().filter(DtpEntity -> DtpEntity.getLocation().equals(location)).collect(Collectors.toList());
        }

        return mapper.toDtpDtoList(dtpFiltered);
    }

    public List<DtpDto> getDtpByPeriod(LocalDate from, LocalDate to, List<DtpDto> dtpDtoList){
        Timestamp dateFrom = Timestamp.valueOf(from.atTime(LocalTime.MIN));
        Timestamp dateTo = Timestamp.valueOf(to.atTime(LocalTime.MAX));

        List<DtpDto> dtpFiltered = null;
        dtpFiltered = dtpDtoList.stream().filter(DtpDto -> DtpDto.getTimeOfDtp().isAfter(dateFrom.toLocalDateTime())).
                filter(DtpDto -> DtpDto.getTimeOfDtp().isBefore(dateTo.toLocalDateTime())).collect(Collectors.toList());

        return dtpFiltered;
    }

    public double getMidCountDtoByMonth(int year, List<DtpDto> ListDtpDto) {
        Calendar calendarFrom = Calendar.getInstance();
        calendarFrom.set(year, 1, 1, 0, 0, 0);
        Calendar calendarTo = Calendar.getInstance();
        calendarTo.set(year, 12, 31, 23, 59, 59);
        LocalDate localDateFrom = LocalDateTime.ofInstant(calendarFrom.toInstant(), calendarFrom.getTimeZone().toZoneId()).toLocalDate();
        LocalDate localDateTo = LocalDateTime.ofInstant(calendarTo.toInstant(), calendarTo.getTimeZone().toZoneId()).toLocalDate();
        List<DtpDto> ListFiltered = getDtpByPeriod(localDateFrom, localDateTo, ListDtpDto);
        ListFiltered = ListFiltered.stream().filter(DtpDto -> (DtpDto.getPunishment().equals(PunishmentClass.PENALTY) || DtpDto.getPunishment().equals(PunishmentClass.LICENSE_DEPRIVATION) || DtpDto.getPunishment().equals(PunishmentClass.ARRESTING))).collect(Collectors.toList());
        return (double) ListFiltered.size() / 12;
    }

    public String getPunishmentStatistics(List<DtpDto> ListDtpDto) {
        Integer punishmentStatistics[] = new Integer[]{0, 0, 0, 0}; // ???
        for (DtpDto dtpDto : ListDtpDto) {
            if (dtpDto.getPunishment().equals(PunishmentClass.INNOCENT))
                punishmentStatistics[0]++;
            else if (dtpDto.getPunishment().equals(PunishmentClass.PENALTY))
                punishmentStatistics[1]++;
            else if (dtpDto.getPunishment().equals(PunishmentClass.LICENSE_DEPRIVATION))
                punishmentStatistics[2]++;
            else if (dtpDto.getPunishment().equals(PunishmentClass.ARRESTING))
                punishmentStatistics[3]++;
        }
        return String.format("Статистика по решениям за указанный период:\r\nНевиновен: %d \r\nШтраф: %d \r\nЛишение прав: %d \r\nАрест: %d",
                punishmentStatistics[0], punishmentStatistics[1], punishmentStatistics[2], punishmentStatistics[3]);
    }

    public DtpEntity getDtpEntityById(UUID id) {
        Optional<DtpEntity> optionalDtp = repository.findById(id);
        if (optionalDtp.isEmpty()) {
            log.error("getDtpById.out - dtp with ID {} not found", id);
        }
        return optionalDtp.get();
    }
}
