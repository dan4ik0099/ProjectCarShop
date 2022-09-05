package com.example.Project.repositories;

import com.example.Project.model.StashSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StashSetRepository extends JpaRepository<StashSet, Long> {
    List<StashSet> findAllByUserId(Long id);
    void deleteAllByUserId(Long id);
    StashSet findFirstByUserIdAndCarId(Long idUser, Long idCar);


}
