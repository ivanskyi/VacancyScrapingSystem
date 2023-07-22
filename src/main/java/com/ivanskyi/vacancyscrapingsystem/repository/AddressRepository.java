package com.ivanskyi.vacancyscrapingsystem.repository;

import com.ivanskyi.vacancyscrapingsystem.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    @Override
    Address save(Address entity);

    @Override
    List<Address> findAll();

    Optional<Address> findByName(String name);
}
