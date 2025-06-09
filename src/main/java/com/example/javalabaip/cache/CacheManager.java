package com.example.javalabaip.cache;

import com.example.javalabaip.dto.LocationResponseDto;
import com.example.javalabaip.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CacheManager {
    private final Map<String, List<UserDto>> userListCache = new HashMap<>();
    private final Map<Long, UserDto> userCache = new HashMap<>();
    private final Map<String, List<LocationResponseDto>> locationListCache = new HashMap<>();
    private final Map<Long, LocationResponseDto> locationCache = new HashMap<>();
    private final Map<String, LocationResponseDto> locationByIpCache = new HashMap<>();

    public void putUserList(String key, List<UserDto> value) {
        userListCache.put(key, value);
    }

    public List<UserDto> getUserList(String key) {
        return userListCache.get(key);
    }

    public boolean containsUserListKey(String key) {
        return userListCache.containsKey(key);
    }

    public void clearUserListCache(String key) {
        userListCache.remove(key);
    }

    public void putUser(Long key, UserDto value) {
        userCache.put(key, value);
    }

    public UserDto getUser(Long key) {
        return userCache.get(key);
    }

    public boolean containsUserKey(Long key) {
        return userCache.containsKey(key);
    }

    public void removeUser(Long key) {
        userCache.remove(key);
    }

    public void putLocationList(String key, List<LocationResponseDto> value) {
        locationListCache.put(key, value);
    }

    public List<LocationResponseDto> getLocationList(String key) {
        return locationListCache.get(key);
    }

    public boolean containsLocationListKey(String key) {
        return locationListCache.containsKey(key);
    }

    public void clearLocationListCache(String key) {
        locationListCache.remove(key);
    }

    public void putLocation(Long key, LocationResponseDto value) {
        locationCache.put(key, value);
    }

    public LocationResponseDto getLocation(Long key) {
        return locationCache.get(key);
    }

    public boolean containsLocationKey(Long key) {
        return locationCache.containsKey(key);
    }

    public void removeLocation(Long key) {
        locationCache.remove(key);
    }

    public void putLocationByIp(String ipAddress, LocationResponseDto value) {
        locationByIpCache.put(ipAddress, value);
    }

    public LocationResponseDto getLocationByIp(String ipAddress) {
        return locationByIpCache.get(ipAddress);
    }

    public boolean containsLocationByIp(String ipAddress) {
        return locationByIpCache.containsKey(ipAddress);
    }

    public void clearLocationByIpCache(String ipAddress) {
        locationByIpCache.remove(ipAddress);
    }

    public void clearAllCache() {
        userListCache.clear();
        userCache.clear();
        locationListCache.clear();
        locationCache.clear();
        locationByIpCache.clear();
    }
}