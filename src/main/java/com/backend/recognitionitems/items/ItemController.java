package com.backend.recognitionitems.items;

import com.backend.recognitionitems.items.dto.request.AddItemRequestDto;
import com.backend.recognitionitems.items.dto.request.DecreaseItemRequestDto;
import com.backend.recognitionitems.items.dto.request.ItemInDetailRequestDto;
import com.backend.recognitionitems.items.dto.response.*;
import com.backend.recognitionitems.product.dto.request.AddProductRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @Operation(summary = "Create item. Technical staff can.")
    @PostMapping("/item")
    public ResponseEntity<String> addItem(@RequestBody AddItemRequestDto addItemRequestDto) {
        try {
            itemService.addItem(addItemRequestDto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Invalid request: " + ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Decrease item.")
    @PostMapping("/item/decrease")
    public ResponseEntity<String> decreaseItem(@RequestBody DecreaseItemRequestDto decreaseItemRequestDto) {
        try {
            itemService.decreaseItem(decreaseItemRequestDto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Invalid request: " + ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @Operation(summary = "List items.")
    @GetMapping("/item/items/{productId}")
    public ResponseEntity<List<ItemResponseDto>> getAllItems(@PathVariable Long productId) {
        List<ItemResponseDto> items = itemService.getAllItems(productId);
        return ResponseEntity.ok(items);
    }

    @Operation(summary = "Get the top 10 branches with the most sales order by desc.")
    @GetMapping("/item/top-ten-branches/{productId}")
    public ResponseEntity<BranchesResponseDto> getTopTenBranches(@PathVariable Long productId) {
        BranchesResponseDto topTenBranches = itemService.getTopTenBranches(productId);
        return ResponseEntity.ok(topTenBranches);
    }

    @Operation(summary = "Get all branches with the most sales order by desc.")
    @GetMapping("/item/top-all-branches/{productId}")
    public ResponseEntity<BranchesResponseDto> getAllBranches(@PathVariable Long productId) {
        BranchesResponseDto allBranches = itemService.getAllBranches(productId);
        return ResponseEntity.ok(allBranches);
    }

    @Operation(summary = "Get information about the product. Total items, total sold items, total remaining items.")
    @GetMapping("/item/product-all-info/{productId}")
    public ResponseEntity<AllItemsInformationResponseDto> getAllItemsInformation(@PathVariable Long productId) {
        AllItemsInformationResponseDto allInfo = itemService.getAllItemsInformation(productId);
        return ResponseEntity.ok(allInfo);
    }

    @Operation(summary = "Get the top 5 products with the most sales order by desc.")
    @GetMapping("/item/top-five-items/{productId}")
    public ResponseEntity<TopFiveItemsResponseDto> getTopFiveItems(@PathVariable Long productId) {
        TopFiveItemsResponseDto topFiveItems = itemService.getTopFiveItems(productId);
        return ResponseEntity.ok(topFiveItems);
    }

    @Operation(summary = "Get the all products with the most sales order by desc.")
    @GetMapping("/item/top-all-items/{productId}")
    public ResponseEntity<TopAllItemsResponseDto> getTopFiveProducts(@PathVariable Long productId) {
        TopAllItemsResponseDto topAllItems = itemService.getTopAllItems(productId);
        return ResponseEntity.ok(topAllItems);
    }

    @Operation(summary = "Get item in detail. Displays the branches where the item is sold, sorts the remaining amount from less to more.")
    @PostMapping("/item/in-detail/{productId}")
    public ResponseEntity<ItemInDetailResponseDto> getItemInDetail(@PathVariable Long productId,
                                                                  @RequestBody ItemInDetailRequestDto itemInDetailRequestDto) {
        ItemInDetailResponseDto itemInDetail = itemService.getItemInDetail(productId, itemInDetailRequestDto.getItemId());
        return ResponseEntity.ok(itemInDetail);
    }

    @Operation(summary = "Get product logo.")
    @GetMapping("/item/product-logo/{productId}")
    public ResponseEntity<ProductLogoResponseDto> getProductLogo(@PathVariable Long productId) {
        ProductLogoResponseDto productLogo = itemService.getProductLogo(productId);
        return ResponseEntity.ok(productLogo);
    }

    @Operation(summary = "Get product id.")
    @GetMapping("/item/{productOwnerId}")
    public ResponseEntity<ProductIdResponseDto> getProductId(@PathVariable Long productOwnerId) {
        ProductIdResponseDto productId = itemService.getProductId(productOwnerId);
        return ResponseEntity.ok(productId);
    }

    @Operation(summary = "Get branch id.")
    @GetMapping("/item/branch/{technicalStaffId}")
    public ResponseEntity<BranchIdResponseDto> getBranchId(@PathVariable Long technicalStaffId) {
        BranchIdResponseDto branchId = itemService.getBranchId(technicalStaffId);
        return ResponseEntity.ok(branchId);
    }
}
