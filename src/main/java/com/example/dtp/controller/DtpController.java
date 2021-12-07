package com.example.dtp.controller;

import com.example.dtp.dto.DtpDto;
import com.example.dtp.dto.LocationDto;
import com.example.dtp.entity.DtpEntity;
import com.example.dtp.service.DtpOperationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


//rta или все dtp?? нейминг в едином стиле плиз по всему проекту
// выгоядит как мешанина разных запросов, сделайте разные контроллеры, статистику в отдельным, круды в отдельный
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/dtp")
public class DtpController {
    private final DtpOperationsService dtpOperationsService;

    @GetMapping("/all")
    public List<DtpDto> getAllRTAs() {
        return dtpOperationsService.getAllDtp(); }

    @GetMapping("/{id}")
    public DtpDto getRTAById(@PathVariable("id") UUID id) {
        return dtpOperationsService.getDtpById(id); }

    //почему метод называется getMidCountDtpByMonth?
    @GetMapping("/mid_dtp_month/{year}")
    public double getMidCountDtpByMonth(@RequestBody LocationDto locationDto, @PathVariable("year") Integer year) {
        return dtpOperationsService.getMidCountDtoByMonth(year, dtpOperationsService.getDtpByLocation(locationDto)); }

    @GetMapping("/punishment_stat")
    public String getMidCountRTAByMonth(@RequestBody LocationDto locationDto) {
        return dtpOperationsService.getPunishmentStatistics(dtpOperationsService.getDtpByLocation(locationDto)); } // оч стремная практика пихать вызовы функций друг в друга
    // у нас делкаративщина, ошибки отлавливать очень сложно и код становится не читабельным, вынесите это в отдельные вызовы внутри сервиса

    //возвращать сущность не гуд, сделайте дто
    @PostMapping("/create")
    public DtpEntity createDtp(@RequestBody DtpDto dto) {
        return dtpOperationsService.createDtp(dto); }

    @PutMapping("/update/{id}")
    public DtpEntity updateDtp(@PathVariable("id") UUID id, @RequestBody DtpDto dto) {
        return dtpOperationsService.updateDtp(id, dto); }

    @PutMapping("/update/location/{id}")
    public DtpEntity updateDtp(@PathVariable("id") UUID id, @RequestBody LocationDto dto) {
        return dtpOperationsService.updateDtpLocation(id, dto); }

    @PostMapping("/set/punishment/{id}")
    public DtpEntity setPunishment(@PathVariable("id") UUID id, @RequestBody String punishment) {
        return dtpOperationsService.setPunishment(id, punishment); }

    //возвращать сущность не гуд, сделайте дто
    @PostMapping("/set/penalty/{id}")
    public DtpEntity setPenalty(@PathVariable("id") UUID id, @RequestBody Double penalty) {
        return dtpOperationsService.setPenalty(id, penalty); }

    //возвращать сущность не гуд, сделайте дто
    @PostMapping("/set/period/{id}")
    public DtpEntity setPeriod(@PathVariable("id") UUID id, @RequestBody Double period) {
        return dtpOperationsService.setPeriod(id, period); }

}
