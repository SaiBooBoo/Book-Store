package com.example.bookstore.controller;

import com.example.bookstore.exceptions.AuthorHasBookException;
import com.example.bookstore.exceptions.DuplicateEmailException;
import com.example.bookstore.models.Author;
import com.example.bookstore.repositories.AuthorRepository;
import com.example.bookstore.services.AuthorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AuthorController {

    @Autowired
    private AuthorService authorService;
    @Autowired
    private AuthorRepository repo;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }


    // ----------------- new -------------------
    @GetMapping("/authors/new")
    public String showCreateAuthorForm(Model model) {
        model.addAttribute("author", new Author());
        return "admin/author-create";
    }

    @PostMapping("/authors")
    public String createAuthor(@Valid @ModelAttribute("author") Author author, BindingResult result) {

        authorService.save(author);
        return "redirect:/admin/authors";
    }

    // -----------------  edit -----------------

    @GetMapping("/authors/edit/{id}")
    public String showEditAuthor(@PathVariable Long id, Model model) {
        Author author = authorService.findById(id);

        model.addAttribute("author", author);
        return "admin/authors/author-edit";
    }

    @PostMapping("/authors/{id}")
    public String updateAuthor(
            @PathVariable Long id,
            @Valid @ModelAttribute("author") Author author,
            BindingResult result) {

        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "admin/authors/author-edit";
        }

        author.setId(id);

        try {
            authorService.save(author);
        } catch (IllegalStateException ex) {
            result.rejectValue("email", "error.author", ex.getMessage());
            return "admin/authors/author-edit";
        }
        return "redirect:/admin/authors";
    }

    @GetMapping("/authors")
    public String adminAuthors(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               @RequestParam(defaultValue = "id") String sortBy,
                               @RequestParam(defaultValue = "asc") String direction,
                               Model model) {

        Page<Author> authorPage = authorService.findPaginated(page, size, sortBy, direction);
        model.addAttribute("authors", authorPage.getContent());
        model.addAttribute("authorPage", authorPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        return "admin/authors";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/authors/{id}/delete")
    public String deleteAuthor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
       try{
           authorService.deleteById(id);
           redirectAttributes.addFlashAttribute("successMessage", "Author deleted successfully");
       } catch (AuthorHasBookException ex) {
           redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
       }

        return "redirect:/admin/authors";
    }

}
