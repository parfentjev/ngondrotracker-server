package com.ngondrotracker.server.meditation.controller;

import com.ngondrotracker.server.common.controller.AbstractRestController;
import com.ngondrotracker.server.common.exception.ItemAlreadyExistsException;
import com.ngondrotracker.server.common.exception.ItemDoesNotExist;
import com.ngondrotracker.server.common.response.BasicResponse;
import com.ngondrotracker.server.common.response.ResultResponse;
import com.ngondrotracker.server.common.support.factory.BasicResponseFactory;
import com.ngondrotracker.server.common.support.factory.ResultResponseFactory;
import com.ngondrotracker.server.meditation.controller.request.MeditationCreateRequest;
import com.ngondrotracker.server.meditation.controller.request.MeditationGetRequest;
import com.ngondrotracker.server.meditation.model.MeditationDto;
import com.ngondrotracker.server.meditation.service.MeditationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/meditation")
public class MeditationController extends AbstractRestController {
    @Autowired
    private MeditationService meditationService;

    @PostMapping(path = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize(ADMIN_ROLE)
    public ResponseEntity<BasicResponse> create(@RequestBody @Valid MeditationCreateRequest request) {
        BasicResponse response;

        try {
            meditationService.create(request.getTitle(), request.getPath(), request.getGoal());
            response = new BasicResponseFactory().successful();
        } catch (ItemAlreadyExistsException e) {
            response = new BasicResponseFactory().notSuccessful(e.getMessage());
        }

        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT;

        return new ResponseEntity<>(response, status);
    }

    @GetMapping(path = "/getAll", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse<List<MeditationDto>>> getAll() {
        List<MeditationDto> meditations = meditationService.getAll();
        ResultResponse<List<MeditationDto>> response = new ResultResponseFactory<List<MeditationDto>>().successful(meditations);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/get", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse<MeditationDto>> get(@RequestBody @Valid MeditationGetRequest request) {
        ResultResponse<MeditationDto> response;

        try {
            MeditationDto meditationDto = meditationService.getByPath(request.getId());

            response = new ResultResponseFactory<MeditationDto>().successful(meditationDto);
        } catch (ItemDoesNotExist e) {
            response = new ResultResponseFactory<MeditationDto>().notSuccessful(e.getMessage());
        }

        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(response, status);
    }
}
