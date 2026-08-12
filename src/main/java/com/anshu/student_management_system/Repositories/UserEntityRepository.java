package com.anshu.student_management_system.Repositories;

import com.anshu.student_management_system.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByUserName(String userName);

    boolean existsByUserName(String userName);
}
