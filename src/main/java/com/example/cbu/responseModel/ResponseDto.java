package com.example.cbu.responseModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    private Integer statusCode;
    private String message;
    private Object data;

    public static ResponseDto getSuccess(int i, String success) {
        return new ResponseDto(i,success,null);
    }
    public static ResponseDto getSuccess(Object object) {
        return new ResponseDto(200,"success", object);
    }

}

