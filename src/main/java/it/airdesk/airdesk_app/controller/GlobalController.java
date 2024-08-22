package it.airdesk.airdesk_app.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;

@ControllerAdvice
public class GlobalController { //most proper class to handle the index mapping, as a facade controller
    
    @GetMapping("/")
    public String getIndex() {
        return "index.html";
    }



}
