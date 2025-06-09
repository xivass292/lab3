package com.example.javalabaip.service;

import com.example.javalabaip.cache.CacheManager;
import com.example.javalabaip.dto.UserDto;
import com.example.javalabaip.model.User;
import com.example.javalabaip.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CacheManager cacheManager;

    public UserService(UserRepository userRepository, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.cacheManager = cacheManager;
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        String cacheKey = "findAllUsers";
        if (cacheManager.containsUserListKey(cacheKey)) {
            return cacheManager.getUserList(cacheKey);
        }

        List<UserDto> result = userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        cacheManager.putUserList(cacheKey, result);
        return result;
    }

    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        if (cacheManager.containsUserKey(id)) {
            return cacheManager.getUser(id);
        }

        User user = userRepository.findByIdWithLocations(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        UserDto result = convertToDto(user);
        cacheManager.putUser(id, result);
        return result;
    }

    @Transactional(readOnly = true)
    public UserDto findByUsername(String username) {
        String cacheKey = "username:" + username;
        if (cacheManager.containsUserListKey(cacheKey)) {
            List<UserDto> cachedUsers = cacheManager.getUserList(cacheKey);
            if (!cachedUsers.isEmpty()) {
                return cachedUsers.get(0);
            }
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        UserDto result = convertToDto(user);
        cacheManager.putUser(user.getId(), result);
        cacheManager.putUserList(cacheKey, List.of(result));
        return result;
    }

    @Transactional
    public UserDto create(@Valid UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        User savedUser = userRepository.save(user);
        UserDto result = convertToDto(savedUser);
        cacheManager.putUser(savedUser.getId(), result);
        cacheManager.clearUserListCache("findAllUsers");
        return result;
    }

    @Transactional
    public UserDto update(Long id, @Valid UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        String oldUsername = user.getUsername();
        user.setUsername(userDto.getUsername());
        User updatedUser = userRepository.save(user);
        UserDto result = convertToDto(updatedUser);
        cacheManager.removeUser(id);
        cacheManager.putUser(id, result);
        cacheManager.clearUserListCache("findAllUsers");
        cacheManager.clearUserListCache("username:" + oldUsername);
        cacheManager.putUserList("username:" + userDto.getUsername(), List.of(result));
        return result;
    }

    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        String username = user.getUsername();
        userRepository.deleteById(id);
        cacheManager.removeUser(id);
        cacheManager.clearUserListCache("findAllUsers");
        cacheManager.clearUserListCache("username:" + username);
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        return dto;
    }
}