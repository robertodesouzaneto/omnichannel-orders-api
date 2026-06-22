package com.example.omnichannel_orders_api.application.service;

import com.example.omnichannel_orders_api.api.exception.BusinessException;
import com.example.omnichannel_orders_api.api.exception.ConsentRequiredException;
import com.example.omnichannel_orders_api.application.dto.CreateOrderRequest;
import com.example.omnichannel_orders_api.application.dto.OrderResponse;
import com.example.omnichannel_orders_api.application.dto.PageResponse;
import com.example.omnichannel_orders_api.application.dto.UpdateOrderStatusRequest;
import com.example.omnichannel_orders_api.domain.enums.OrderStatus;
import com.example.omnichannel_orders_api.domain.model.*;
import com.example.omnichannel_orders_api.infrastructure.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final UnitRepository unitRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final ConsentService consentService;
    private final AuditService auditService;

    public PageResponse<OrderResponse> listAll(Pageable pageable) {
        return PageResponse.from(
                orderRepository.findAll(pageable).map(OrderResponse::from)
        );
    }

    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        consentService.verifyActiveConsent(customer);

        Unit unit = unitRepository.findByIdAndActiveTrue(request.unitId())
                .orElseThrow(() -> new NoSuchElementException("Unit not found: " + request.unitId()));

        Order order = Order.builder()
                .customer(customer)
                .unit(unit)
                .channel(request.channel())
                .build();

        List<OrderItem> items = request.items().stream().map(itemReq -> {
            Product product = productRepository.findByIdAndActiveTrue(itemReq.productId())
                    .orElseThrow(() -> new NoSuchElementException("Product not found: " + itemReq.productId()));

            Stock stock = stockRepository.findByUnitIdAndProductId(unit.getId(), product.getId())
                    .orElseThrow(() -> new BusinessException(
                            "Product '" + product.getName() + "' has no stock registered at this unit"));

            stock.decrement(itemReq.quantity());
            stockRepository.save(stock);

            return OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemReq.quantity())
                    .unitPrice(product.getPrice())
                    .build();
        }).toList();

        order.getItems().addAll(items);
        order.calculateTotal();

        Order saved = orderRepository.save(order);
        auditService.log(
                customer.getId(),
                "ORDER_CREATED",
                "Order",
                saved.getId(),
                "channel=" + saved.getChannel() + " total=" + saved.getTotal()
        );
        return OrderResponse.from(saved);
    }

    public OrderResponse getById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + id));

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isStaff = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().matches("ROLE_ATTENDANT|ROLE_KITCHEN|ROLE_MANAGER|ROLE_ADMIN"));

        if (!isStaff) {
            String email = authentication.getName();
            if (!order.getCustomer().getEmail().equals(email)) {
                throw new NoSuchElementException("Order not found: " + id);
            }
        }

        return OrderResponse.from(order);
    }

    public PageResponse<OrderResponse> listMyOrders(Pageable pageable) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        return PageResponse.from(
                orderRepository.findAllByCustomerId(customer.getId(), pageable)
                        .map(OrderResponse::from)
        );
    }

    @Transactional
    public OrderResponse updateStatus(UUID id, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + id));

        OrderStatus newStatus = request.status();

        boolean valid = switch (newStatus) {
            case READY -> order.getStatus() == OrderStatus.IN_PREPARATION;
            case DELIVERED -> order.getStatus() == OrderStatus.READY;
            case CANCELED -> order.getStatus() != OrderStatus.DELIVERED
                    && order.getStatus() != OrderStatus.CANCELED;
            default -> false;
        };

        if (!valid) {
            throw new IllegalArgumentException(
                    "Cannot transition order from " + order.getStatus() + " to " + newStatus);
        }

        OrderStatus previousStatus = order.getStatus();
        order.changeStatus(newStatus);
        Order saved = orderRepository.save(order);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        userRepository.findByEmail(email).ifPresent(user ->
                auditService.log(
                        user.getId(),
                        "ORDER_STATUS_CHANGED",
                        "Order",
                        saved.getId(),
                        "from=" + previousStatus + " to=" + newStatus
                )
        );

        return OrderResponse.from(saved);
    }
}
