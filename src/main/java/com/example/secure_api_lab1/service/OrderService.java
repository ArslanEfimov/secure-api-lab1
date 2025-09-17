package com.example.secure_api_lab1.service;

import com.example.secure_api_lab1.configuration.CustomUserDetails;
import com.example.secure_api_lab1.dto.OrderDto;
import com.example.secure_api_lab1.dto.OrderRequestDto;
import com.example.secure_api_lab1.exception.UserNotFoundException;
import com.example.secure_api_lab1.model.Order;
import com.example.secure_api_lab1.model.User;
import com.example.secure_api_lab1.repository.OrderRepository;
import com.example.secure_api_lab1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    public Page<OrderDto> getOrders(int page, int size) {
        Long userId = getCurrentUserDetails().getId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("data").descending());
        Page<Order> orders = orderRepository.findOrdersByUserId(userId, pageable);
        return orders.map(this::convertToDto);
    }

    public OrderDto createOrder(OrderRequestDto orderRequestDto) {
        Long userId = getCurrentUserDetails().getId();
        User user = userRepository.findUserById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        LocalDateTime createData = LocalDateTime.now();
        String productName = orderRequestDto.getProductName();
        Double price = orderRequestDto.getPrice();

        Order order = new Order();
        order.setUser(user);
        order.setProductName(productName);
        order.setPrice(price);
        order.setData(createData);


        Order savedOrder = orderRepository.save(order);

        return OrderDto.builder()
                .id(savedOrder.getId())
                .price(price)
                .data(createData)
                .productName(HtmlUtils.htmlEscape(productName))
                .build();


    }


    private CustomUserDetails getCurrentUserDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (CustomUserDetails) authentication.getPrincipal();

    }

    private OrderDto convertToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setData(order.getData());
        dto.setPrice(order.getPrice());
        dto.setProductName(HtmlUtils.htmlEscape(order.getProductName()));
        return dto;
    }


}
