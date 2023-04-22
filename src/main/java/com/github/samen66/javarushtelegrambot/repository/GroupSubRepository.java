package com.github.samen66.javarushtelegrambot.repository;

import com.github.samen66.javarushtelegrambot.entity.GroupSub;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Repository
public interface GroupSubRepository extends JpaRepository<GroupSub, Integer> {

}
