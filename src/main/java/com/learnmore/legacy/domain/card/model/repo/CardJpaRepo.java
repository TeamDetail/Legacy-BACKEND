package com.learnmore.legacy.domain.card.model.repo;

import com.learnmore.legacy.domain.card.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardJpaRepo extends JpaRepository<Card, Long> {
    List<Card> findAllByRegionAttribute_AttributeName(String region);

    Card findByRuins_RuinsId(Long ruinsId);

    List<Card> findByNationAttribute_NationAttributeId(Long nationId);
    List<Card> findByNationAttribute_NationAttributeIdIn(List<Long> nationIds);
    List<Card> findByLineAttribute_LineAttributeId(Long lineId);
    List<Card> findByLineAttribute_LineAttributeIdIn(List<Long> lineIds);
    List<Card> findByRegionAttribute_RegionAttributeId(Long regionId);
    List<Card> findByRegionAttribute_RegionAttributeIdIn(List<Long> regionIds);


}