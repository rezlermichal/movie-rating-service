package com.github.movierating.mapper;

import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Mapper
public interface DateMapper {

    default OffsetDateTime map(LocalDateTime ldt) {
        return Optional.ofNullable(ldt)
                .map(localDateTime -> localDateTime.atZone(ZoneId.systemDefault()).toOffsetDateTime())
                .orElse(null);
    }

}
