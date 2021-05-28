package com.game.service;

import com.game.controller.PlayerCreationRequest;
import com.game.controller.PlayerOrder;
import com.game.controller.PlayerUpdateRequest;
import com.game.entity.*;
import com.game.repository.RpgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RpgService {
    @Autowired
    private RpgRepository rpgRepository;

    public ResponseEntity<Player> getPlayer(Long id) {
        Optional<Player> playerOptional = rpgRepository.findById(id);
        if (playerOptional.isPresent()) {
            return new ResponseEntity<>(playerOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    public List<Player> findAll(Pageable pageable) {
        return rpgRepository.findAll(pageable).stream().collect(Collectors.toList());
    }

    public Integer getPlayersCount(String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel) {
        PlayerEntitySpecification specification = new PlayerEntitySpecification();
        if (name != null) {
            specification.add(new SearchCriteria("name", name, SearchOperation.MATCH));
        }
        if (title != null) {
            specification.add(new SearchCriteria("title", title, SearchOperation.MATCH));
        }
        if (race != null) {
            specification.add(new SearchCriteria("race", race, SearchOperation.EQUAL));
        }
        if (profession != null) {
            specification.add(new SearchCriteria("profession", profession, SearchOperation.EQUAL));
        }
        if (maxExperience != null) {
            specification.add(new SearchCriteria("experience", maxExperience, SearchOperation.LESS_THAN));
        }
        if (banned != null) {
            specification.add(new SearchCriteria("banned", banned, SearchOperation.EQUAL));
        }
        if (minExperience != null) {
            specification.add(new SearchCriteria("experience", minExperience, SearchOperation.GREATER_THAN));
        }
        if (minLevel != null) {
            specification.add(new SearchCriteria("level", minLevel, SearchOperation.GREATER_THAN));
        }
        if (maxLevel != null) {
            specification.add(new SearchCriteria("level", maxLevel, SearchOperation.LESS_THAN));
        }
        List<Player> playerList = rpgRepository.findAll(specification);
        if (after != null) {
            playerList.removeIf(x -> x.getBirthday() < after);
        }
        if (before != null) {
            playerList.removeIf(x -> x.getBirthday() > before);
        }
        playerList.forEach(System.out::println);

        return playerList.size();

    }

    public List<Player> getPlayerList(
            String name,
            String title,
            Race race,
            Profession profession,
            Long after,
            Long before,
            Boolean banned,
            Integer minExperience,
            Integer maxExperience,
            Integer minLevel,
            Integer maxLevel,
            PlayerOrder order,
            Integer pageNumber,
            Integer pageSize) {

        PlayerEntitySpecification specification = new PlayerEntitySpecification();
        if (name != null) {
            specification.add(new SearchCriteria("name", name, SearchOperation.MATCH));
        }
        if (title != null) {
            specification.add(new SearchCriteria("title", title, SearchOperation.MATCH));
        }
        if (race != null) {
            specification.add(new SearchCriteria("race", race, SearchOperation.EQUAL));
        }
        if (profession != null) {
            specification.add(new SearchCriteria("profession", profession, SearchOperation.EQUAL));
        }
        if (maxExperience != null) {
            specification.add(new SearchCriteria("experience", maxExperience, SearchOperation.LESS_THAN));
        }
        if (banned != null) {
            specification.add(new SearchCriteria("banned", banned, SearchOperation.EQUAL));
        }
        if (minExperience != null) {
            specification.add(new SearchCriteria("experience", minExperience, SearchOperation.GREATER_THAN));
        }
        if (minLevel != null) {
            specification.add(new SearchCriteria("level", minLevel, SearchOperation.GREATER_THAN));
        }
        if (maxLevel != null) {
            specification.add(new SearchCriteria("level", maxLevel, SearchOperation.LESS_THAN));
        }
        if (order != null) {
            specification.add(new SearchCriteria("order", race, SearchOperation.EQUAL));
        }


        List<Player> playerList = rpgRepository.findAll(specification);
        if (after != null) {
            playerList.removeIf(x -> x.getBirthday() < after);
        }
        if (before != null) {
            playerList.removeIf(x -> x.getBirthday() > before);
        }

        //    if (pageNumber != null || pageSize != null) {
        if (pageNumber == null) {
            pageNumber = 0;
        }
        if (pageSize == null) {
            pageSize = 3;
        }
        int firstElement = pageNumber * pageSize;
        int a = playerList.size();


        if (playerList.size() < ((pageNumber + 1) * pageSize)) {
            playerList = playerList.subList(firstElement, playerList.size());
        } else {
            playerList = playerList.subList(firstElement, firstElement + pageSize);
        }
        //     }
        playerList.forEach(System.out::println);

        return playerList;
    }

    public ResponseEntity<Player> deletePlayer(Long id) {
        if (!rpgRepository.findById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        rpgRepository.deleteById(id);
        return new ResponseEntity<Player>(HttpStatus.OK);
    }

    public ResponseEntity<Player> createPlayer(PlayerCreationRequest playerCreationRequest) {

        Player player = new Player();

        if (playerCreationRequest.getName() == null
                || playerCreationRequest.getName().trim().equals("")
                || playerCreationRequest.getName().length() > 12
                || playerCreationRequest.getTitle().length() > 30
                || playerCreationRequest.getTitle().trim().equals("")
                || playerCreationRequest.getBirthday() == null
                || playerCreationRequest.getBirthday() < 0
                || playerCreationRequest.getExperience() == null
                || playerCreationRequest.getExperience() < 0
                || playerCreationRequest.getExperience() > 10000000
                || playerCreationRequest.getBirthday() < Timestamp.valueOf("2000-01-01 00:00:01").getTime()
                || playerCreationRequest.getBirthday() > Timestamp.valueOf("3000-12-31 23:59:59").getTime()
        ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            player.setName(playerCreationRequest.getName());
            player.setTitle(playerCreationRequest.getTitle());
            player.setRace(playerCreationRequest.getRace());
            player.setProfession(playerCreationRequest.getProfession());
            player.setBirthday(playerCreationRequest.getBirthday());
            player.setBanned(playerCreationRequest.getBanned());
            player.setExperience(playerCreationRequest.getExperience());

            Integer exp = playerCreationRequest.getExperience();
            Integer level = (int) (Math.sqrt(2500 + (200 * exp)) - 50) / 100;
            Integer untilNextLevel = (50 * (level + 1) * (level + 2)) - exp;
            player.setLevel(level);
            player.setUntilNextLevel(untilNextLevel);

            Player save = rpgRepository.save(player);
            return new ResponseEntity<>(save, HttpStatus.OK);
        }
    }

    public ResponseEntity<Player> updatePlayer(Object id, PlayerUpdateRequest playerUpdateRequest) {

        Long newId = Long.parseLong(id.toString());
        Optional<Player> playerOptional = rpgRepository.findById(newId);
        if (!playerOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Player playerOptionalGet = playerOptional.get();

        if (playerUpdateRequest.getName() != null) {
            playerOptionalGet.setName(playerUpdateRequest.getName());
        }
        if (playerUpdateRequest.getTitle() != null) {
            playerOptionalGet.setTitle(playerUpdateRequest.getTitle());
        }
        if (playerUpdateRequest.getRace() != null) {
            playerOptionalGet.setRace(playerUpdateRequest.getRace());
        }
        if (playerUpdateRequest.getProfession() != null) {
            playerOptionalGet.setProfession(playerUpdateRequest.getProfession());
        }
        if (playerUpdateRequest.getBirthday() != null) {
            if (playerUpdateRequest.getBirthday() < 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            playerOptionalGet.setBirthday(playerUpdateRequest.getBirthday());
        }
        if (playerUpdateRequest.getBanned() != null) {
            playerOptionalGet.setBanned(playerUpdateRequest.getBanned());
        }
        if (playerUpdateRequest.getExperience() != null) {
            if (playerUpdateRequest.getExperience() < 0 || playerUpdateRequest.getExperience() > 10000000) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            playerOptionalGet.setExperience(playerUpdateRequest.getExperience());
            Integer exp = playerUpdateRequest.getExperience();
            Integer level = (int) (Math.sqrt(2500 + (200 * exp)) - 50) / 100;
            Integer untilNextLevel = (50 * (level + 1) * (level + 2)) - exp;
            playerOptionalGet.setLevel(level);
            playerOptionalGet.setUntilNextLevel(untilNextLevel);
        }

        rpgRepository.save(playerOptionalGet);
        return new ResponseEntity<>(playerOptionalGet, HttpStatus.OK);
    }
}
