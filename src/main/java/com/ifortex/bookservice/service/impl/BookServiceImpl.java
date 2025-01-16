package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.service.BookService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @PersistenceContext
    private EntityManager entityManager; // Внедрение EntityManager

    @Override
    public Map<String, Long> getBooks() {
        String jpql = "SELECT b FROM Book b";
        TypedQuery<Book> query = entityManager.createQuery(jpql, Book.class);

        List<Book> books = query.getResultList();

        return books.stream()
                .flatMap(book -> book.getGenres().stream()
                        .map(genre -> Map.entry(genre, 1L))) // Создаем запись для каждого жанра
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.summingLong(Map.Entry::getValue)

                ))

                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

    }

}
