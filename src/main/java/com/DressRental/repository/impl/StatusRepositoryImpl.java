package com.DressRental.repository.impl;

import com.DressRental.models.entities.Status;
import com.DressRental.repository.BaseRepository;
import com.DressRental.repository.StatusRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class StatusRepositoryImpl extends BaseRepository<Status, UUID> implements StatusRepository {
    public StatusRepositoryImpl() {
        super(Status.class);
    }
}
