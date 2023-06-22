package com.backend.recognitionitems.items;

import com.backend.recognitionitems.items.dto.Item;
import com.backend.recognitionitems.items.dto.response.ItemResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByItemName(String itemName);

    List<Item> findByProductIdAndIsReportedIsTrue(Long productId);

    Optional<Item> findByItemNameAndBranchId(String itemName, Long id);

    List<Item> findByIsReportedIsTrue();

    List<Item> findByProductIdAndIsReportedIsTrueOrderByItemSoldAmountDesc(Long productId);

    List<Item> findByProductIdAndIdAndIsReportedIsTrueOrderByItemRemainingAmountAsc(Long productId, Long itemId);
}
