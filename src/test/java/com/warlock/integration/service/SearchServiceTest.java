package com.warlock.integration.service;

import com.warlock.domain.Extinct;
import com.warlock.domain.Stand;
import com.warlock.domain.User;
import com.warlock.service.SearchService;
import com.warlock.service.SearchServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test-postgres")
@Transactional
class SearchServiceTest {

    @Autowired
    private SearchService searchService;

    @Test
    void searchStands_ShouldFindByFullText() {
        List<Stand> result = searchService.searchStands("динозавр", "relevance");
        assertThat(result)
            .extracting(Stand::getStandName)
            .contains("Эра Динозавров");
    }

    @Test
    void searchExtincts_ShouldFindByFullText() {
        List<Extinct> result = searchService.searchExtincts("травоядный", "relevance");
        assertThat(result)
            .extracting(Extinct::getExtinctName)
            .contains("Шерстистый Мамонт");
    }

    @Test
    void searchUsers_ShouldFindByFullText() {
        List<User> result = searchService.searchUsers("палеонтолог");
        assertThat(result)
            .extracting(User::getLogin)
            .contains("paleontologist");
    }
}