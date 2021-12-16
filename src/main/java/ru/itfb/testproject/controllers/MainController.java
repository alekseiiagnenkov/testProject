package ru.itfb.testproject.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itfb.testproject.entity.Author;
import ru.itfb.testproject.entity.Book;
import ru.itfb.testproject.repositories.AuthorRepository;
import ru.itfb.testproject.repositories.BookRepository;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
public class MainController {

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    @Autowired
    public MainController(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        jsonBooks();
        jsonAuthors();
    }

    @GetMapping("/")
    public String main(Model model) {
        return "main";
    }

/*    @GetMapping("/login") //TODO исправить ошибку 404
    public String login(Model model) {
        return "login";
    }*/

    @GetMapping("/about")
    public String about(Model model) {
        return "about";
    }

    @GetMapping("books")
    public String books(Model model) {
        Iterable<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/book")
    @RequestMapping(value = "/toBook", method = RequestMethod.POST)
    public String book(Model model, @RequestParam(name = "Pk") Long Pk) {
        Iterator<Book> books = bookRepository.findAll().iterator();
        Book book = books.next();
        while (!Objects.equals(book.getPk(), Pk)) {
            book = books.next();
        }
        model.addAttribute("book", book);
        return "book";
    }

    @GetMapping("/author")
    @RequestMapping(value = "/toAuthor", method = RequestMethod.POST)
    public String author(Model model, @RequestParam(name = "Pk") Long Pk) {
        Iterator<Author> authors = authorRepository.findAll().iterator();
        Author author = authors.next();
        while (!Objects.equals(author.getPk(), Pk)) {
            author = authors.next();
        }
        model.addAttribute("author", author);
        return "author";
    }

    @GetMapping("/authors")
    public String authors(Model model) {
        Iterable<Author> authors = authorRepository.findAll();
        model.addAttribute("authors", authors);
        return "authors";
    }

    @RequestMapping("jsonAuthors")
    @ResponseBody
    public void jsonAuthors() {
        URL url = this.getClass().getClassLoader().getResource("database/authors.json");

        if (url != null) {

            File jsonFile = new File(url.getFile());

            ObjectMapper objectMapper = new ObjectMapper();

            try {
                List<Author> authors = objectMapper.readValue(jsonFile, new TypeReference<>() {

                });

                authorRepository.saveAll(authors);

                log.info("All records saved.");

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            log.warn("uri is null.");
        }
    }

    @RequestMapping("jsonBooks")
    @ResponseBody
    public void jsonBooks() {
        URL url = this.getClass().getClassLoader().getResource("database/books.json");

        if (url != null) {

            File jsonFile = new File(url.getFile());

            ObjectMapper objectMapper = new ObjectMapper();

            try {
                List<Book> books = objectMapper.readValue(jsonFile, new TypeReference<>() {

                });

                bookRepository.saveAll(books);

                log.info("All records saved.");

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            log.warn("uri is null.");
        }


    }
}