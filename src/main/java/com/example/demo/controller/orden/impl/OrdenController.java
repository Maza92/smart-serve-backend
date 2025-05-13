package com.example.demo.controller.orden.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.orden.IOrdenController;
import com.example.demo.service.order.IOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orden")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class OrdenController implements IOrdenController {

    private final IOrderService orderService;

    public void test() {

    }
}