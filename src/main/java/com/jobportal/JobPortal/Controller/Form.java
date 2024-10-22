package com.jobportal.JobPortal.Controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Form {
    private List<String> parentValue;
    private Map<String, @NotNull List<String>> child;
    @NotBlank
    private String text;
    public Form(){}
//    public String getValue(){
//        return  value;
//    }
//    public void setValue(String value){
//        this.value=value;
//    }
}
