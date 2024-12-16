package com.DressRental.repository.impl;

import com.DressRental.models.entities.Dress;
import com.DressRental.models.entities.DressSize;
import com.DressRental.repository.BaseRepository;
import com.DressRental.repository.DressRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public class DressRepositoryImpl extends BaseRepository<Dress, UUID> implements DressRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public DressRepositoryImpl() {
        super(Dress.class);
    }

    @Override
    public List<Dress> findAllByNameWithSizes(String name) {
        return entityManager.createQuery(
                        "SELECT d FROM Dress d LEFT JOIN FETCH d.size WHERE d.name = :name", Dress.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public List<Dress> findNotDeletedDressesByCategory(String category) {
        return entityManager.createQuery("select d from Dress d where d.category.name = :category and d.deleted = false", Dress.class)
                .setParameter("category", category)
                .getResultList();
    }

    @Override
    public List<Dress> findAllDressesByCategory(String category) {
        return entityManager.createQuery("select d from Dress d where d.category.name = :category", Dress.class)
                .setParameter("category", category)
                .getResultList();
    }

    @Override
    public List<DressSize> findAvailableSizes(String dressName) {
        return entityManager.createQuery(
                        "select s.name from Dress d join DressSize s on d.size.id = s.id where d.name = :dressName", DressSize.class)
                .setParameter("dressName", dressName)
                .getResultList();
    }

    @Override
    public Dress findDressByNameAndSize(String dressName, String size) {
        System.out.println("Поиск платья: " + dressName + ", размер: " + size);
        return entityManager.createQuery("select d from Dress d where d.name = :dressName and d.size.name = :size and d.deleted = false", Dress.class)
                .setParameter("dressName", dressName)
                .setParameter("size", size)
                .getSingleResult();
    }

    @Override
    public boolean isDressAvailable(UUID dressId, LocalDate startDate, LocalDate endDate) {
        Long count = entityManager.createQuery(
                        "select count(r) from Rental r where r.dress.id = :dressId " +
                        "and (r.rentalDate < :endDate and r.returnDate > :startDate) and r.status.name != :cancelledStatus", Long.class)
                .setParameter("dressId", dressId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("cancelledStatus", "Отменено")
                .getSingleResult();
        return count == 0;
    }

    @Override
    public List<Dress> findDressesNotRentedFewMonths(int monthQuantity) {
        LocalDate period = LocalDate.now().minusMonths(monthQuantity);

        return entityManager.createQuery(
                        "select d from Dress d where exists (select 1 from Rental r where r.dress.id = d.id and r.rentalDate <= :period) and d.deleted = false",
                        Dress.class)
                .setParameter("period", period)
                .getResultList();
    }

    @Override
    public List<Dress> findDressesNeverRented() {
        return entityManager.createQuery("select d from Dress d where not exists (select r from Rental r where r.dress.id = d.id) and d.deleted = false", Dress.class)
                .getResultList();
    }
}