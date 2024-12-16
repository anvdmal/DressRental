package com.DressRental.controllers;

import com.DressRental.service.impl.ClientRatingServiceImpl;
import com.DressRental.service.impl.RentalServiceImpl;
import com.DressRentalContracts.controllers.RentalController;
import com.DressRentalContracts.viewmodel.BaseViewModel;
import com.DressRentalContracts.viewmodel.ShowRentalViewModel;
import com.DressRentalContracts.viewmodel.ShowRentalsViewModel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/rentals")
public class RentalControllerImpl implements RentalController {
    private RentalServiceImpl rentalService;
    private ClientRatingServiceImpl clientRatingService;
    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Autowired
    public void setRentalService(RentalServiceImpl rentalService) {
        this.rentalService = rentalService;
    }

    @Autowired
    public void setClientRatingService(ClientRatingServiceImpl clientRatingService) {
        this.clientRatingService = clientRatingService;
    }

    @Override
    public BaseViewModel createBaseViewModel(String title) {
        return new BaseViewModel(title);
    }

    @Override
    @GetMapping("/not-confirmed")
    public String showNotConfirmedRentalsPage(Model model) {
        LOG.log(Level.INFO, "Show not confirmed rentals");
        var notConfirmedRentals = rentalService.getNotConfirmedRentals()
                .stream().map(r -> new ShowRentalViewModel(r.getRentalId(), r.getClientId(), r.getDressName(), r.getDressSize(), r.getRentalDate(),
                        r.getReturnDate(), r.getDeposit(), r.getFinalPrice(), r.getStatus(), clientRatingService.hasRatingForRental(r.getRentalId())))
                .toList();

        var viewModel = new ShowRentalsViewModel(
                createBaseViewModel("Не подтвержденные заявки"),
                notConfirmedRentals);

        model.addAttribute("model", viewModel);

        return "rentals-not-confirmed";
    }

    @Override
    @PostMapping("/not-confirmed/{rentalId}/confirm")
    public String confirmRental(@PathVariable UUID rentalId) {
        LOG.log(Level.INFO, "Confirm rental " + rentalId);
        rentalService.confirmRental(rentalId);
        return "redirect:/rentals/active";
    }

    @Override
    @PostMapping("/not-confirmed/{rentalId}/cancel")
    public String cancelRental(@PathVariable UUID rentalId) {
        LOG.log(Level.INFO, "Cancel rental " + rentalId);
        rentalService.cancelRental(rentalId);
        return "redirect:/rentals/not-confirmed";
    }

    @Override
    @GetMapping("/active")
    public String showActiveRentalsPage(Model model) {
        LOG.log(Level.INFO, "Show active rentals");
        var activeRentals = rentalService.getActiveRentals()
                .stream().map(r -> new ShowRentalViewModel(r.getRentalId(), r.getClientId(), r.getDressName(), r.getDressSize(), r.getRentalDate(),
                        r.getReturnDate(), r.getDeposit(), r.getFinalPrice(), r.getStatus(), clientRatingService.hasRatingForRental(r.getRentalId())))
                .toList();

        var viewModel = new ShowRentalsViewModel(
                createBaseViewModel("Активные аренды"),
                activeRentals);

        model.addAttribute("model", viewModel);

        return "rentals-active";
    }

    @Override
    @PostMapping("/active/{rentalId}/complete")
    public String completeRental(@PathVariable UUID rentalId) {
        LOG.log(Level.INFO, "Complete rental " + rentalId);
        rentalService.completeRental(rentalId);
        return "redirect:/rentals/completed";
    }

    @Override
    @GetMapping("/completed")
    public String showCompletedRentalsPage(Model model) {
        LOG.log(Level.INFO, "Show canceled rentals");
        var completedRentals = rentalService.getCompletedRentals()
                .stream().map(r -> new ShowRentalViewModel(r.getRentalId(), r.getClientId(), r.getDressName(), r.getDressSize(), r.getRentalDate(),
                        r.getReturnDate(), r.getDeposit(), r.getFinalPrice(), r.getStatus(), clientRatingService.hasRatingForRental(r.getRentalId())))
                .toList();

        var viewModel = new ShowRentalsViewModel(
                createBaseViewModel("Завершенные аренды"),
                completedRentals);

        model.addAttribute("model", viewModel);

        return "rentals-completed";
    }
}
