package com.game.service;

import com.game.controller.PlayerCreationRequest;
import com.game.controller.PlayerOrder;
import com.game.controller.PlayerUpdateRequest;
import com.game.entity.*;
import com.game.repository.RpgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    /*   public List<Player> findAll(Pageable pageable) {
           return rpgRepository.findAll(pageable).stream().collect(Collectors.toList());
       }*/

    public List<Player> findAll(Pageable pageable) {
        return rpgRepository.findAll(pageable).stream().collect(Collectors.toList());
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
            PlayerOrder order) {

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
       /* Pageable pageable;
        int pageNumber = Integer.parseInt(params.getOrDefault("pageNumber", "0"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "3"));
        if (params.containsKey("order")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, (ShipOrder.valueOf(params.get("order"))).getFieldName());
        } else {
            pageable = PageRequest.of(pageNumber, pageSize);
        }*/
      /*  if (pageNumber != null) {
            specification.add(new SearchCriteria("pageNumber", race, SearchOperation.EQUAL));
        }
        if (pageSize != null) {
            specification.add(new SearchCriteria("pageSize", race, SearchOperation.EQUAL));
        }*/


        List<Player> playerList = rpgRepository.findAll(specification);
        if (after != null) {
            playerList.removeIf(x -> x.getBirthday() < after);
        }
        if (before != null) {
            playerList.removeIf(x -> x.getBirthday() > before);
        }


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

/*    public List<Player> findByParams(Map<String, String> params) {
        String name = (String) params.getOrDefault("name", null);
        String title = (String) params.getOrDefault("title", null);
        Race race = params.containsKey("race") ? Race.valueOf((String) params.get("race")) : null;
        Calendar calendar = Calendar.getInstance();
        Date after = null;
        if (params.containsKey("after")) {
            after = new Date(Long.parseLong(params.get("after")));
        }
        Date before = null;
        if (params.containsKey("before")) {
            before = new Date(Long.parseLong(params.get("before")));
        }
        Boolean banned = params.containsKey("banned") ? Boolean.parseBoolean(params.get("banned")) : null;
        Double minExperience = params.containsKey("minExperience") ? Double.parseDouble(params.get("minExperience")) : null;
        Double maxExperience = params.containsKey("maxExperience") ? Double.parseDouble(params.get("maxExperience")) : null;
        Integer minLevel = params.containsKey("minLevel") ? Integer.parseInt(params.get("minLevel")) : null;
        Integer maxLevel = params.containsKey("maxLevel") ? Integer.parseInt(params.get("maxLevel")) : null;

        Pageable pageable;
        int pageNumber = Integer.parseInt(params.getOrDefault("pageNumber", "0"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "3"));

        if (params.containsKey("order")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, (PlayerOrder.valueOf(params.get("order"))).getFieldName());
        } else {
            pageable = PageRequest.of(pageNumber, pageSize);
        }

        return rpgRepository.findAllByParams(name, title, race, after, before, banned, minExperience, maxExperience, minLevel, maxLevel, pageable).stream().collect(Collectors.toList());
    }*/

//    public List<Player> findByParams(Map<String, String> params) {
//        String name = (String) params.getOrDefault("name", null);
//        String title = (String) params.getOrDefault("title", null);
//        Race race = params.containsKey("race") ? Race.valueOf((String) params.get("race")) : null;
//
//        return rpgRepository.findAllByParams(name, title);
//    }


//        {
//            "name": "Arman",
//                "title": "armyan",
//                "race": "GIANT",
//                "profession": "WARRIOR",
//                "birthday": 28,
//                "banned": false,
//                "experience": 0
//       }


//    public ResponseEntity<List<Player>> getPlayerList(PlayerRequest playerRequest) {
//
//        Player playerToFind = new Player();
//        playerToFind.setName(name);
//        Example<Player> example = Example.of(playerToFind);
//
//        return new ResponseEntity<>(rpgRepository.findAll(example), HttpStatus.OK);
//    }
}
