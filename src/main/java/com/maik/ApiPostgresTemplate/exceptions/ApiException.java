package com.maik.ApiPostgresTemplate.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ApiException extends RuntimeException{

        private static final long serialVersionUID = 1L;

        private Integer code;
        private String message;

}
