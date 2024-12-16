package com.DressRental.controllers;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.DressRental.service.impl.DressServiceImpl;
import com.DressRentalContracts.controllers.HomeController;
import com.DressRentalContracts.viewmodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class HomeControllerImpl implements HomeController {
    private DressServiceImpl dressService;
    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Autowired
    public void setDressService(DressServiceImpl dressService) {
        this.dressService = dressService;
    }

    @Override
    public BaseViewModel createBaseViewModel(String title) {
        return new BaseViewModel(title);
    }

    @Override
    @GetMapping
    public String showHomePage(Model model) {
        LOG.log(Level.INFO, "Show home page");
        var viewModel = new HomeViewModel(
                createBaseViewModel("Главная страница"),
                dressService.getAllCategories());

        model.addAttribute("model", viewModel);

        return "home";
    }
}
