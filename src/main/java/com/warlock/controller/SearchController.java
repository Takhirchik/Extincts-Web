package com.warlock.controller;

import com.warlock.mapper.ExtinctMapper;
import com.warlock.mapper.StandsMapper;
import com.warlock.mapper.UserMapper;
import com.warlock.model.shortResponse.ExtinctShortResponse;
import com.warlock.model.shortResponse.StandShortResponse;
import com.warlock.model.shortResponse.UserShortResponse;
import com.warlock.service.StandService;
import com.warlock.service.SearchService;
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

@RestController
@RequestMapping("/search/")
@RequiredArgsConstructor
@Tag(name = "Search API", description = "Поиск Stand, Extinct и User")
public class SearchController {

    @Autowired
    public final StandsMapper standsMapper;

    @Autowired
    public final UserMapper userMapper;

    @Autowired
    public final ExtinctMapper extinctMapper;

    @Autowired
    public final StandService standService;

    @Autowired
    public final SearchService searchService;

    @Operation(
            summary = "Поиск Stand",
            description = "Полнотекстовый поиск Stand с возможностью сортировки",
            parameters = {
                    @Parameter(name = "query", description = "Поисковый запрос", required = true, example = "динозавры"),
                    @Parameter(
                            name = "sortBy",
                            description = "Критерий сортировки",
                            schema = @Schema(
                                    type = "string",
                                    allowableValues = {"relevance", "date", "views"},
                                    defaultValue = "relevance"
                            )
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный поиск",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = StandShortResponse.class))
                            )),
                    @ApiResponse(responseCode = "400", description = "Неверные параметры запроса")
            }
    )
    @GetMapping(value = "/stands", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StandShortResponse>> searchStands(
            @RequestParam(required = true) String query,
            @RequestParam(defaultValue = "relevance") String sortBy
    ) {
        return new ResponseEntity<>(
                searchService.searchStands(query, sortBy)
                        .stream()
                        .map(stand -> {
                            var userShortResponse = userMapper.fromEntityToShortResponse(stand.getCreator());
                            var extinct = standService.findAllExtincts(stand).getFirst();
                            var extinctShortResponse = extinctMapper.fromEntityToShortResponse(
                                    extinct,
                                    userMapper.fromEntityToShortResponse(extinct.getCreator())
                            );
                            return standsMapper.fromEntityToShortResponse(stand, userShortResponse, extinctShortResponse);
                        })
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Поиск Extinct",
            description = "Полнотекстовый поиск Extinct с возможностью сортировки",
            parameters = {
                    @Parameter(name = "query", description = "Поисковый запрос", required = true, example = "тираннозавр"),
                    @Parameter(
                            name = "sortBy",
                            description = "Критерий сортировки",
                            schema = @Schema(
                                    type = "string",
                                    allowableValues = {"relevance", "date", "views", "likes"},
                                    defaultValue = "relevance"
                            )
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный поиск",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ExtinctShortResponse.class))
                            )),
                    @ApiResponse(responseCode = "400", description = "Неверные параметры запроса")
            }
    )
    @GetMapping(value = "/extincts", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ExtinctShortResponse>> searchExtincts(
            @RequestParam(required = true) String query,
            @RequestParam(defaultValue = "relevance") String sortBy
    ) {
        return new ResponseEntity<>(
                searchService.searchExtincts(query, sortBy)
                        .stream()
                        .map(extinct -> {
                            var userShortResponse = userMapper.fromEntityToShortResponse(extinct.getCreator());
                            return extinctMapper.fromEntityToShortResponse(extinct, userShortResponse);
                        })
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }


    @Operation(
            summary = "Поиск User",
            description = "Полнотекстовый поиск User",
            parameters = {
                    @Parameter(name = "query", description = "Поисковый запрос", required = true, example = "ivan")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный поиск",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = UserShortResponse.class))
                            )),
                    @ApiResponse(responseCode = "400", description = "Неверные параметры запроса")
            }
    )
    @GetMapping(value = "/users", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserShortResponse>> searchUsers(@RequestParam(required = true) String query) {
        return new ResponseEntity<>(
                searchService.searchUsers(query)
                        .stream()
                        .map(userMapper::fromEntityToShortResponse)
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }
}
