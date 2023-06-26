package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Demo;

public interface DemoRepository extends JpaRepository<Demo, Long> {
    List<Demo> findByPublished(boolean published);
    List<Demo> findByTitleContaining(String title);
}
