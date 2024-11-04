package com.DressRental.repository.impl;

import com.DressRental.entity.Dress;
import com.DressRental.entity.DressCategory;
import com.DressRental.repository.BaseRepository;
import com.DressRental.repository.DressCategoryRepository;

import java.util.UUID;

public class DressCategoryRepositoryImpl extends BaseRepository<DressCategory, UUID> implements DressCategoryRepository {
    public DressCategoryRepositoryImpl() {
        super(DressCategory.class);
    }
}
