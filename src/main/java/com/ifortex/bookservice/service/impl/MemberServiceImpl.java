package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.model.Member;
import com.ifortex.bookservice.service.MemberService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

       @PersistenceContext
    private EntityManager entityManager; // Внедрение EntityManager

    @Override
    public Member findMember() {
        String jpql = "SELECT m FROM Member m";

        TypedQuery<Member> query = entityManager.createQuery(jpql, Member.class);

        List<Member> members = query.getResultList();
        String genreToFilter = "Romance";

               return members.stream()
                .filter(member -> member.getBorrowedBooks().stream()
                        .anyMatch(book -> book.getGenres().contains(genreToFilter)))
                .max(Comparator.comparing(Member::getMembershipDate))
                .orElse(null);
    }

    @Override
    public List<Member> findMembers() {
        String jpql = "SELECT m FROM Member m WHERE YEAR(m.membershipDate) = 2023 AND SIZE(m.borrowedBooks) = 0";

        TypedQuery<Member> query = entityManager.createQuery(jpql, Member.class);

        return query.getResultList();
    }
}
