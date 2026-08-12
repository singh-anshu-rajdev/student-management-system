package com.anshu.student_management_system.Service.ServiceImpl;

import com.anshu.student_management_system.DTO.RegisterDTO;
import com.anshu.student_management_system.Entities.UserEntity;
import com.anshu.student_management_system.Repositories.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserEntityServiceImpl implements UserDetailsService {

    @Autowired
    UserEntityRepository userEntityRepository;

    public UserEntity registerUser(RegisterDTO registerDTO){
        UserEntity user = new UserEntity();
        user.setUserName(registerDTO.getUserName());
        user.setPassword(registerDTO.getPassword());
        user.setRole(registerDTO.getRole());
        return userEntityRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository.findByUserName(username).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
    }
}
