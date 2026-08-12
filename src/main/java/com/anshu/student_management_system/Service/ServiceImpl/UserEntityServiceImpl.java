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

    public RegistrationResponseDTO registerUser(RegistrationRequestDTO registerDTO){
        UserEntity user = new UserEntity();
        user.setUserName(registerDTO.getUserName());
        user.setPassword(registerDTO.getPassword());
        user.setRole(registerDTO.getRole());
        user = userEntityRepository.save(user);
        RegistrationResponseDTO response = new RegistrationResponseDTO();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole());
        response.setStatusMessage("User Registered Successfully");
        return response;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository.findByUserName(username).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
    }
}
