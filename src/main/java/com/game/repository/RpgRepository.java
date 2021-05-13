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

//    List<Player> findAll(PlayerEntitySpecification specification);

    /*  @Query("select u from Player u where "
*//*       + "(:name is not null and u.name like concat('%', :name, '%') or :name is null) ")
        Page<Player> findAllByParams(@Param("name") String name,
           @Param("title") String title,
           @Param("race") Race race,
           @Param("after") Date after,
           @Param("before") Date before,
           @Param("banned") Boolean banned,
           @Param("minExperience") Double minExperience,
           @Param("maxExperience") Double maxExperience,
           @Param("minLevel") Integer minLevel,
           @Param("maxLevel") Integer maxLevel,
           @Param("pageable") Pageable pageable);

    Page<Player> findAll(Pageable pageable);*/

//    List<Player> findAllByParams(@Param("name") String name,
//                                 @Param("planet") String title
//                                 );
}
//where "
//        + "(:name is not null and u.name like concat('%', :name, '%') or :name is null) "
//        + "and (:planet is not null and u.title like concat('%', :title, '%') or :planet is null) "
//        + "and (:race is not null and u.race like :race or :race is null) "
//        + "and (:after is not null and u.prodDate >= :after or :after is null) "
//        + "and (:before is not null and u.prodDate <= :before or :before is null) "
//        + "and (:banned is not null and u.true = :true or :false is null) "
//        + "and (:minExperience is not null and u.experience >= :minExperience or :minExperience is null) "
//        + "and (:maxExperience is not null and u.experience <= :maxExperience or :maxExperience is null) "
//        + "and (:minLevel is not null and u.level >= :minLevel or :minLevel is null) "
//        + "and (:maxLevel is not null and u.level <= :maxLevel or :maxLevel is null)