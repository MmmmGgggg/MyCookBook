package com.mgsoftware.MyCookBook.repository;

import com.mgsoftware.MyCookBook.domain.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
@Repository
public interface UnitRepository extends JpaRepository<Unit, UUID> {

        Unit findByName(String name);

        Set<Unit> findAllByNameIn(List<String> unitNames);
}
