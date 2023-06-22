package com.backend.recognitionitems.items;

import com.backend.recognitionitems.branch.BranchRepository;
import com.backend.recognitionitems.branch.dto.Branch;
import com.backend.recognitionitems.cashiercheckout.CashierCheckoutRepository;
import com.backend.recognitionitems.cashiercheckout.dto.CashierCheckout;
import com.backend.recognitionitems.items.dto.request.AddItemRequestDto;
import com.backend.recognitionitems.items.dto.Item;
import com.backend.recognitionitems.items.dto.request.DecreaseItemRequestDto;
import com.backend.recognitionitems.items.dto.response.*;
import com.backend.recognitionitems.product.ProductRepository;
import com.backend.recognitionitems.product.dto.Product;
import com.backend.recognitionitems.user.UserRepository;
import com.backend.recognitionitems.user.dto.User;
import com.backend.recognitionitems.user.dto.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;
    private final CashierCheckoutRepository cashierCheckoutRepository;
    @Transactional
    public void addItem(AddItemRequestDto addItemRequestDto) {
        Branch branch = branchRepository.findById(addItemRequestDto.getBranchId()).orElse(null);
        if (branch == null) {
            throw new IllegalArgumentException("The branch does not exist!");
        }

        Product product = productRepository.findById(addItemRequestDto.getProductId()).orElse(null);
        if (product == null) {
            throw new IllegalArgumentException("The product does not exist!");
        }
        /*
        * {
  "itemName": "Rexona Roll-on Deodorant",
  "itemTotalAmount": 200,
  "photosZip": [
    0,1,0
  ],
  "branchId": 10000,
  "productId":10001
}
* */
        //isReported kontrolüne gerek yok
        Item existingItem = itemRepository.findByItemName(addItemRequestDto.getItemName()).orElse(null);
        if (existingItem != null) {
            throw new IllegalArgumentException("The item already exist!");
        }

        Item item = Item.builder()
                .itemName(addItemRequestDto.getItemName())
                .itemTotalAmount(addItemRequestDto.getItemTotalAmount())
                .itemRemainingAmount(addItemRequestDto.getItemTotalAmount())
                .itemSoldAmount(0L)
                .isReported(true)
                .photosZip(addItemRequestDto.getPhotosZip())
                .product(product)
                .branch(branch)
                .build();

        itemRepository.save(item);
    }

    @Transactional
    public void decreaseItem(DecreaseItemRequestDto decreaseItemRequestDto) {
        User cashier = userRepository.findById(decreaseItemRequestDto.getCashierId()).orElse(null);
        if (cashier == null) {
            throw new IllegalArgumentException("The cashier does not exist!");
        }
        if (!cashier.getRole().equals(UserType.CASHIER)) {
            throw new IllegalArgumentException("The user is not a cashier!");
        }

        CashierCheckout cashierCheckout = cashierCheckoutRepository.findByCashierId(cashier.getId()).orElse(null);
        if (cashierCheckout == null) {
            throw new IllegalArgumentException("The cashier does not have a cashier checkout and branch!");
        }

        //isReported kontrolüne gerek yok
        Item item = itemRepository.findByItemNameAndBranchId(decreaseItemRequestDto.getItemName(),
                cashierCheckout.getBranch().getId()).orElse(null);
        if (item == null) {
            throw new IllegalArgumentException("The item does not exist or the item is not sold in the branch!");
        }
        if (item.getItemRemainingAmount() == 0) {
            throw new IllegalArgumentException("The item does not have stock!");
        }

        Long soldAmount = item.getItemSoldAmount();
        Long remainingAmount = item.getItemRemainingAmount();
        item.setItemSoldAmount(soldAmount += 1);
        item.setItemRemainingAmount(remainingAmount -= 1);

        if ((item.getItemRemainingAmount() + item.getItemSoldAmount()) != item.getItemTotalAmount()) {
            throw new IllegalArgumentException("Error occurs when decreasing item stock!");
        }

        itemRepository.save(item);
    }

    @Transactional
    public List<ItemResponseDto> getAllItems(Long productId) {
        List<Item> items = itemRepository.findByIsReportedIsTrue();
        return items.stream()
                .map(item ->
                        ItemResponseDto.builder()
                                .itemId(item.getId())
                                .itemName(item.getItemName())
                                .itemTotalAmount(item.getItemTotalAmount())
                                .itemRemainingAmount(item.getItemRemainingAmount())
                                .itemSoldAmount(item.getItemSoldAmount())
                                .isReported(item.getIsReported())
                                .productId(item.getProduct().getId())
                                .productName(item.getProduct().getProductName())
                                .productOwnerId(item.getProduct().getProductOwner().getId())
                                .productOwnerName(item.getProduct().getProductOwner().getName())
                                .productOwnerSurname(item.getProduct().getProductOwner().getSurname())
                                .branchId(item.getBranch().getId())
                                .branchName(item.getBranch().getBranchName())
                                .brandId(item.getBranch().getBrand().getId())
                                .brandName(item.getBranch().getBrand().getBrandName())
                                .build()
                )
                .collect(Collectors.toList());
    }
    @Transactional
    public BranchesResponseDto getTopTenBranches(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new IllegalArgumentException("The product owner does not have this product.");
        }
        List<Item> allProductItemsFromDb = itemRepository.findByProductIdAndIsReportedIsTrue(productId);
        List<ProductItemResponseDto> allProductItems = allProductItemsFromDb.stream()
                .map(item ->
                        ProductItemResponseDto.builder()
                                .itemId(item.getId())
                                .itemName(item.getItemName())
                                .itemTotalAmount(item.getItemTotalAmount())
                                .itemRemainingAmount(item.getItemRemainingAmount())
                                .itemSoldAmount(item.getItemSoldAmount())
                                .isReported(item.getIsReported())
                                //.photosZip(item.getPhotosZip())
                                .product(ProductItemProductDto.builder()
                                        .productId(item.getProduct().getId())
                                        .productName(item.getProduct().getProductName())
                                        .productLogo(item.getProduct().getProductLogo())
                                        .productOwnerId(item.getProduct().getProductOwner().getId())
                                        .productOwnerName(item.getProduct().getProductOwner().getName())
                                        .productOwnerSurname(item.getProduct().getProductOwner().getSurname())
                                        .build())
                                .branch(ProductItemBranchDto.builder()
                                        .branchId(item.getBranch().getId())
                                        .branchName(item.getBranch().getBranchName())
                                        .brandId(item.getBranch().getBrand().getId())
                                        .brandName(item.getBranch().getBrand().getBrandName())
                                        .build())
                                .build()
                )
                .collect(Collectors.toList());

        Map<Long, List<ProductItemResponseDto>> itemBranches = allProductItems.stream()
                .collect(Collectors.groupingBy(item -> item.getBranch().getBranchId()));

        List<BranchResponseDto> topTenBranches = new ArrayList<>();

        for (Map.Entry<Long, List<ProductItemResponseDto>> entry : itemBranches.entrySet()) {
            Long branchId = entry.getKey();
            Branch branch = branchRepository.findById(branchId).orElse(null);
            List<ProductItemResponseDto> itemList = entry.getValue();

            Long itemTotalAmount = 0L;
            Long itemRemainingAmount = 0L;
            Long itemSoldAmount = 0L;
            for (ProductItemResponseDto item : itemList) {
                itemTotalAmount += item.getItemTotalAmount();
                itemRemainingAmount += item.getItemRemainingAmount();
                itemSoldAmount += item.getItemSoldAmount();
            }
            topTenBranches.add(BranchResponseDto.builder()
                    .topTotalAmount(itemTotalAmount)
                    .topTotalRemainingAmount(itemRemainingAmount)
                    .topTotalSoldAmount(itemSoldAmount)
                    .isReported(product.getIsReported())
                    .productId(product.getId())
                    .productName(product.getProductName())
                    .productLogo(product.getProductLogo())
                    .productOwnerId(product.getProductOwner().getId())
                    .productOwnerName(product.getProductOwner().getName())
                    .productOwnerSurname(product.getProductOwner().getSurname())
                    .branchId(branch.getId())
                    .branchName(branch.getBranchName())
                    .brandId(branch.getBrand().getId())
                    .brandName(branch.getBrand().getBrandName())
                    .build());
        }

        return BranchesResponseDto.builder()
                .topTenBranches(topTenBranches.stream()
                        .sorted(Comparator.comparingLong(BranchResponseDto::getTopTotalSoldAmount).reversed())
                        .limit(10)
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public BranchesResponseDto getAllBranches(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new IllegalArgumentException("The product owner does not have this product.");
        }
        List<Item> allProductItemsFromDb = itemRepository.findByProductIdAndIsReportedIsTrue(productId);
        List<ProductItemResponseDto> allProductItems = allProductItemsFromDb.stream()
                .map(item ->
                        ProductItemResponseDto.builder()
                                .itemId(item.getId())
                                .itemName(item.getItemName())
                                .itemTotalAmount(item.getItemTotalAmount())
                                .itemRemainingAmount(item.getItemRemainingAmount())
                                .itemSoldAmount(item.getItemSoldAmount())
                                .isReported(item.getIsReported())
                                //.photosZip(item.getPhotosZip())
                                .product(ProductItemProductDto.builder()
                                        .productId(item.getProduct().getId())
                                        .productName(item.getProduct().getProductName())
                                        .productLogo(item.getProduct().getProductLogo())
                                        .productOwnerId(item.getProduct().getProductOwner().getId())
                                        .productOwnerName(item.getProduct().getProductOwner().getName())
                                        .productOwnerSurname(item.getProduct().getProductOwner().getSurname())
                                        .build())
                                .branch(ProductItemBranchDto.builder()
                                        .branchId(item.getBranch().getId())
                                        .branchName(item.getBranch().getBranchName())
                                        .brandId(item.getBranch().getBrand().getId())
                                        .brandName(item.getBranch().getBrand().getBrandName())
                                        .build())
                                .build()
                )
                .collect(Collectors.toList());

        Map<Long, List<ProductItemResponseDto>> itemBranches = allProductItems.stream()
                .collect(Collectors.groupingBy(item -> item.getBranch().getBranchId()));

        List<BranchResponseDto> topTenBranches = new ArrayList<>();

        for (Map.Entry<Long, List<ProductItemResponseDto>> entry : itemBranches.entrySet()) {
            Long branchId = entry.getKey();
            Branch branch = branchRepository.findById(branchId).orElse(null);
            List<ProductItemResponseDto> itemList = entry.getValue();

            Long itemTotalAmount = 0L;
            Long itemRemainingAmount = 0L;
            Long itemSoldAmount = 0L;
            for (ProductItemResponseDto item : itemList) {
                itemTotalAmount += item.getItemTotalAmount();
                itemRemainingAmount += item.getItemRemainingAmount();
                itemSoldAmount += item.getItemSoldAmount();
            }
            topTenBranches.add(BranchResponseDto.builder()
                    .topTotalAmount(itemTotalAmount)
                    .topTotalRemainingAmount(itemRemainingAmount)
                    .topTotalSoldAmount(itemSoldAmount)
                    .isReported(product.getIsReported())
                    .productId(product.getId())
                    .productName(product.getProductName())
                    .productLogo(product.getProductLogo())
                    .productOwnerId(product.getProductOwner().getId())
                    .productOwnerName(product.getProductOwner().getName())
                    .productOwnerSurname(product.getProductOwner().getSurname())
                    .branchId(branch.getId())
                    .branchName(branch.getBranchName())
                    .brandId(branch.getBrand().getId())
                    .brandName(branch.getBrand().getBrandName())
                    .build());
        }

        return BranchesResponseDto.builder()
                .topTenBranches(topTenBranches.stream()
                        .sorted(Comparator.comparingLong(BranchResponseDto::getTopTotalSoldAmount).reversed())
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public AllItemsInformationResponseDto getAllItemsInformation(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new IllegalArgumentException("The product owner does not have this product.");
        }

        List <Item> items = itemRepository.findByProductIdAndIsReportedIsTrue(productId);
        Long totalAmount = 0L;
        Long remainingAmount = 0L;
        Long soldAmount = 0L;

        for (Item item : items) {
            totalAmount += item.getItemTotalAmount();
            remainingAmount += item.getItemRemainingAmount();
            soldAmount += item.getItemSoldAmount();
        }

        return AllItemsInformationResponseDto.builder()
                .totalItems(totalAmount)
                .totalSoldItems(soldAmount)
                .totalRemainingItems(remainingAmount)
                .build();
    }

    @Transactional
    public TopFiveItemsResponseDto getTopFiveItems(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new IllegalArgumentException("The product owner does not have this product.");
        }
        List<Item> items = itemRepository.findByProductIdAndIsReportedIsTrueOrderByItemSoldAmountDesc(productId);
        List<TopItemResponseDto> allProductItems = items.stream()
                .map(item ->
                        TopItemResponseDto.builder()
                                .itemName(item.getItemName())
                                .itemTotalAmount(item.getItemTotalAmount())
                                .itemRemainingAmount(item.getItemRemainingAmount())
                                .itemSoldAmount(item.getItemSoldAmount())
                                .build()
                )
                .collect(Collectors.toList());

        return TopFiveItemsResponseDto.builder()
                .topFiveItems(allProductItems.stream()
                        .limit(5)
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public TopAllItemsResponseDto getTopAllItems(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new IllegalArgumentException("The product owner does not have this product.");
        }
        List<Item> items = itemRepository.findByProductIdAndIsReportedIsTrueOrderByItemSoldAmountDesc(productId);
        List<TopItemDetailResponseDto> allProductItems = items.stream()
                .map(item ->
                        TopItemDetailResponseDto.builder()
                                .itemId(item.getId())
                                .itemName(item.getItemName())
                                .itemTotalAmount(item.getItemTotalAmount())
                                .itemRemainingAmount(item.getItemRemainingAmount())
                                .itemSoldAmount(item.getItemSoldAmount())
                                .productId(item.getProduct().getId())
                                .productName(item.getProduct().getProductName())
                                .productOwnerId(item.getProduct().getProductOwner().getId())
                                .productOwnerName(item.getProduct().getProductOwner().getName())
                                .productOwnerSurname(item.getProduct().getProductOwner().getSurname())
                                .branchId(item.getBranch().getId())
                                .branchName(item.getBranch().getBranchName())
                                .brandId(item.getBranch().getBrand().getId())
                                .brandName(item.getBranch().getBrand().getBrandName())
                                .build()
                )
                .collect(Collectors.toList());
        return TopAllItemsResponseDto.builder()
                .allItems(allProductItems.stream()
                        .collect(Collectors.toList()))
                .build();
    }
    @Transactional
    public ItemInDetailResponseDto getItemInDetail(Long productId, Long itemId) {
        List<Item> items = itemRepository.findByProductIdAndIdAndIsReportedIsTrueOrderByItemRemainingAmountAsc(productId, itemId);
        return ItemInDetailResponseDto.builder()
                .items(items.stream()
                        .map(item ->
                                ItemResponseDto.builder()
                                        .itemId(item.getId())
                                        .itemName(item.getItemName())
                                        .itemTotalAmount(item.getItemTotalAmount())
                                        .itemRemainingAmount(item.getItemRemainingAmount())
                                        .itemSoldAmount(item.getItemSoldAmount())
                                        .isReported(item.getIsReported())
                                        .productId(item.getProduct().getId())
                                        .productName(item.getProduct().getProductName())
                                        .productOwnerId(item.getProduct().getProductOwner().getId())
                                        .productOwnerName(item.getProduct().getProductOwner().getName())
                                        .productOwnerSurname(item.getProduct().getProductOwner().getSurname())
                                        .branchId(item.getBranch().getId())
                                        .branchName(item.getBranch().getBranchName())
                                        .brandId(item.getBranch().getBrand().getId())
                                        .brandName(item.getBranch().getBrand().getBrandName())
                                        .build()
                        )
                        .collect(Collectors.toList()))
                .build();
    }

    public ProductLogoResponseDto getProductLogo(Long productId) {
        return ProductLogoResponseDto.builder()
                .productLogo(productRepository.findById(productId).orElse(null).getProductLogo())
                .build();
    }

    @Transactional
    public ProductIdResponseDto getProductId(Long productOwnerId) {
        Product product = productRepository.findByProductOwnerId(productOwnerId).orElse(null);
        if (product == null) {
            throw new IllegalArgumentException("The product owner does not have product.");
        }
        return ProductIdResponseDto.builder()
                .productId(product.getId())
                .build();
    }
    @Transactional
    public BranchIdResponseDto getBranchId(Long technicalStaffId) {
        Branch branch = branchRepository.findByTechnicalStaffId(technicalStaffId).orElse(null);
        if (branch == null) {
            throw new IllegalArgumentException("The technical staff does not have branch.");
        }
        return BranchIdResponseDto.builder()
                .branchId(branch.getId())
                .build();
    }
}
