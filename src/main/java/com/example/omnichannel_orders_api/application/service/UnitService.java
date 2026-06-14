package com.example.omnichannel_orders_api.application.service;

import com.example.omnichannel_orders_api.application.dto.*;
import com.example.omnichannel_orders_api.domain.model.Product;
import com.example.omnichannel_orders_api.domain.model.Stock;
import com.example.omnichannel_orders_api.domain.model.Unit;
import com.example.omnichannel_orders_api.infrastructure.repository.ProductRepository;
import com.example.omnichannel_orders_api.infrastructure.repository.StockRepository;
import com.example.omnichannel_orders_api.infrastructure.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    public List<UnitResponse> listActive() {
        return unitRepository.findAll().stream()
                .filter(Unit::isActive)
                .map(UnitResponse::from)
                .toList();
    }

    @Transactional
    public UnitResponse create(UnitRequest request) {
        Unit unit = Unit.builder()
                .name(request.name())
                .address(request.address())
                .build();
        return UnitResponse.from(unitRepository.save(unit));
    }

    @Transactional
    public UnitResponse update(UUID id, UnitRequest request) {
        Unit unit = unitRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Unit not found: " + id));

        unit.setName(request.name());
        unit.setAddress(request.address());
        return UnitResponse.from(unitRepository.save(unit));
    }

    @Transactional
    public StockResponse setStock(UUID unitId, StockRequest request) {
        Unit unit = unitRepository.findByIdAndActiveTrue(unitId)
                .orElseThrow(() -> new NoSuchElementException("Unit not found: " + unitId));

        Product product = productRepository.findByIdAndActiveTrue(request.productId())
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + request.productId()));

        Stock stock = stockRepository.findByUnitIdAndProductId(unitId, request.productId())
                .orElseGet(() -> Stock.builder().unit(unit).product(product).quantity(0).build());

        stock.setQuantity(request.quantity());
        return StockResponse.from(stockRepository.save(stock));
    }

    public List<StockResponse> listStock(UUID unitId) {
        if (!unitRepository.existsById(unitId)) {
            throw new NoSuchElementException("Unit not found: " + unitId);
        }
        return stockRepository.findAll().stream()
                .filter(s -> s.getUnit().getId().equals(unitId))
                .map(StockResponse::from)
                .toList();
    }
}
