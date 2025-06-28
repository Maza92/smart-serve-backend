package com.example.demo.service.parameter;

import java.math.BigDecimal;

public interface IParameterService {
    void loadParameters();

    String getString(String key);

    BigDecimal getBigDecimal(String key);
}
