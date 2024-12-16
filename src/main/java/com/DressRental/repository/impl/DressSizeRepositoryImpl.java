package com.DressRental.repository.impl;

import com.DressRental.models.entities.DressSize;
import com.DressRental.repository.BaseRepository;
import com.DressRental.repository.DressSizeRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class DressSizeRepositoryImpl extends BaseRepository<DressSize, UUID> implements DressSizeRepository {
    public DressSizeRepositoryImpl() {
        super(DressSize.class);
    }
}
