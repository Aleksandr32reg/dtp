package com.example.dtp.mapper;

import com.example.dtp.dto.DtpDto;
import com.example.dtp.entity.DtpEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface DtpMapper {

    List<DtpDto> toDtoList(List<DtpEntity> dtpEntities);

    DtpEntity toDtpEntity(DtpDto addressAndDtpDto);

    DtpDto toDtpDto(DtpEntity dtpEntity);

    void updateDtpEntity(DtpDto source, @MappingTarget DtpEntity target);

}
