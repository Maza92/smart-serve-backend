package com.example.demo.controller.orden;

import com.example.demo.annotation.AcceptLanguageHeader;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Orden", description = "Orden Operations API")
public interface IOrdenController {

    @AcceptLanguageHeader
    void test();
}