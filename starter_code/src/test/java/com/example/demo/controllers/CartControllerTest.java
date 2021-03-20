package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void add_to_cart_happy_path() throws Exception{
        // create item to add
        Item item = new Item();
        item.setDescription("description");
        item.setId(0L);
        item.setName("name");
        item.setPrice(new BigDecimal(100));
        // create user
        User user = new User();
        user.setUsername("test");
        user.setId(0L);
        user.setPassword("password");
        // create cart for user
        Cart cart = new Cart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);
        // create cart request
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("test");
        request.setItemId(0L);
        request.setQuantity(2);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(user);
        when(itemRepository.findById(request.getItemId())).thenReturn(Optional.of(item));
        final ResponseEntity<Cart> response = cartController.addTocart(request);
        // we got response
        assertNotNull(response);
        // status success
        assertEquals(200, response.getStatusCodeValue());
        Cart cartNew = response.getBody();
        assertNotNull(cartNew);
    }

    @Test
    public void add_to_cart_failed() throws Exception{
        // create item to add
        Item item = new Item();
        item.setDescription("description");
        item.setId(0L);
        item.setName("name");
        item.setPrice(new BigDecimal(100));
        // create user
        User user = new User();
        user.setUsername("test");
        user.setId(0L);
        user.setPassword("password");
        // create cart for user
        Cart cart = new Cart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);
        // create cart request
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("test");
        request.setItemId(1L);
        request.setQuantity(2);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(null);
        when(itemRepository.findById(request.getItemId())).thenReturn(null);
        final ResponseEntity<Cart> response = cartController.addTocart(request);
        // we got response
        assertNotNull(response);
        // status not found
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_happy_path() throws Exception{
        // create item to add
        Item item = new Item();
        item.setDescription("description");
        item.setId(0L);
        item.setName("name");
        item.setPrice(new BigDecimal(100));
        // create second item to add
        Item item2 = new Item();
        item2.setDescription("description2");
        item2.setId(1L);
        item2.setName("name2");
        item2.setPrice(new BigDecimal(200));
        // create user
        User user = new User();
        user.setUsername("test");
        user.setId(0L);
        user.setPassword("password");
        // create cart for user
        Cart cart = new Cart();
        cart.addItem(item);
        cart.addItem(item2);
        cart.setUser(user);
        user.setCart(cart);
        // create cart request
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("test");
        request.setItemId(0L);
        request.setQuantity(1);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(user);
        when(itemRepository.findById(request.getItemId())).thenReturn(Optional.of(item));
        final ResponseEntity<Cart> response = cartController.removeFromcart(request);
        // we got response
        assertNotNull(response);
        // status success
        assertEquals(200, response.getStatusCodeValue());
        Cart cartNew = response.getBody();
        assertNotNull(cartNew);
        // one item left in cart
        assertEquals(1, cartNew.getItems().size());
        assertEquals("name2", cartNew.getItems().get(0).getName());
    }

    @Test
    public void remove_from_cart_failed() throws Exception{
        // create item to add
        Item item = new Item();
        item.setDescription("description");
        item.setId(0L);
        item.setName("name");
        item.setPrice(new BigDecimal(100));
        // create second item to add
        Item item2 = new Item();
        item2.setDescription("description2");
        item2.setId(1L);
        item2.setName("name2");
        item2.setPrice(new BigDecimal(200));
        // create user
        User user = new User();
        user.setUsername("test");
        user.setId(0L);
        user.setPassword("password");
        // create cart for user
        Cart cart = new Cart();
        cart.addItem(item);
        cart.addItem(item2);
        cart.setUser(user);
        user.setCart(cart);
        // create cart request
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("test");
        request.setItemId(0L);
        request.setQuantity(1);
        when(userRepository.findByUsername(request.getUsername())).thenReturn(null);
        when(itemRepository.findById(request.getItemId())).thenReturn(null);
        final ResponseEntity<Cart> response = cartController.removeFromcart(request);
        // we got response
        assertNotNull(response);
        // status not found
        assertEquals(404, response.getStatusCodeValue());
    }
}
