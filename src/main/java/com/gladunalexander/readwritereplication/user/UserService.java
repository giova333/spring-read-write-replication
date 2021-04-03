package com.gladunalexander.readwritereplication.user;

import com.gladunalexander.readwritereplication.configuration.DataSourceType;
import com.gladunalexander.readwritereplication.configuration.annotation.Datasource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    @Transactional
    @Datasource(type = DataSourceType.WRITE)
    public User save(User user) {
        return repository.save(user);
    }

    @Transactional(readOnly = true)
    @Datasource(type = DataSourceType.READ)
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }
}
