package com.DressRental.repository.impl;

import com.DressRental.entity.DressCategory;
import com.DressRental.repository.BaseRepository;
import com.DressRental.repository.DressCategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class DressCategoryRepositoryImpl extends BaseRepository<DressCategory, UUID> implements DressCategoryRepository {
    public DressCategoryRepositoryImpl() {
        super(DressCategory.class);
    }
}
