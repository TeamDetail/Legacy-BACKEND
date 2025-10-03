package com.learnmore.legacy.domain.store.model.repo;

import com.learnmore.legacy.domain.store.model.Store;
import com.learnmore.legacy.domain.store.model.enums.StoreType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreJpaRepo extends JpaRepository<Store, Long> {
    List<Store> findAllByStoreType(StoreType storeType);
}
