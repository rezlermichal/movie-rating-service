package com.github.movierating.service;

import com.github.movierating.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> getCurrentUser();

}
