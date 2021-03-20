package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void get_all_items() throws Exception{
        final ResponseEntity<List<Item>> response = itemController.getItems();
        // we got response
        assertNotNull(response);
        // status success
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertNotNull(items);
        // we did not saved any items
        assertEquals(0, items.size());
        // create an item
        Item itemNew = new Item();
        itemNew.setDescription("description");
        itemNew.setId(0L);
        itemNew.setName("name");
        itemNew.setPrice(new BigDecimal(100));
        List<Item> itemList = new ArrayList<>();
        itemList.add(itemNew);
        when(itemRepository.findAll()).thenReturn(itemList);
        final ResponseEntity<List<Item>> responseNew = itemController.getItems();
        // we got response
        assertNotNull(responseNew);
        // status success
        assertEquals(200, responseNew.getStatusCodeValue());
        List<Item> itemsNew = responseNew.getBody();
        assertNotNull(itemsNew);
        // we saved 1 item
        assertEquals(1, itemsNew.size());
    }

    @Test
    public void get_item_by_id() throws Exception{
        // create an item
        Item item = new Item();
        item.setDescription("description");
        item.setId(0L);
        item.setName("name");
        item.setPrice(new BigDecimal(100));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        final ResponseEntity<Item> response = itemController.getItemById(0L);
        // we got response
        assertNotNull(response);
        // status success
        assertEquals(200, response.getStatusCodeValue());
        Item item2 = response.getBody();
        assertNotNull(item2);
        assertEquals("description", item2.getDescription());
        assertEquals("name", item2.getName());
        assertEquals(BigDecimal.valueOf(100), item2.getPrice());
    }

    @Test
    public void get_item_by_id_not_found() throws Exception{
        // create an item
        Item item = new Item();
        item.setDescription("description");
        item.setId(0L);
        item.setName("name");
        item.setPrice(new BigDecimal(100));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        final ResponseEntity<Item> response = itemController.getItemById(2L);
        // we got response
        assertNotNull(response);
        // status success
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_items_by_name() throws Exception{
        // create an item
        Item item = new Item();
        item.setDescription("description");
        item.setId(0L);
        item.setName("name");
        item.setPrice(new BigDecimal(100));
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        when(itemRepository.findByName(item.getName())).thenReturn(itemList);
        final ResponseEntity<List<Item>> response = itemController.getItemsByName("name");
        // we got response
        assertNotNull(response);
        // status success
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertNotNull(items);
    }

    @Test
    public void get_items_by_name_not_found() throws Exception{
        // create an item
        Item item = new Item();
        item.setDescription("description");
        item.setId(0L);
        item.setName("name");
        item.setPrice(new BigDecimal(100));
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        when(itemRepository.findByName(item.getName())).thenReturn(itemList);
        final ResponseEntity<List<Item>> response = itemController.getItemsByName("other name");
        // we got response
        assertNotNull(response);
        // status success
        assertEquals(404, response.getStatusCodeValue());
    }
}
