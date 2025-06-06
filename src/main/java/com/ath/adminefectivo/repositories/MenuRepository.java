package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.Menu;

public interface MenuRepository extends JpaRepository<Menu, String>, QuerydslPredicateExecutor<Menu> {

}
