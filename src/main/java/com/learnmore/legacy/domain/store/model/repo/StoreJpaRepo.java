package com.learnmore.legacy.domain.store.model.repo;

import com.learnmore.legacy.domain.store.model.Store;
import com.learnmore.legacy.domain.store.model.enums.StoreType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreJpaRepo extends JpaRepository<Store, Long> {
    List<Store> findAllByStoreType(StoreType storeType);

    Store findByStoreName(String itemName);

    Store findByStoreIdAndStoreType(Long itemId, StoreType storeType);
}
