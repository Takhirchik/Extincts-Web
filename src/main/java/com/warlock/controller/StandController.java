package com.warlock.controller;

import com.warlock.factory.StandCategoryFactoryDTO;
import com.warlock.factory.StandsFactoryDTO;
import com.warlock.service.StandCategoryService;
import com.warlock.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.warlock.service.StandService;
import com.warlock.model.request.CreateStandRequest;
import com.warlock.model.response.StandResponse;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/stands")
@RequiredArgsConstructor
public class StandController {

    @Autowired
    private final StandService standService;

    @Autowired
    private final StandCategoryService standCategoryService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final StandsFactoryDTO standsFactoryDTO;

    @Autowired
    private final StandCategoryFactoryDTO standCategoryFactoryDTO;

    @GetMapping(produces=APPLICATION_JSON_VALUE)
    public List<StandResponse> findAll(){
        return standService.findAll()
                .stream()
                .map(stand -> {
                    var standCategoryResponse = standCategoryFactoryDTO.fromEntityToDTO(stand.getStandCategory());
                    return standsFactoryDTO.fromEntityToDTO(stand, standCategoryResponse);
                })
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{standId}", produces=APPLICATION_JSON_VALUE)
    public StandResponse findById(@PathVariable Long standId){
        var stand = standService.findById(standId);

        return standsFactoryDTO.fromEntityToDTO(stand,
                standCategoryFactoryDTO.fromEntityToDTO(stand.getStandCategory()));
    }

    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public StandResponse create(@RequestBody CreateStandRequest request) {
        var stand = standsFactoryDTO.fromDTOToEntity(request);
        stand.setUser(userService.findById(request.getUserId()));
        stand.setStandCategory(standCategoryService.findByName(request.getStandCategory().getCategoryName()));

        var createdUser = standService.createStand(stand);

        return standsFactoryDTO.fromEntityToDTO(createdUser,
                standCategoryFactoryDTO.fromEntityToDTO(createdUser.getStandCategory()));
    }

    @PatchMapping(value = "/{standId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public StandResponse update(@PathVariable Long standId, @RequestBody CreateStandRequest request) {
        var stand = standsFactoryDTO.fromDTOToEntity(request);
        var userId = request.getUserId();
        stand.setUser(userId != null ? userService.findById(userId) : null);
        var standCategoryName = request.getStandCategory().getCategoryName();
        stand.setStandCategory(standCategoryName != null ? standCategoryService.findByName(standCategoryName) : null);

        var updatedUser = standService.update(standId, stand);

        return standsFactoryDTO.fromEntityToDTO(updatedUser,
                standCategoryFactoryDTO.fromEntityToDTO(updatedUser.getStandCategory()));
    }

    @DeleteMapping(value = "/{standId}", produces = APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Long standId) {
        standService.delete(standId);
    }
}
