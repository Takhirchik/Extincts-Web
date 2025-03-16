package com.warlock.repository;

import com.warlock.domain.Extinct;
import com.warlock.domain.ExtinctImage;
import com.warlock.domain.Stand;
import com.warlock.domain.StandCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StandCategoryExtinctImageRepositoryIntegrationTest {

    @Autowired
    private StandCategoryRepository standCategoryRepository;

    @Autowired
    private StandRepository standRepository;

    @Autowired
    private ExtinctRepository extinctRepository;

    @Autowired
    private ExtinctImageRepository extinctImageRepository;

    private Stand testStand;

    private Extinct testExtinct;

    private ExtinctImage testExtinctImage;

    @BeforeEach
    public void setUp(){
        StandCategory standCategory = standCategoryRepository.findByCategoryName("Искусство")
                .orElseThrow(() -> new RuntimeException("Stand category Искусство not found"));

        testStand = new Stand();
        testStand.setStandName("testStandName");
        testStand.setDescription("Test Desctiprion:" +
                "It all tests and only tests" +
                "Don't be ridiculous by reading it!");
        testStand.setStandCategory(standCategory);
        standRepository.save(testStand);

        testExtinct = new Extinct();
        testExtinct.setExtinctName("testExtinctName");
        testExtinct.setDescription("Test Desctiprion:" +
                "It all tests and only tests" +
                "Don't be ridiculous by reading it!");
        testExtinct.setStand(testStand);
        extinctRepository.save(testExtinct);

        testExtinctImage = new ExtinctImage();
        testExtinctImage.setUrlImage("testImage.com");
        testExtinctImage.setExtinct(testExtinct);
        extinctImageRepository.save(testExtinctImage);
    }

    @AfterEach
    public void tearDown(){
        extinctImageRepository.delete(testExtinctImage);
        extinctRepository.delete(testExtinct);
        standRepository.delete(testStand);
    }

    @Test
    public void testFindExtinctImage(){
        ExtinctImage savedExtinctImage = extinctImageRepository.findById(testExtinctImage.getId()).orElse(null);

        assertNotNull(savedExtinctImage);
        assertEquals(testExtinctImage.getUrlImage(), savedExtinctImage.getUrlImage());
        assertEquals(testExtinctImage.getExtinct().getExtinctName(), savedExtinctImage.getExtinct().getExtinctName());
        assertEquals(testExtinctImage.getExtinct().getDescription(), savedExtinctImage.getExtinct().getDescription());
        assertEquals(testExtinctImage.getExtinct().getStand().getStandName(),
                savedExtinctImage.getExtinct().getStand().getStandName());
        assertEquals(testExtinctImage.getExtinct().getStand().getDescription(),
                savedExtinctImage.getExtinct().getStand().getDescription());
        assertEquals(testExtinctImage.getExtinct().getStand().getStandCategory().getCategoryName(),
                savedExtinctImage.getExtinct().getStand().getStandCategory().getCategoryName());
    }

    @Test
    public void testUpdateExtinctImage(){
        testExtinctImage.setUrlImage("updatedImage.com");
        extinctImageRepository.save(testExtinctImage);

        ExtinctImage updatedExtinctImage = extinctImageRepository.findById(testExtinctImage.getId()).orElse(null);

        assertNotNull(updatedExtinctImage);
        assertEquals(testExtinctImage.getUrlImage(), updatedExtinctImage.getUrlImage());
    }

    @Test
    public void testFindExtinct(){
        Extinct savedExtinct = extinctRepository.findById(testExtinct.getId()).orElse(null);

        assertNotNull(savedExtinct);
        assertEquals(testExtinct.getExtinctName(), savedExtinct.getExtinctName());
        assertEquals(testExtinct.getDescription(), savedExtinct.getDescription());
        assertEquals(testExtinct.getStand().getStandName(), savedExtinct.getStand().getStandName());
        assertEquals(testExtinct.getStand().getDescription(), savedExtinct.getStand().getDescription());
        assertEquals(testExtinct.getStand().getStandCategory().getCategoryName(),
                savedExtinct.getStand().getStandCategory().getCategoryName());
    }

    @Test
    public void testUpdateExtinct(){
        testExtinct.setExtinctName("updatedExtinctName");
        extinctRepository.save(testExtinct);

        Extinct updatedExtinct = extinctRepository.findById(testExtinct.getId()).orElse(null);

        assertNotNull(updatedExtinct);
        assertEquals(testExtinct.getExtinctName(), updatedExtinct.getExtinctName());
    }

    @Test
    public void testFindStand(){
        Stand savedStand = standRepository.findByStandName(testStand.getStandName()).orElse(null);

        assertNotNull(savedStand);
        assertEquals(testStand.getDescription(), savedStand.getDescription());
        assertEquals(testStand.getStandCategory().getCategoryName(), savedStand.getStandCategory().getCategoryName());
    }

    @Test
    public void testUpdateStand(){
        StandCategory standCategory = standCategoryRepository.findByCategoryName("Технологии")
                .orElseThrow(() -> new RuntimeException("Stand category Технологии not found"));

        testStand.setStandCategory(standCategory);
        standRepository.save(testStand);

        Stand updatedStand = standRepository.findByStandName(testStand.getStandName()).orElse(null);

        assertNotNull(updatedStand);
        assertEquals(testStand.getStandCategory().getCategoryName(), updatedStand.getStandCategory().getCategoryName());
    }
}
