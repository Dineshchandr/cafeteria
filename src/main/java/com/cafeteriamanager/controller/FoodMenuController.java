package com.cafeteriamanager.controller;

import java.util.List;

import com.cafeteriamanager.dto.RetrieveFoodItemQuantityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cafeteriamanager.dto.DayWiseMenuDTO;
import com.cafeteriamanager.dto.FoodItemDTO;
import com.cafeteriamanager.dto.FoodMenuDTO;
import com.cafeteriamanager.dto.FoodMenuItemMappingDto;
import com.cafeteriamanager.dto.FoodMenuItemsQuantityDto;
import com.cafeteriamanager.entity.Availability;
import com.cafeteriamanager.exception.AlreadyExistingFoodMenuException;
import com.cafeteriamanager.exception.FoodMenuNotFoundException;
import com.cafeteriamanager.service.api.FoodMenuServiceApi;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(FoodMenuController.bashUrl)
@Slf4j
@AllArgsConstructor
public class FoodMenuController {

    static final String bashUrl = "/foodMenu";

    @Autowired
    private FoodMenuServiceApi foodMenuServiceApi;

    @Operation(description = "Create FoodMenu", summary = "Create FoodMenu",
            responses = {
                @ApiResponse(description = "Success", responseCode = "200",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = FoodItemDTO.class))),
                @ApiResponse(description = "Client Error", responseCode = "4XX",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = FoodItemDTO.class))),

            })

    @PostMapping("/add-menu")
    @ResponseStatus(HttpStatus.CREATED)
    public FoodMenuDTO createFoodMenu(@RequestBody FoodMenuDTO foodMenuDTO) throws AlreadyExistingFoodMenuException {

        log.info("Entering createFoodMenu() Controller ");
        FoodMenuDTO menuDTO = foodMenuServiceApi.createFoodMenu(foodMenuDTO);
        log.info("Leaving createFoodMenu() Controller");
        return menuDTO;

    }

    @Operation(description = "ViewFoodMenu", summary = "ViewFoodMenu",
            responses = {
                @ApiResponse(description = "Success", responseCode = "200",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = FoodItemDTO.class))),
                @ApiResponse(description = "Client Error", responseCode = "4XX",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = FoodItemDTO.class))),

            })

    @GetMapping("/viewMenu")
    public List<FoodMenuDTO> retrieveFoodAllMenu() {
        log.info("Entering retrieveFoodAllMenu() Controller ");
        List<FoodMenuDTO> list = foodMenuServiceApi.retrieveAllFoodMenus();
        log.info("Leaving retrieveFoodAllMenu() Controller");
        return list;

    }

    @Operation(description = "ViewFoodMenuById", summary = "ViewFoodMenuById",
            responses = {
                @ApiResponse(description = "Success", responseCode = "200",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = FoodItemDTO.class))),
                @ApiResponse(description = "Client Error", responseCode = "4XX",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = FoodItemDTO.class))),

            })
    @GetMapping("/find")
    public FoodMenuDTO retrieveMenuById(@RequestParam(name = "id") Long id) throws FoodMenuNotFoundException {
        log.info("Entering retrieveMenuById() Controller ");
        FoodMenuDTO menu = foodMenuServiceApi.retrieveMenuById(id);
        log.info("Leaving retrieveMenuById() Controller");
        return menu;

    }

    @PatchMapping("/update-menu")
    public FoodMenuDTO updateFoodMenu(@RequestParam(name = "id") Long id, @RequestBody FoodMenuDTO foodMenuDTO)
            throws FoodMenuNotFoundException {
        log.info("Entering updateFoodMenu() Controller ");
        FoodMenuDTO menu = foodMenuServiceApi.updateFoodMenu(id, foodMenuDTO);
        log.info("Leaving updateFoodMenu() Controller");
        return menu;

    }

    @PatchMapping("/addItem")
    public FoodMenuItemMappingDto addItemTOMenu(@RequestParam(name = "menuId") Long menuId,
            @RequestParam(name = "itemId") Long itemId) throws FoodMenuNotFoundException {
        log.info("Entering addItemTOMenu() Controller ");
        FoodMenuItemMappingDto foodMenuItemMappingDto = foodMenuServiceApi.addMenuAndItem(menuId, itemId);
        log.info("Leaving addItemTOMenu() Controller");
        return foodMenuItemMappingDto;

    }

    @GetMapping("/viewMenuItem")
    public FoodMenuItemMappingDto viewMenu(@RequestParam(name = "menuId") Long menuId)
            throws FoodMenuNotFoundException {
        FoodMenuItemMappingDto dto = foodMenuServiceApi.retrieveMenuItem(menuId);
        return dto;
    }

    @GetMapping("/day")
    public DayWiseMenuDTO day(@RequestParam(name = "day") String day) {
        return foodMenuServiceApi.retrieveMenuItemByDay(day);
    }

    @DeleteMapping("/item-remove")
    public FoodMenuItemMappingDto removeMenuItemById(@RequestParam(name = "id") Long id) {
        return foodMenuServiceApi.removeMenuItemById(id);
    }

    @PatchMapping("/setAvailability")
    public FoodMenuDTO SetAvailability(@RequestParam(name = "id") Long id, @RequestBody List<Availability> day)
            throws FoodMenuNotFoundException {
        log.info("Entering SetAvailability() Controller ");
        FoodMenuDTO menuDTO = foodMenuServiceApi.SetAvailability(id, day);
        log.info("Leaving SetAvailability() Controller");
        return menuDTO;

    }

    @DeleteMapping("/menu-delete/{id}")
    public String removeMenu(@PathVariable Long id) throws FoodMenuNotFoundException {
        log.info("Entering removeMenu() Controller ");
        foodMenuServiceApi.deleteMenuId(id);
        log.info("Leaving removeMenu() Controller");
        return "MENU IS DELETED SUCCESSFULLY";
    }

    @PatchMapping("/addQuantity")
    public List<FoodMenuItemsQuantityDto> addItemQuantity(@RequestParam(name = "menuId") Long menuId,@RequestParam(name = "itemId") Long itemId,
            @RequestParam(name = "value") Integer quantity) {
        log.info("Entering addItemQuantity() Controller ");
        List<FoodMenuItemsQuantityDto> addItemsQuantity = foodMenuServiceApi.addItemsQuantity(menuId,itemId, quantity);
        log.info("Leaving addItemQuantity() Controller");
        return addItemsQuantity;

    }

    @GetMapping("/day-menu")
    public List<FoodMenuItemMappingDto> retrieveFoodMenuByDay() throws FoodMenuNotFoundException {
        log.info("Entering retrieveFoodMenuByDay() Controller ");
        List<FoodMenuItemMappingDto> retrieveTodayMenu = foodMenuServiceApi.retrieveTodayMenu();
        log.info("Leaving retrieveFoodMenuByDay() Controller");
        return retrieveTodayMenu;
    }

    @GetMapping("/viewQuantity")
    public RetrieveFoodItemQuantityDto retrieveFoodQuantity(@RequestParam(name = "menuId") Long FoodMenuID,
            @RequestParam(name = "itemid") Long FoodItemId) throws FoodMenuNotFoundException {
        log.info("Entering retrieveFoodQuantity() Controller ");
        RetrieveFoodItemQuantityDto quantityDto = foodMenuServiceApi.retrieveMenuQuantity(FoodMenuID, FoodItemId);
        log.info("Leaving retrieveFoodQuantity() Controller");
        return quantityDto;
    }
}
