package com.game.controller;

import com.game.entity.*;
import com.game.service.RpgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RpgController {
    @Autowired
    private RpgService rpgService;


    @DeleteMapping("/rest/players/{id}")
    public ResponseEntity<Player> deletePlayer(@PathVariable("id") String id) {
        try {
            Long newLong = Long.parseLong(id.toString());
            if (newLong <= 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            return rpgService.deletePlayer(newLong);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


    }

    @PostMapping(value = "/rest/players", consumes = "application/json")
    public ResponseEntity<Player> createPlayer(@RequestBody PlayerCreationRequest playerCreationRequest) {

        return rpgService.createPlayer(playerCreationRequest);

    }

    @PostMapping("/rest/players/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Object id, @RequestBody PlayerUpdateRequest playerUpdateRequest) {
        try {
            Long newLong = Long.parseLong(id.toString());
            if (newLong <= 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            return rpgService.updatePlayer(id, playerUpdateRequest);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/rest/players/count")
    public Integer getPlayersCount(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Race race,
            @RequestParam(required = false) Profession profession,
            @RequestParam(required = false) Long after,
            @RequestParam(required = false) Long before,
            @RequestParam(required = false) Boolean banned,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience,
            @RequestParam(required = false) Integer minLevel,
            @RequestParam(required = false) Integer maxLevel) {
        return rpgService.getPlayersCount(
                name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);
    }

    @GetMapping("/rest/players")
    public ResponseEntity<List<Player>> getPlayerList(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Race race,
            @RequestParam(required = false) Profession profession,
            @RequestParam(required = false) Long after,
            @RequestParam(required = false) Long before,
            @RequestParam(required = false) Boolean banned,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience,
            @RequestParam(required = false) Integer minLevel,
            @RequestParam(required = false) Integer maxLevel,
            @RequestParam(required = false) PlayerOrder order,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize) {
        if (name == null &&
                title == null &&
                race == null &&
                profession == null &&
                after == null &&
                before == null &&
                banned == null &&
                minExperience == null &&
                maxExperience == null &&
                minLevel == null &&
                maxLevel == null &&
                order == null &&
                pageNumber == null &&
                pageSize == null) {
            return new ResponseEntity<List<Player>>(rpgService.findAll(PageRequest.of(0, 3)), HttpStatus.OK);

        } else {
            return new ResponseEntity<List<Player>>(rpgService.getPlayerList(
                    name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel, order, pageNumber, pageSize), HttpStatus.OK);
        }
    }

    @GetMapping("/rest/players/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable Object id) {
        try {
            Long newLong = Long.parseLong(id.toString());
            if (newLong <= 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            return rpgService.getPlayer(newLong);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }



}

//curl -d "param1=value1&param2=value2" -X POST http://localhost/data
//curl -d "name=¬ ­" -X GET http://localhost/rest/players
//RequestBody
