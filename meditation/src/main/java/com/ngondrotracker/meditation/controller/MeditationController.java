package com.ngondrotracker.meditation.controller;

import com.ngondrotracker.common.controller.AbstractRestController;
import com.ngondrotracker.common.response.BasicResponse;
import com.ngondrotracker.common.response.ResultResponse;
import com.ngondrotracker.common.util.factory.BasicResponseFactory;
import com.ngondrotracker.common.util.factory.ResultResponseFactory;
import com.ngondrotracker.meditation.controller.request.MeditationCreateRequest;
import com.ngondrotracker.meditation.dto.MeditationDto;
import com.ngondrotracker.meditation.service.MeditationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/meditations")
public class MeditationController extends AbstractRestController {
    @Autowired
    private MeditationService meditationService;

    @PostMapping(path = "/", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize(ADMIN_ROLE)
    public ResponseEntity<BasicResponse> createMeditation(@RequestBody @Valid MeditationCreateRequest request) {
        meditationService.create(request.getTitle(), request.getPath(), request.getGoal());
        BasicResponse response = new BasicResponseFactory().successful();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(path = "/", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse<List<MeditationDto>>> getMeditations() {
        List<MeditationDto> meditations = meditationService.findAll();
        ResultResponse<List<MeditationDto>> response = new ResultResponseFactory<List<MeditationDto>>().successful(meditations);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse<MeditationDto>> getMeditationByPath(@PathVariable(name = "id") String path) {
        MeditationDto meditationDto = meditationService.getByPath(path);
        ResultResponse<MeditationDto> response = new ResultResponseFactory<MeditationDto>().successful(meditationDto);

        return ResponseEntity.ok(response);
    }
}
