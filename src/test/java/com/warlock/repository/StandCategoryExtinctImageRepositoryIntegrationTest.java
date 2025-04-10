//package com.warlock.repository;
//
//import com.warlock.domain.Extinct;
//import com.warlock.domain.Stand;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class StandCategoryExtinctImageRepositoryIntegrationTest {
//
//    @Autowired
//    private StandRepository standRepository;
//
//    @Autowired
//    private ExtinctRepository extinctRepository;
//
//    private Stand testStand;
//
//    private Extinct testExtinct;
//
//    @BeforeEach
//    public void setUp(){
//
//        testStand = new Stand();
//        testStand.setStandName("testStandName");
//        testStand.setDescription("Test Desctiprion:" +
//                "It all tests and only tests" +
//                "Don't be ridiculous by reading it!");
//        standRepository.save(testStand);
//
//        testExtinct = new Extinct();
//        testExtinct.setExtinctName("testExtinctName");
//        testExtinct.setDescription("Test Desctiprion:" +
//                "It all tests and only tests" +
//                "Don't be ridiculous by reading it!");
//        testExtinct.setStand(testStand);
//        extinctRepository.save(testExtinct);
//
//        testExtinctImage = new ExtinctImage();
//        testExtinctImage.setUrlImage("testImage.com");
//        testExtinctImage.setExtinct(testExtinct);
//        extinctImageRepository.save(testExtinctImage);
//    }
//
//    @AfterEach
//    public void tearDown(){
//        extinctImageRepository.delete(testExtinctImage);
//        extinctRepository.delete(testExtinct);
//        standRepository.delete(testStand);
//    }
//
//    @Test
//    public void testFindExtinctImage(){
//        ExtinctImage savedExtinctImage = extinctImageRepository.findById(testExtinctImage.getId()).orElse(null);
//
//        assertNotNull(savedExtinctImage);
//        assertEquals(testExtinctImage.getUrlImage(), savedExtinctImage.getUrlImage());
//        assertEquals(testExtinctImage.getExtinct().getExtinctName(), savedExtinctImage.getExtinct().getExtinctName());
//        assertEquals(testExtinctImage.getExtinct().getDescription(), savedExtinctImage.getExtinct().getDescription());
//        assertEquals(testExtinctImage.getExtinct().getStand().getStandName(),
//                savedExtinctImage.getExtinct().getStand().getStandName());
//        assertEquals(testExtinctImage.getExtinct().getStand().getDescription(),
//                savedExtinctImage.getExtinct().getStand().getDescription());
//
//    }
//
//    @Test
//    public void testUpdateExtinctImage(){
//        testExtinctImage.setUrlImage("updatedImage.com");
//        extinctImageRepository.save(testExtinctImage);
//
//        ExtinctImage updatedExtinctImage = extinctImageRepository.findById(testExtinctImage.getId()).orElse(null);
//
//        assertNotNull(updatedExtinctImage);
//        assertEquals(testExtinctImage.getUrlImage(), updatedExtinctImage.getUrlImage());
//    }
//
//    @Test
//    public void testFindExtinct(){
//        Extinct savedExtinct = extinctRepository.findById(testExtinct.getId()).orElse(null);
//
//        assertNotNull(savedExtinct);
//        assertEquals(testExtinct.getExtinctName(), savedExtinct.getExtinctName());
//        assertEquals(testExtinct.getDescription(), savedExtinct.getDescription());
//        assertEquals(testExtinct.getStand().getStandName(), savedExtinct.getStand().getStandName());
//        assertEquals(testExtinct.getStand().getDescription(), savedExtinct.getStand().getDescription());
//    }
//
//    @Test
//    public void testUpdateExtinct(){
//        testExtinct.setExtinctName("updatedExtinctName");
//        extinctRepository.save(testExtinct);
//
//        Extinct updatedExtinct = extinctRepository.findById(testExtinct.getId()).orElse(null);
//
//        assertNotNull(updatedExtinct);
//        assertEquals(testExtinct.getExtinctName(), updatedExtinct.getExtinctName());
//    }
//
//    @Test
//    public void testFindStand(){
//        Stand savedStand = standRepository.findByStandName(testStand.getStandName()).orElse(null);
//
//        assertNotNull(savedStand);
//        assertEquals(testStand.getDescription(), savedStand.getDescription());
//    }
//
//    @Test
//    public void testUpdateStand(){
//        standRepository.save(testStand);
//
//        Stand updatedStand = standRepository.findByStandName(testStand.getStandName()).orElse(null);
//
//        assertNotNull(updatedStand);
//    }
//}
