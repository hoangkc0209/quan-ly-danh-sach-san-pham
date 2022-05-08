package com.example.th.controller;

import com.example.th.Cart;
import com.example.th.model.Product;
import com.example.th.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Controller
@RequestMapping("/cart")
public class CartController {
    private final ProductRepository productRepository;
    @Autowired
    public CartController(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    @GetMapping
    public String showCart(Model model) {
        Cart cart = new Cart();
        cart.setListProduct(StreamSupport
                .stream(productRepository.findAll().spliterator(), false)
                .collect(Collectors.toCollection(ArrayList::new)));

        model.addAttribute("cart",cart );
        System.out.println("run");
        return "cart";
    }
    @GetMapping("/addProduct")
    public String ShowAddProductView(Model model){
        Product product = new Product();
        model.addAttribute("product",product);
        return "addProduct";
    }
    @PostMapping("/add")
    public String addProduct(@Valid Product product, Errors errors){
        if(errors.hasErrors())
            return "addProduct";
//        Product product = new Product();
        productRepository.save(product);
        return "redirect:/cart";
    }
    @GetMapping("/editProduct")
    public String ShowEditProductView(@RequestParam(name = "productId") String id, Model model){
        System.out.println(id);
        Product product = productRepository.findById(Long.parseLong(id)).get();
        System.out.println(product.getId());
        model.addAttribute("product",product );
        return "editProduct";
    }
    @Transactional
    @PostMapping("/edit")
    public String editProduct(@Valid Product p,
                              Errors errors){
        System.out.println("edit");
        if(errors.hasErrors())
            return "editProduct";
        System.out.println(p.getId());
        Product product = productRepository.findById(p.getId()).get();
        product.setCode(p.getCode());
        product.setPrice(p.getPrice());
        product.setDescription(p.getDescription());
//        productRepository.save(product);
        return "redirect:/cart";
    }

    @GetMapping("/deleteProduct")
    public String ShowDeleteProductView(@RequestParam(name = "productCode") String code, Model model){
        System.out.println(code);
        Product product = productRepository.findByCode(code).get();
        model.addAttribute("product",product );
        return "deleteProduct";
    }
    @PostMapping("/delete")
    public String editProduct(@RequestParam(name = "productId") String id){
        Product product =  productRepository.findById(Long.parseLong(id)).get();
//        product.setCode(code);
        productRepository.delete(product);
        return "redirect:/cart";
    }
}
