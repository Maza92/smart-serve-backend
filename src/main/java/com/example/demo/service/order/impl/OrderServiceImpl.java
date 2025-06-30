package com.example.demo.service.order.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.controller.restaurantTable.IRestaurantTableController;
import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.order.CreateDraftOrderDto;
import com.example.demo.dto.order.CreateDraftOrderResponseDto;
import com.example.demo.dto.order.OrderToKitchenDto;
import com.example.demo.dto.order.UpdateOrderWithDetailsDto;
import com.example.demo.dto.order.UpdateOrderWithDetailsResponseDto;
import com.example.demo.dto.orderDetail.CreateOrderDetailDto;
import com.example.demo.entity.DishEntity;
import com.example.demo.entity.OrderDetailEntity;
import com.example.demo.entity.OrderEntity;
import com.example.demo.entity.RestaurantTableEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.enums.OrderStatusEnum;
import com.example.demo.enums.RestaurantTableEnum;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.mappers.IOrderMapper;
import com.example.demo.repository.DishRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.dish.IDishService;
import com.example.demo.service.inventory.IInventoryService;
import com.example.demo.service.order.IOrderService;
import com.example.demo.service.parameter.IParameterService;
import com.example.demo.service.restaurantTable.IRestaurantTableService;
import com.example.demo.service.securityContext.impl.SecurityContextService;
import com.example.demo.service.webSocket.IStompMessagingService;
import com.example.demo.utils.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final IInventoryService inventoryService;
    private final IDishService dishService;
    private final ObjectMapper objectMapper;
    private final SecurityContextService securityContextService;
    private final IRestaurantTableService restaurantTableService;
    private final IStompMessagingService stompMessagingService;
    private final IParameterService parameterService;
    private final MessageUtils messageUtils;
    private final IOrderMapper orderMapper;

    private final ApiExceptionFactory apiExceptionFactory;

    @Override
    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public ApiSuccessDto<CreateDraftOrderResponseDto> createDraftOrder(CreateDraftOrderDto order) {
        RestaurantTableEntity restaurantTable = restaurantTableService.isRestaurantTableAvailable(order.getTableId());
        restaurantTableService.setStatusAndSendMessage(restaurantTable, RestaurantTableEnum.OCCUPIED);
        UserEntity currentUser = securityContextService.getUser();
        OrderEntity newOrder = new OrderEntity()
                .setUser(currentUser)
                .setTable(restaurantTable)
                .setStatus(OrderStatusEnum.PENDING)
                .setTotalPrice(BigDecimal.ZERO)
                .setTaxAmount(BigDecimal.ZERO)
                .setDiscountAmount(BigDecimal.ZERO)
                .setGuestsCount(order.getGuestsCount());

        OrderEntity savedOrder = orderRepository.save(newOrder);

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.order.create.success"),
                orderMapper.toDraftOrderResponseDto(savedOrder));
    }

    @Override
    @Transactional
    public ApiSuccessDto<Void> sendToKitchen(Integer orderId,
            UpdateOrderWithDetailsDto order) {

        Map<Integer, DishEntity> dishesMap = dishService.validateDishesForOrder(order.getDetails());
        inventoryService.checkStockForOrder(order.getDetails(), dishesMap);

        OrderEntity orderToSend = orderRepository.findById(orderId)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.order.not.found"));

        if (orderToSend.getStatus() != OrderStatusEnum.PENDING)
            throw apiExceptionFactory.badRequestException("operation.order.not.available",
                    orderToSend.getStatus().name());

        orderToSend.setCustomerName(order.getCustomerName());
        orderToSend.setComments(order.getComments());
        orderToSend.setServiceType(order.getServiceType());
        orderToSend.setStatus(OrderStatusEnum.SENT_TO_KITCHEN);

        Set<OrderDetailEntity> orderDetails = createOrderDetails(order.getDetails(), orderToSend, dishesMap);
        orderToSend.setOrderDetails(orderDetails);

        OrderEntity savedOrder = orderRepository.save(orderToSend);
        String destination = "/topic/kitchen/order-updates";
        stompMessagingService.sendMessage(destination, orderMapper.toOrderToKitchenDto(savedOrder));

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.order.create.success"),
                null);
    }

    private Set<OrderDetailEntity> createOrderDetails(List<CreateOrderDetailDto> detailsDto, OrderEntity order,
            Map<Integer, DishEntity> dishesMap) {
        return detailsDto.stream().map(detailDto -> {
            DishEntity dish = dishesMap.get(detailDto.getDishId());

            OrderDetailEntity orderDetail = new OrderDetailEntity();
            orderDetail.setOrder(order);
            orderDetail.setDish(dish);
            orderDetail.setQuantity(detailDto.getQuantity());

            BigDecimal unitPrice = dish.getBasePrice();

            try {
                orderDetail.setModifications(objectMapper.valueToTree(detailDto.getModifications()));
            } catch (Exception e) {
                apiExceptionFactory.businessException("operation.order.invalid.modification", e.getMessage());
            }

            orderDetail.setUnitPrice(unitPrice);

            orderDetail.setFinalPrice(unitPrice.multiply(BigDecimal.valueOf(detailDto.getQuantity())));
            orderDetail.setStatus(OrderStatusEnum.PENDING.name());

            return orderDetail;
        }).collect(Collectors.toSet());
    }

    @Override
    public ApiSuccessDto<PageDto<OrderToKitchenDto>> getOrdersToKitchen(int page, int size) {

        if (size <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.size");

        if (page <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.number");

        List<OrderStatusEnum> statues = Arrays.asList(
                OrderStatusEnum.READY,
                OrderStatusEnum.IN_PREPARATION,
                OrderStatusEnum.SENT_TO_KITCHEN);

        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size);

        Page<OrderEntity> orders = orderRepository.findByStatues(statues, pageable);

        List<OrderToKitchenDto> ordersToKitchen = orders.getContent().stream()
                .map(order -> {
                    OrderToKitchenDto orderToKitchenDto = orderMapper.toOrderToKitchenDto(order);
                    return orderToKitchenDto;
                })
                .toList();

        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.order.get.orders.to.kitchen"),
                PageDto.fromPage(orders, ordersToKitchen));
    }

    @Override
    @Transactional
    public ApiSuccessDto<Void> claimOrderToCook(Integer orderId) {
        OrderEntity order = orderRepository.findByStatusAndId(OrderStatusEnum.SENT_TO_KITCHEN, orderId)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.order.not.found"));

        order.setStatus(OrderStatusEnum.IN_PREPARATION);
        orderRepository.save(order);
        sendUpdateMessageToKitchen(order);
        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.order.claim.order.to.kitchen"),
                null);
    }

    @Override
    @Transactional
    public ApiSuccessDto<Void> readyOrder(Integer orderId) {
        OrderEntity order = orderRepository.findByStatusAndId(OrderStatusEnum.IN_PREPARATION, orderId)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.order.not.found"));

        order.setStatus(OrderStatusEnum.READY);
        orderRepository.save(order);
        sendUpdateMessageToKitchen(order);
        inventoryService.updateStockForOrder(order);
        return ApiSuccessDto.of(HttpStatus.OK.value(),
                messageUtils.getMessage("operation.order.ready.order"),
                null);
    }

    @Override
    public OrderEntity sendUpdateMessageToKitchen(OrderEntity order) {
        String destination = "/topic/kitchen/order-updates";
        stompMessagingService.sendMessage(destination, orderMapper.toOrderToKitchenDto(order));
        return order;
    }

    private void calculateOrderTotals(OrderEntity order) {
        BigDecimal subtotal = order.getOrderDetails().stream()
                .map(OrderDetailEntity::getFinalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal taxRate = parameterService.getBigDecimal("TAX_RATE");
        BigDecimal taxAmount = subtotal.multiply(taxRate);

        // future implementation
        BigDecimal discountAmount = BigDecimal.ZERO;

        order.setTotalPrice(subtotal);

        order.setTaxAmount(taxAmount);
        order.setDiscountAmount(discountAmount);
    }

}