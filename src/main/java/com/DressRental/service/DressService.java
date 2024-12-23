package com.DressRental.service;

import com.DressRental.dto.input.DressCreateDTO;
import com.DressRental.dto.input.DressEditDTO;
import com.DressRental.dto.output.DressDTO;

import java.util.List;
import java.util.UUID;

public interface DressService {
    void addDress(DressCreateDTO dressDTO);
    DressEditDTO getDressByName(String dressName);
    List<DressDTO> getDressesByCategory(String category, UUID userId);
    List<String> getAvailableDressSize(String dressName);
    void updateDress(String dressName, DressEditDTO dressEditDTO);
    void deleteDress(String dressName);
    List<DressDTO> timeToShineDresses(UUID userId);
    List<String> getAllCategories();
    List<String> getAllSizes();
}
