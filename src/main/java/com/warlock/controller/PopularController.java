package com.warlock.controller;

import com.warlock.mapper.ExtinctMapper;
import com.warlock.mapper.StandsMapper;
import com.warlock.mapper.UserMapper;
import com.warlock.model.shortResponse.ExtinctShortResponse;
import com.warlock.model.shortResponse.StandShortResponse;
import com.warlock.service.StandService;
import com.warlock.service.PopularService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/popular")
@RestController
@RequiredArgsConstructor
@Tag(name = "Popular API", description = "Получение популярных Stand и Extinct")
public class PopularController {

    @Autowired
    private final PopularService popularService;

    @Autowired
    private final StandsMapper standsMapper;

    @Autowired
    private final ExtinctMapper extinctMapper;

    @Autowired
    private final UserMapper userMapper;

    @Autowired
    private final StandService standService;

    @Operation(
            summary = "Получить популярные Stand",
            description = "Возвращает список популярных Stand за указанный период",
            parameters = {
                    @Parameter(
                            name = "period",
                            description = "Период в днях для выборки популярных Stand",
                            example = "7",
                            schema = @Schema(type = "integer", defaultValue = "7")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешно получен список популярных Stand",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = StandShortResponse.class)))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неверный параметр периода"
                    )
            }
    )
    @GetMapping(value = "/stands", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StandShortResponse>> getPopularStands(
            @RequestParam(defaultValue = "7") Integer period
    ) {
        return new ResponseEntity<>(
                popularService.getPopularStands(period)
                        .stream()
                        .map(stand -> {
                            var userShortResponse = userMapper.fromEntityToShortResponse(stand.getCreator());
                            var allExtincts = standService.findAllExtincts(stand);
                            var extinctShortResponse = allExtincts.isEmpty() ? null :
                                    extinctMapper.fromEntityToShortResponse(
                                            allExtincts.getFirst(),
                                            userMapper.fromEntityToShortResponse(allExtincts.getFirst().getCreator())
                                    );
                            return standsMapper.fromEntityToShortResponse(stand, userShortResponse, extinctShortResponse);
                        })
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Получить популярные Extinct",
            description = "Возвращает список популярных Extinct за указанный период",
            parameters = {
                    @Parameter(
                            name = "period",
                            description = "Период в днях для выборки популярных Extinct",
                            example = "7",
                            schema = @Schema(type = "integer", defaultValue = "7")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешно получен список популярных Extinct",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ExtinctShortResponse.class)))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неверный параметр периода"
                    )
            }
    )
    @GetMapping(value = "/extincts", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ExtinctShortResponse>> getPopularExtincts(
            @RequestParam(defaultValue = "7") Integer period
    ) {
        return new ResponseEntity<>(
                popularService.getPopularExtincts(period)
                        .stream()
                        .map(extinct -> {
                            var userShortResponse = userMapper.fromEntityToShortResponse(extinct.getCreator());
                            return extinctMapper.fromEntityToShortResponse(extinct, userShortResponse);
                        })
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }
}
