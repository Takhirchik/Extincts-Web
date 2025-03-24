package com.warlock.controller;

import com.warlock.factory.ExtinctsFactoryDTO;
import com.warlock.service.StandService;
import com.warlock.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.warlock.service.ExtinctService;
import com.warlock.model.request.CreateExtinctRequest;
import com.warlock.model.response.ExtinctResponse;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/extincts")
@RequiredArgsConstructor
public class ExtinctController {

    @Autowired
    private final ExtinctService extinctService;

    @Autowired
    private final StandService standService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final ExtinctsFactoryDTO extinctsFactoryDTO;

    @GetMapping(produces=APPLICATION_JSON_VALUE)
    public List<ExtinctResponse> findAll(){
        return extinctService.findAll()
                .stream()
                .map(extinctsFactoryDTO::fromEntityToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{extinctId}", produces=APPLICATION_JSON_VALUE)
    public ExtinctResponse findById(@PathVariable Long extinctId){
        return extinctsFactoryDTO.fromEntityToDTO(extinctService.findById(extinctId));
    }

    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ExtinctResponse create(@RequestBody CreateExtinctRequest request) {
        var extinct = extinctsFactoryDTO.fromDTOToEntity(request);
        extinct.setStand(standService.findById(request.getStandId()));
        extinct.setUser(userService.findById(request.getUserId()));

        return extinctsFactoryDTO.fromEntityToDTO(extinctService.createExtinct(extinct));
    }

    @PatchMapping(value = "/{extinctId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ExtinctResponse update(@PathVariable Long extinctId, @RequestBody CreateExtinctRequest request) {
        var extinct = extinctsFactoryDTO.fromDTOToEntity(request);
        var standId = request.getStandId();
        extinct.setStand(standId != null ? standService.findById(standId) : null);
        var userId = request.getUserId();
        extinct.setUser(userId != null ? userService.findById(userId) : null);

        return extinctsFactoryDTO.fromEntityToDTO(extinctService.update(extinctId, extinct));
    }

    @DeleteMapping(value = "/{extinctId}", produces = APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Long extinctId) {
        extinctService.delete(extinctId);
    }
}
