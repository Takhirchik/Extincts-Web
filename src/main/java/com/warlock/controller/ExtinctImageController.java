package com.warlock.controller;

import com.warlock.factory.ExtinctImageFactoryDTO;
import com.warlock.factory.ExtinctsFactoryDTO;
import com.warlock.model.request.CreateExtinctImageRequest;
import com.warlock.model.response.ExtinctImageResponse;
import com.warlock.service.ExtinctImageService;
import com.warlock.service.ExtinctService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/extinctsImages")
@RequiredArgsConstructor
public class ExtinctImageController {

    @Autowired
    private final ExtinctImageService extinctImageService;

    @Autowired
    private final ExtinctService extinctService;

    @Autowired
    private final ExtinctImageFactoryDTO extinctImageFactoryDTO;

    @Autowired
    private final ExtinctsFactoryDTO extinctsFactoryDTO;

    @GetMapping(produces=APPLICATION_JSON_VALUE)
    public List<ExtinctImageResponse> findAll(){
        return extinctImageService.findAll()
                .stream()
                .map(extinctImageFactoryDTO::fromEntityToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{extinctImageId}", produces=APPLICATION_JSON_VALUE)
    public ExtinctImageResponse findById(@PathVariable Long extinctImageId){
        return extinctImageFactoryDTO.fromEntityToDTO(extinctImageService.findById(extinctImageId));
    }

    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ExtinctImageResponse create(@RequestBody CreateExtinctImageRequest request) {
        var extinctImage = extinctImageFactoryDTO.fromDTOToEntity(request);
        var extinct = extinctService.findById(request.getExtinctId());
        extinctImage.setExtinct(extinct);

        var createdExtinctImage = extinctImageService.createExtinctImage(extinctImage);

        return extinctImageFactoryDTO.fromEntityToDTO(createdExtinctImage);
    }

    @PatchMapping(value = "/{extinctImageId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ExtinctImageResponse update(@PathVariable Long extinctImageId, @RequestBody CreateExtinctImageRequest request) {
        var extinctImage = extinctImageFactoryDTO.fromDTOToEntity(request);
        var extinctId = request.getExtinctId();
        extinctImage.setExtinct(extinctId != null ? extinctService.findById(extinctId) : null);

        var updatedExtinctImage = extinctImageService.update(extinctImageId, extinctImage);
        return extinctImageFactoryDTO.fromEntityToDTO(updatedExtinctImage);
    }

    @DeleteMapping(value = "/{extinctImageId}", produces = APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Long extinctImageId) {
        extinctImageService.delete(extinctImageId);
    }
}
