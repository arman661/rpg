package com.game.repository;

import com.game.entity.Player;
import com.game.entity.PlayerEntitySpecification;
import com.game.entity.Race;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RpgRepository extends JpaRepository<Player, Long>, CrudRepository<Player, Long>,
        JpaSpecificationExecutor<Player>{
    Optional<Player> findById(Long id);
}
