package com.DressRental.controllers;

import com.DressRental.dto.input.ClientRatingCreateEditDTO;
import com.DressRental.service.impl.ClientRatingServiceImpl;
import com.DressRental.service.impl.RentalServiceImpl;
import com.DressRentalContracts.controllers.ClientRatingController;
import com.DressRentalContracts.form.AddClientRatingForm;
import com.DressRentalContracts.form.EditClientRatingForm;
import com.DressRentalContracts.viewmodel.*;
import jakarta.validation.Valid;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/ratings")
public class ClientRatingControllerImpl implements ClientRatingController {
    private ClientRatingServiceImpl clientRatingService;
    private RentalServiceImpl rentalService;
    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Autowired
    public void setClientRatingService(ClientRatingServiceImpl clientRatingService) {
        this.clientRatingService = clientRatingService;
    }

    @Autowired
    public void setRentalService(RentalServiceImpl rentalService) {
        this.rentalService = rentalService;
    }

    @Override
    public BaseViewModel createBaseViewModel(String title) {
        return new BaseViewModel(title);
    }

    @Override
    @GetMapping()
    public String showRatingsPage(Model model) {
        LOG.log(Level.INFO, "Show ratings page");
        var allRatings = clientRatingService.getAllRatings()
                .stream().map(r -> new ShowClientRatingViewModel(r.getRatingId(), r.getClientId(), r.getClientName(), r.getRating(), r.getComment(), r.getDate()))
                .toList();

        var viewModel = new ShowClientRatingAllViewModel(
                createBaseViewModel("Рейтинги клиентов"),
                allRatings);
        model.addAttribute("model", viewModel);

        return "ratings";
    }

    @Override
    @GetMapping("/{rentalId}/create")
    public String showCreateRatingPage(
            @PathVariable UUID rentalId,
            Model model) {
        LOG.log(Level.INFO, "Show create rating page");
        var rental = rentalService.getRentalById(rentalId);

        var viewModel = new AddClientRatingViewModel(
                createBaseViewModel("Добавить рейтинг"),
                rental.getClientId(),
                rentalId);
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new AddClientRatingForm(null, ""));

        return "rating-create";
    }

    @Override
    @PostMapping("/{rentalId}/create")
    public String createRating(
            @PathVariable UUID rentalId,
            @Valid @ModelAttribute("form") AddClientRatingForm form,
            BindingResult bindingResult,
            Model model) {
        var rental = rentalService.getRentalById(rentalId);

        if (bindingResult.hasErrors()) {
            LOG.log(Level.INFO, "Incorrect creation new rating for client " + rental.getClientId());
            var viewModel = new AddClientRatingViewModel(
                    createBaseViewModel("Добавить рейтинг"),
                    rental.getClientId(),
                    rentalId);
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "rating-create";
        }

        clientRatingService.addRating(rental.getClientId(), rentalId, new ClientRatingCreateEditDTO(form.rating(), form.comment()));
        LOG.log(Level.INFO, "Correct creation new rating for client " + rental.getClientId());
        return "redirect:/ratings";
    }

    @Override
    @GetMapping("/{ratingId}/edit")
    public String showEditRatingPage(@PathVariable UUID ratingId, Model model) {
        LOG.log(Level.INFO, "Show edit rating page");
        var rating = clientRatingService.getRatingById(ratingId);

        var viewModel = new EditClientRatingViewModel(
                createBaseViewModel("Редактировать рейтинг"),
                rating.getClientId());
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new EditClientRatingForm(rating.getClientId(), rating.getRating(), rating.getComment()));

        return "rating-edit";
    }

    @Override
    @PostMapping("/{ratingId}/edit")
    public String editRating(
            @PathVariable UUID ratingId,
            @Valid @ModelAttribute("form") EditClientRatingForm form,
            BindingResult bindingResult,
            Model model) {
        var rating = clientRatingService.getRatingById(ratingId);

        if (bindingResult.hasErrors()) {
            LOG.log(Level.INFO, "Incorrect editing client rating " + ratingId);
            var viewModel = new EditClientRatingViewModel(
                    createBaseViewModel("Добавить рейтинг"),
                    rating.getClientId());
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "rating-edit";
        }

        clientRatingService.updateRating(ratingId, new ClientRatingCreateEditDTO(form.rating(), form.comment()));
        LOG.log(Level.INFO, "Correct editing client rating " + ratingId);
        return "redirect:/ratings";
    }
}
