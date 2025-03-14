package com.warlock.controller;

import com.warlock.model.request.CreateExtinctImageRequest;
import com.warlock.model.response.ExtinctImageResponse;
import com.warlock.service.ExtinctImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/extinctsImages")
@RequiredArgsConstructor
public class ExtinctImageController {
    private final ExtinctImageService extinctImageService;

    @GetMapping(produces=APPLICATION_JSON_VALUE)
    public List<ExtinctImageResponse> findAll(){
        return extinctImageService.findAll();
    }

    @GetMapping(value = "/{extinctImageId}", produces=APPLICATION_JSON_VALUE)
    public ExtinctImageResponse findById(@PathVariable Long extinctImageId){
        return extinctImageService.findById(extinctImageId);
    }

    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ExtinctImageResponse create(@RequestBody CreateExtinctImageRequest request) {
        return extinctImageService.createExtinctImage(request);
    }

    @PatchMapping(value = "/{extinctImageId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ExtinctImageResponse update(@PathVariable Long extinctImageId, @RequestBody CreateExtinctImageRequest request) {
        return extinctImageService.update(extinctImageId, request);
    }

    @DeleteMapping(value = "/{extinctImageId}", produces = APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Long extinctImageId) {
        extinctImageService.delete(extinctImageId);
    }
}
