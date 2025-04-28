package com.warlock.service;

import com.warlock.domain.Extinct;
import com.warlock.domain.Stand;
import com.warlock.domain.User;
import com.warlock.repository.ExtinctRepository;
import com.warlock.repository.StandRepository;
import com.warlock.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "search")
public class SearchServiceImpl implements SearchService {

    @Autowired
    private final StandRepository standRepository;

    @Autowired
    private final ExtinctRepository extinctRepository;

    @Autowired
    private final UserRepository userRepository;

    @Cacheable(key = "'stands:' + #query + ':' + #sortBy")
    @Override
    public @NonNull List<Stand> searchStands(String query, String sortBy){
        return standRepository.searchStands(query, sortBy);

    }

    @Cacheable(key = "'extincts:' + #query + ':' + #sortBy")
    @Override
    public @NonNull List<Extinct> searchExtincts(String query, String sortBy){
        return extinctRepository.searchExtincts(query, sortBy);
    }

    @Cacheable(key = "'users:' + #query")
    @Override
    public @NonNull List<User> searchUsers(String query){
        return userRepository.searchUsers(query);
    }
}
