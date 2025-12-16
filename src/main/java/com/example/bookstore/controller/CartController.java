package com.example.bookstore.controller;

import com.example.bookstore.dto.CartForm;
import com.example.bookstore.models.BookOrder;
import com.example.bookstore.services.OrderService;
import com.example.bookstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/checkout")
    public String checkout(@RequestParam Long userId, @ModelAttribute CartForm cartForm, RedirectAttributes ra) {
        // CartForm contains list of OrderItemRequest
        BookOrder order = orderService.createOrder(userId, cartForm.toOrderItemsRequest());
        ra.addFlashAttribute("message", "Order placed: " + order.getId());
        return "redirect:/orders/" + order.getId();
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        var order = orderService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("order", order);
        return "orders/view";
    }

}
