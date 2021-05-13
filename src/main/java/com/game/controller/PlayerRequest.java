package com.game.controller;


import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public class PlayerRequest {
    private String name;
    private String title;
    private Race race;
    private Profession profession;
    private Long birthday;
    private Boolean  banned;
    private Integer experience;
    //@RequestBod из контролера

}
