package com.anshu.student_management_system.Repositories;

import com.anshu.student_management_system.Entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByStudentId(Long studentId);
}
