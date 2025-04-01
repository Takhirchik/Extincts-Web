package com.warlock.service.util;

import com.warlock.domain.Extinct;
import com.warlock.domain.Stand;
import com.warlock.domain.User;
import lombok.NonNull;

import java.util.List;

public interface SearchService {
    @NonNull
    List<Stand> searchStands(String query, String sortBy);

    @NonNull
    List<Extinct> searchExtincts(String query, String sortBy);

    @NonNull
    List<User> searchUsers(String query);
}
