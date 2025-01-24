package com.jobportal.JobPortal.Controller.API;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jobportal.JobPortal.Service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class RESTController {

    @Autowired
    private final MainService service;

    @GetMapping("/todoke/kouketsu")
    public String getOASubject(@RequestParam(value = "key" ,required = false) String key){
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        System.out.println(key);
        String json;
        try {
            json = mapper.writeValueAsString(service.getOASubject());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    @PutMapping("/todoke/kouketsu")
    public void putOASubject(@RequestParam(value = "key" ,required = false) String key, @RequestBody(required = false) OASubject subject){
        service.putOASubject(subject);
        //System.out.println(subject);
    }
}
