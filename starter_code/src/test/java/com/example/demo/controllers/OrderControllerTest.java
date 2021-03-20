package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.aspectj.weaver.ast.Or;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setup() {
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void order_submit_happy_path() throws Exception{
        // create user
        User user = new User();
        user.setUsername("test");
        user.setId(0L);
        user.setPassword("password");
        // create cart with item for user
        Item item = new Item();
        item.setDescription("description");
        item.setId(0L);
        item.setName("name");
        item.setPrice(new BigDecimal(100));
        Cart cart = new Cart();
        cart.setUser(user);
        cart.addItem(item);
        user.setCart(cart);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        final ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());
        // we got response
        assertNotNull(response);
        // status success
        assertEquals(200, response.getStatusCodeValue());
        UserOrder order = response.getBody();
        assertNotNull(order);
    }

    @Test
    public void order_submit_not_found_user() throws Exception{
        // create user
        User user = new User();
        user.setUsername("test");
        user.setId(0L);
        user.setPassword("password");
        // create cart with item for user
        Item item = new Item();
        item.setDescription("description");
        item.setId(0L);
        item.setName("name");
        item.setPrice(new BigDecimal(100));
        Cart cart = new Cart();
        cart.setUser(user);
        cart.addItem(item);
        user.setCart(cart);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        final ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());
        // we got response
        assertNotNull(response);
        // status not found
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_orders_for_user_happy_path() throws Exception{
        // create user
        User user = new User();
        user.setUsername("test");
        user.setId(0L);
        user.setPassword("password");
        // create cart with item for user
        Item item = new Item();
        item.setDescription("description");
        item.setId(0L);
        item.setName("name");
        item.setPrice(new BigDecimal(100));
        Cart cart = new Cart();
        cart.setUser(user);
        cart.addItem(item);
        user.setCart(cart);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        // create order
        orderController.submit(user.getUsername());
        // check history
        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());
        // we got response
        assertNotNull(response);
        // status success
        assertEquals(200, response.getStatusCodeValue());
        List<UserOrder> orders = response.getBody();
        assertNotNull(orders);
    }

    @Test
    public void get_orders_for_user_not_found() throws Exception{
        // create user
        User user = new User();
        user.setUsername("test");
        user.setId(0L);
        user.setPassword("password");
        // create cart with item for user
        Item item = new Item();
        item.setDescription("description");
        item.setId(0L);
        item.setName("name");
        item.setPrice(new BigDecimal(100));
        Cart cart = new Cart();
        cart.setUser(user);
        cart.addItem(item);
        user.setCart(cart);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        // create order
        orderController.submit(user.getUsername());
        // not found user
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        // check history
        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());
        // we got response
        assertNotNull(response);
        // status not found
        assertEquals(404, response.getStatusCodeValue());
    }
}
