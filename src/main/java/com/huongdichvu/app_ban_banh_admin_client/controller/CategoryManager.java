package com.huongdichvu.app_ban_banh_admin_client.controller;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huongdichvu.app_ban_banh_admin_client.dto.Category;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Controller
public class CategoryManager {

//quản lý danh mục
    @GetMapping("/admin/category")
    public String listCategory(Model model){
        WebClient webClient = WebClient.create();
//        List<Category> cateList = categoryService.findAll();
        List<Category> cateList = webClient.get()
                .uri("https://localhost:8080/admin/category")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Category>>() {})
                .block();

        model.addAttribute("categorys", cateList);
        return "category";
    }

    @GetMapping("/admin/category/addCategory")
    public String  newCategory(Model model)
    {
        Category category = new Category();
        model.addAttribute("category", category);
        return "create_category";
    }
    
    @PostMapping("/admin/category/addCategory")
    public String saveCategory(@ModelAttribute Category category) throws JsonProcessingException {
        WebClient webClient = WebClient.create();


        String jsonBody = new ObjectMapper().writeValueAsString(category);
         webClient.post()
                .uri("https://localhost:8080/admin/category/addCategory")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jsonBody))
                .retrieve()
                .bodyToMono(String.class);
//        categoryService.save(category);
        return "redirect:/admin/category";
    }
    @GetMapping("/admin/category/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id)
    {
        categoryService.delete((long)id);
        return "redirect:/admin/category";
    }

    @GetMapping("/admin/category/edit/{id}")
    public String editCategory(@PathVariable Long id ,Model model)
    {
            model.addAttribute("category", categoryService.find(id).get());
            return "edit_category";

    }

    @PostMapping("/admin/category/{id}")
    public String saveCategory(@PathVariable Long id,@ModelAttribute("category") Category tmp ){
        Category category = categoryService.find(id).get();
        category.setTenCategory(tmp.getTenCategory());
        categoryService.save(category);
        return "redirect:/admin/category" ;

    }
}
