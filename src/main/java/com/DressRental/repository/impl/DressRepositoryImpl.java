package com.DressRental.repository.impl;

import com.DressRental.entity.Dress;
import com.DressRental.entity.DressSize;
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
    public List<Dress> findDressesByCategoryId(UUID categoryId) {
        return entityManager.createQuery("select d from Dress d where d.category = :categoryId", Dress.class)
                .setParameter("categoryId", categoryId)
                .getResultList();
    }

    @Override
    public List<DressSize> findAvailableSizes(String dressName) {
        return entityManager.createQuery("select ds from Dress d join DressSize ds on d.size = ds.id" +
                                         " where d.name = :name and d.availability = true", DressSize.class)
                .setParameter("name", dressName)
                .getResultList();
    }

    //проверить правильность доступности платья в даты
    @Override
    public boolean isDressAvailable(UUID dressId, LocalDate startDate, LocalDate endDate) {
        Boolean isRented = entityManager.createQuery(
                        "SELECT COUNT(r) > 0 FROM Rental r WHERE r.dress.id = :dressId " +
                        "AND (r.rentalDate < :endDate AND r.returnDate > :startDate)", Boolean.class)
                .setParameter("dressId", dressId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult();
        return !isRented;
    }

    @Override
    public List<Dress> findDressesNotRentedFewMonths(int monthQuantity) {
        LocalDate period = LocalDate.now().minusMonths(monthQuantity);

        return entityManager.createQuery("select d from Dress d join Rental r on d.id = r.dress where r.rentalDate <= :period", Dress.class)
                .setParameter("period", period)
                .getResultList();
    }

    @Override
    public List<Dress> findDressesNeverRented() {
        return entityManager.createQuery("select d from Dress d where not exists (select r from Rental r where r.dress.id = d.id)", Dress.class)
                .getResultList();
    }
}