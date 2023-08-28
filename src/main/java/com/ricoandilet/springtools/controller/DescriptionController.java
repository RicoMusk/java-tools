package com.ricoandilet.springtools.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: rico
 * @date: 2023/8/21
 *
 * add descriptions for swagger api
 *
 **/
@Tag(name = "DescriptionController", description = "description API")
public class DescriptionController {


    @Operation(summary = "get a description list")
    @GetMapping
    public String getDescList(){

        return null;
    }

}
