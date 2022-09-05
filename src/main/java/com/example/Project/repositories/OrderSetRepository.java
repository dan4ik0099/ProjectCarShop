package com.example.Project.repositories;

import com.example.Project.model.OrderSet;
import org.hibernate.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface OrderSetRepository extends JpaRepository<OrderSet, Long> {

}
