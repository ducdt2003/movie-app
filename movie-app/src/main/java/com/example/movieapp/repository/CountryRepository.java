package com.example.movieapp.repository;

import com.example.movieapp.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

    // Kiểm tra xem một quốc gia đã tồn tại theo tên hay chưa
    boolean existsByName(String name);

    // Kiểm tra xem một quốc gia đã tồn tại theo slug hay chưa
    boolean existsBySlug(String slug);

    // Tìm kiếm một quốc gia theo tên
    Optional<Country> findByName(String name);

    // Tìm kiếm một quốc gia theo slug
    Optional<Country> findBySlug(String slug);
}