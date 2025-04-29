package com.kcd.pos.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Getter
@Schema(description = "생성/수정 meta 데이터 DTO")
@NoArgsConstructor
public abstract class BaseDto {

    @Schema(description = "생성자 ID", example = "admin")
    private String createdBy;

    @Schema(description = "생성일시", example = "2025-04-28T21:32:00")
    private LocalDateTime createdAt;

    @Schema(description = "생성일시(yyyy-MM-dd HH:mm:ss)", example = "2025-04-28 21:32:00")
    private String createdAtStr;

    @Schema(description = "수정자 ID", example = "admin")
    private String modifiedBy;

    @Schema(description = "수정일시", example = "2025-04-28T21:32:00")
    private LocalDateTime modifiedAt;

    @Schema(description = "수정일시(yyyy-MM-dd HH:mm:ss)", example = "2025-04-28 21:32:00")
    private String modifiedAtStr;

    protected BaseDto(String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt) {
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.createdAtStr = format(createdAt);
        this.modifiedBy = modifiedBy;
        this.modifiedAt = modifiedAt;
        this.modifiedAtStr = format(modifiedAt);
    }

    // 수정용
    protected BaseDto(LocalDateTime modifiedAt, String modifiedBy) {
        this.modifiedBy = modifiedBy;
        this.modifiedAt = modifiedAt;
        this.modifiedAtStr = format(modifiedAt);
    }

    private String format(LocalDateTime dateTime) {
        if (Objects.isNull(dateTime)) return "";
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
