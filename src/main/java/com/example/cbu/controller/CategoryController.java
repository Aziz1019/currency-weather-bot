package com.example.cbu.controller;

import com.example.cbu.responseModel.ResponseDto;
import com.example.cbu.service.impl.ServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final ServiceFactory service;

    @GetMapping("{serviceName}/{code}")
    public ResponseDto getCurrency(@PathVariable Integer code, @PathVariable String serviceName) {
        return service.findService(serviceName).findByCode(code);
    }

    @GetMapping("{composite}/{code}")
    public ResponseDto getCompositeCurrency(@PathVariable Integer code, @PathVariable String composite) {
        return service.findService(composite).findByCode(code);
    }

    @PostMapping("/refresh-currency/{serviceName}")
    public ResponseDto save(@PathVariable String serviceName){
        service.findService(serviceName).save();
        return ResponseDto.getSuccess(200,"ok");
    }
}
