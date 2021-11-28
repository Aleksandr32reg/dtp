package com.example.dtp.controller;

import com.example.dtp.dto.DtpDto;
import com.example.dtp.entity.DtpEntity;
import com.example.dtp.service.DtpOperationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/controllerDtp/")
public class DtpController {
    private final DtpOperationsService dtpOperationsService;

    @GetMapping("/dtp/all")
    public List<DtpDto> getAllDtp(){
        return dtpOperationsService.getAllDtp();
    }
    @GetMapping("/dtp/{id}")
    public DtpDto getDtpById(@PathVariable("id") UUID id) {
        return dtpOperationsService.getDtpById(id);
    }

    @PostMapping("/dtp/create")
    public DtpEntity createDtp(@RequestBody DtpDto dtpDto) {
        return dtpOperationsService.createDtp(dtpDto);
    }
    @PostMapping("/dtp/update")
    public DtpEntity updateDtp(@RequestBody UUID id, @RequestBody DtpDto dtpDto) { return dtpOperationsService.updateDtp(id,dtpDto); }
}
