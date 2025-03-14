package com.warlock.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.warlock.service.ExtinctService;
import com.warlock.model.request.CreateExtinctRequest;
import com.warlock.model.response.ExtinctResponse;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/extincts")
@RequiredArgsConstructor
public class ExtinctController {
    private final ExtinctService extinctService;

    @GetMapping(produces=APPLICATION_JSON_VALUE)
    public List<ExtinctResponse> findAll(){
        return extinctService.findAll();
    }

    @GetMapping(value = "/{extinctId}", produces=APPLICATION_JSON_VALUE)
    public ExtinctResponse findById(@PathVariable Long extinctId){
        return extinctService.findById(extinctId);
    }

    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ExtinctResponse create(@RequestBody CreateExtinctRequest request) {
        return extinctService.createExtinct(request);
    }

    @PatchMapping(value = "/{extinctId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ExtinctResponse update(@PathVariable Long extinctId, @RequestBody CreateExtinctRequest request) {
        return extinctService.update(extinctId, request);
    }

    @DeleteMapping(value = "/{extinctId}", produces = APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Long extinctId) {
        extinctService.delete(extinctId);
    }
}
