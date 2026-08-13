package com.anshu.student_management_system.Service.ServiceImpl;

import com.anshu.student_management_system.DTO.RegistrationRequestDTO;
import com.anshu.student_management_system.DTO.RegistrationResponseDTO;
import com.anshu.student_management_system.Entities.UserEntity;
import com.anshu.student_management_system.Repositories.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserEntityServiceImpl implements UserDetailsService {

    @Autowired
    UserEntityRepository userEntityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository.findByUserName(username).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
    }
}
