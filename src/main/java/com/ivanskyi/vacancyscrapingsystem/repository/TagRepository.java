package com.ivanskyi.vacancyscrapingsystem.repository;

import com.ivanskyi.vacancyscrapingsystem.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    @Override
    Tag save(Tag tag);

    @Override
    List<Tag> findAll();
}
