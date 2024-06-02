package com.projeta.api;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class Problem {
  private Integer status;
  private LocalDateTime hora;
  private String detalhes;

    @Getter
    @Builder
    public static class Object {
        private String name;
        private String userMessage;
    }
}
