package com.warlock.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.warlock.service.StandService;
import com.warlock.model.request.CreateStandRequest;
import com.warlock.model.response.StandResponse;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/stands")
@RequiredArgsConstructor
public class StandController {
    private final StandService standService;

    @GetMapping(produces=APPLICATION_JSON_VALUE)
    public List<StandResponse> findAll(){
        return standService.findAll();
    }

    @GetMapping(value = "/{standId}", produces=APPLICATION_JSON_VALUE)
    public StandResponse findById(@PathVariable Long standId){
        return standService.findById(standId);
    }

    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public StandResponse create(@RequestBody CreateStandRequest request) {
        return standService.createStand(request);
    }

    @PatchMapping(value = "/{standId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public StandResponse update(@PathVariable Long standId, @RequestBody CreateStandRequest request) {
        return standService.update(standId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{standId}", produces = APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Long standId) {
        standService.delete(standId);
    }
}
