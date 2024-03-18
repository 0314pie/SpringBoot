package net.kdigital.test2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.kdigital.test2.entity.GuestBookEntity;

public interface GuestBookRepository extends JpaRepository<GuestBookEntity, Long> {

}
