package com.example.bookstore.controllers.thymeleafController;

import com.example.bookstore.dtos.AuthorDto;
import com.example.bookstore.exceptions.AuthorHasBookException;
import com.example.bookstore.models.Author;
import com.example.bookstore.repositories.AuthorRepository;
import com.example.bookstore.services.AuthorService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorRepository authorRepo;

    public AuthorController(AuthorService authorService, AuthorRepository authorRepo) {
        this.authorService = authorService;
        this.authorRepo = authorRepo;
    }

    @GetMapping("/authors/new")
    public String showCreateAuthorForm(Model model) {
        model.addAttribute("author", new Author());
        return "admin/author-create";
    }

    @PostMapping("/authors")
    public String createAuthor(@Valid @ModelAttribute("author") AuthorDto authorDto, BindingResult result, Model model) {

       try{
           authorService.save(authorDto);
       } catch(RuntimeException ex){
           result.reject("author.error", ex.getMessage());
           return "admin/author-create";
       }
        return "redirect:/admin/authors";
    }

    @GetMapping("/authors/edit/{id}")
    public String showEditAuthor(@PathVariable Long id, Model model) {
        AuthorDto author = authorService.findById(id);

        model.addAttribute("author", author);
        return "admin/authors/author-edit";
    }

    @PostMapping("/authors/{id}")
    public String updateAuthor(
            @PathVariable Long id,
            @Valid @ModelAttribute("author") AuthorDto authorDto,
            BindingResult result) {

        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "admin/authors/author-edit";
        }

        authorDto.setId(id);

        try {
            authorService.save(authorDto);
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

        Page<AuthorDto> authorPage = authorService.findPaginated(page, size, sortBy, direction);
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


    @GetMapping("/authors/export")
    public void exportAuthorsToExcel(HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=authors.xlsx");

        List<Author> authors = authorRepo.findAll();

        try {
            authorService.exportAuthorToExcel(
                    authors,
                    response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
