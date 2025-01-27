package com.jobportal.JobPortal.Controller.API;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jobportal.JobPortal.Service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
public class RESTController {

    @Autowired
    private final MainService service;
    @Value("${securityKey}")
    private String securityKey;
    /*
    * パラメーターのキーが正しければ、公欠反映テーブルに存在する公欠日の内、反映フラグがfalseのものを一件返す。
    * */
    @GetMapping("/todoke/kouketsu")
    public String getOASubject(@RequestParam(value = "key") String key){
        if(!checkKey(key)) return "Invalid key";
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        String json;
        try {
            json = mapper.writeValueAsString(service.getOASubject());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return json;
    }
    /*
     * パラメーターのキーが正しければ、公欠システムに反映した公欠日の反映フラグをtrueにする。
     * */
    @PutMapping("/todoke/kouketsu")
    public String putOASubject(@RequestParam(value = "key") String key, @RequestBody(required = false) OASubject subject){
        if(!checkKey(key)) return "Invalid key";
        service.putOASubject(subject);
        return "success";
    }
    //キーが正しいかチェックする。正しければtrueを返す。
    public boolean checkKey(String key){
        return securityKey.equals(key);
    }
}
