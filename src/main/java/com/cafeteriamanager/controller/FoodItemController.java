package com.cafeteriamanager.controller;

import com.cafeteriamanager.exception.FoodItemNotFoundException;
import com.cafeteriamanager.service.api.FoodItemServiceApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cafeteriamanager.dto.FoodItemDTO;
import com.cafeteriamanager.exception.AlreadyExistingFoodItemException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(FoodItemController.baseUrl)
@Slf4j
@AllArgsConstructor
public class FoodItemController {

    static final String baseUrl = "/foodItem";
    @Autowired
    private FoodItemServiceApi foodItemServiceApi;

    @Operation(description = "AddFOOD item in the item List", summary = "Post the Food item",
            responses = {
                @ApiResponse(description = "Success", responseCode = "200",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = FoodItemDTO.class))),
                @ApiResponse(description = "Client Error", responseCode = "4XX",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = Error.class))) }

    )

    @PostMapping("/addFoodItem")
    public FoodItemDTO CreateFoodItemMethod(@RequestBody @Valid FoodItemDTO foodItemDTO)
            throws AlreadyExistingFoodItemException {
        log.info("Entering foodItemDTO() Controller ");
        FoodItemDTO foodItem = foodItemServiceApi.createFoodItem(foodItemDTO);
        log.info("Leaving foodItemDTO() Controller");
        return foodItem;

    }

    @Operation(description = "Retrieves a list of all food items in the system.",
            summary = "Retrieves a list of all food items",
            responses = {
                @ApiResponse(description = "Success", responseCode = "200",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = FoodItemDTO.class))),
                @ApiResponse(description = "Client Error", responseCode = "4XX",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = FoodItemDTO.class))) })

    @GetMapping("/Items")
    public List<FoodItemDTO> ViewItemDTOMethod() throws FoodItemNotFoundException {
        log.info("Entering ViewItemDTOMethod() Controller");
        List<FoodItemDTO> itemsDTO = foodItemServiceApi.viewAllItem();
        log.info("Leaving ViewItemDTOMethod() Controller");
        return itemsDTO;

    }
    @Operation(
            description = "Update an existing food item",
            summary = "Update food item",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = FoodItemDTO.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Client Error",
                            responseCode = "4XX",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = FoodItemDTO.class)
                            )
                    ),

            }
    )


    @PutMapping("/update/{id}")
    public FoodItemDTO UpdateItemMethod(@PathVariable Long id, @RequestBody @Valid FoodItemDTO itemDTO)
            throws FoodItemNotFoundException {
        log.info("Entering UpdateItemMethod() Controller");
        FoodItemDTO foodItemDTO = foodItemServiceApi.updateFoodItem(id, itemDTO);
        log.info("Leaving UpdateItemMethod() Controller");
        return foodItemDTO;

    }
    @Operation(
            description = "delete food item by Id",
            summary = "delete food item",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = FoodItemDTO.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Client Error",
                            responseCode = "4XX",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = FoodItemDTO.class)
                            )
                    ),

            }
    )


    @DeleteMapping("/delete/{id}")
    public String deleteItemMethod(@PathVariable Long id) throws FoodItemNotFoundException {
        log.info("Entering deleteItemMethod() Controller");
        foodItemServiceApi.deleteFoodItemById(id);
        log.info("Leaving deleteItemMethod() Controller");
        return "ITEM DELETED";
    }

    @Operation(
            description = "retrieve food item by Id",
            summary = "retrieveItemById",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = FoodItemDTO.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Client Error",
                            responseCode = "4XX",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = FoodItemDTO.class)
                            )
                    ),

            }
    )


    @GetMapping("/find/{id}")
    public FoodItemDTO retrieveItemById(@PathVariable Long id) throws FoodItemNotFoundException {
        log.info("Entering retrieveItem() Controller");
        FoodItemDTO itemDTO = foodItemServiceApi.retrieveById(id);
        log.info("Leaving retrieveItem() Controller");

        return itemDTO;

    }
    @Operation(
            description = "retrieve food item by Name",
            summary = "retrieveItemByName",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = FoodItemDTO.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Client Error",
                            responseCode = "4XX",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = FoodItemDTO.class)
                            )
                    ),

            }
    )


    @GetMapping("/find-name")
    public FoodItemDTO retrieveItemByName(@RequestParam(name = "name") String name) throws FoodItemNotFoundException {
        log.info("Entering retrieveItemByName() Controller");
        FoodItemDTO itemDTO = foodItemServiceApi.retrieveByName(name);
        log.info("Leaving retrieveItemByName() Controller");
        return itemDTO;

    }



}
