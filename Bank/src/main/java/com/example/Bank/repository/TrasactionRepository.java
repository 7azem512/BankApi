package com.example.Bank.repository;

import com.example.Bank.entity.Trasaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrasactionRepository extends JpaRepository<Trasaction, String> {
}
