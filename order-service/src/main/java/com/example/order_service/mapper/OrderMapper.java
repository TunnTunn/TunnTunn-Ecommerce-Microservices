package com.example.order_service.mapper;

import com.example.order_service.dto.OrderItemResponse;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.model.Order;
import com.example.order_service.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    // Map entity -> DTO
    @Mapping(target = "items", source = "items")
    OrderResponse toOrderResponse(Order order);

    List<OrderResponse> toOrderResponses(List<Order> orders);

    OrderItemResponse toOrderItemResponse(OrderItem orderItem);
}
