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
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    @Autowired
    private final StandRepository standRepository;

    @Autowired
    private final ExtinctRepository extinctRepository;

    @Autowired
    private final UserRepository userRepository;

    @Override
    public @NonNull List<Stand> searchStands(String query, String sortBy){
        return standRepository.searchStands(query, sortBy);

    }

    @Override
    public @NonNull List<Extinct> searchExtincts(String query, String sortBy){
        return extinctRepository.searchExtincts(query, sortBy);
    }

    @Override
    public @NonNull List<User> searchUsers(String query){
        return userRepository.searchUsers(query);
    }
}
