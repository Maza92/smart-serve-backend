package com.example.demo.dto.unit;

import com.example.demo.enums.UnitTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class UnitDto {
    private Integer id;
    private String name;
    private String abbreviation;
    private UnitTypeEnum unitType;
}
