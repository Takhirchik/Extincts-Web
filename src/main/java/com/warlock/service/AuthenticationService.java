package com.warlock.service;

import com.warlock.domain.User;
import lombok.NonNull;

public interface AuthenticationService {
    @NonNull
    String register(@NonNull User request);

    @NonNull
    String authenticate(@NonNull User request);
}
