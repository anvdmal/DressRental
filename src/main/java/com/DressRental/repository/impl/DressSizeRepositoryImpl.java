package com.DressRental.repository.impl;

import com.DressRental.entity.Dress;
import com.DressRental.entity.DressSize;
import com.DressRental.repository.BaseRepository;
import com.DressRental.repository.DressSizeRepository;

import java.util.UUID;

public class DressSizeRepositoryImpl extends BaseRepository<DressSize, UUID> implements DressSizeRepository {
    public DressSizeRepositoryImpl() {
        super(DressSize.class);
    }
}
