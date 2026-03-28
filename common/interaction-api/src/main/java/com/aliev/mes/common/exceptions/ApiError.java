package com.aliev.mes.common.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static com.aliev.mes.common.util.Constants.DATE_TIME_PATTERN;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiError {
    String error;
    String message;
    String reason;
    String status;

    @Builder.Default
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    LocalDateTime timestamp = LocalDateTime.now();
}
