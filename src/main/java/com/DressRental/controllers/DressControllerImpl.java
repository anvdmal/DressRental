package com.DressRental.controllers;

import com.DressRental.dto.input.DressCreateDTO;
import com.DressRental.dto.input.DressEditDTO;
import com.DressRental.dto.input.RentalCreateDTO;
import com.DressRental.exceptions.DressAlreadyRentedException;
import com.DressRental.exceptions.InvalidDataException;
import com.DressRental.service.impl.AuthService;
import com.DressRental.service.impl.DressServiceImpl;
import com.DressRental.service.impl.RentalServiceImpl;
import com.DressRentalContracts.controllers.DressController;
import com.DressRentalContracts.form.AddDressForm;
import com.DressRentalContracts.form.AddRentalForm;
import com.DressRentalContracts.form.EditDressForm;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/dress")
public class DressControllerImpl implements DressController {
    private DressServiceImpl dressService;
    private RentalServiceImpl rentalService;
    private AuthService authService;
    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Autowired
    public void setDressService(DressServiceImpl dressService) {
        this.dressService = dressService;
    }

    @Autowired
    public void setRentalService(RentalServiceImpl rentalService) {
        this.rentalService = rentalService;
    }

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public BaseViewModel createBaseViewModel(String title) {
        return new BaseViewModel(title);
    }

    @Override
    @GetMapping("/{categoryName}")
    public String showDressCategoryPage(
            Principal principal,
            @PathVariable String categoryName,
            Model model) {
        LOG.log(Level.INFO, "Show dresses of the category " + categoryName + " for " + principal.getName());
        String email = principal.getName();
        var user = authService.getUser(email);

        var allDresses = dressService.getDressesByCategory(categoryName, user.getId())
                .stream()
                .map(d -> {
                    List<String> sizes = d.getSizes();
                    return new ShowDressesViewModel(d.getImageUrl(), d.getName(), d.getDescription(), d.getPrice(), d.getDeposit(), sizes, d.isDeleted());
                })
                .toList();

        var viewModel = new ShowCategoryDressesViewModel(
                createBaseViewModel(categoryName),
                allDresses);
        model.addAttribute("model", viewModel);

        return "category-dresses";
    }

    @Override
    @GetMapping("/admin/{categoryName}")
    public String showDressCategoryPageAdmin(
            Principal principal,
            @PathVariable String categoryName,
            Model model) {
        LOG.log(Level.INFO, "Show dresses of the category " + categoryName + " for " + principal.getName());

        String email = principal.getName();
        var user = authService.getUser(email);

        var allDresses = dressService.getAllDressesByCategory(categoryName, user.getId())
                .stream()
                .map(d -> {
                    List<String> sizes = d.getSizes();
                    return new ShowDressesViewModel(d.getImageUrl(), d.getName(), d.getDescription(), d.getPrice(), d.getDeposit(), sizes, d.isDeleted());
                })
                .toList();

        var viewModel = new ShowCategoryDressesViewModel(
                createBaseViewModel(categoryName),
                allDresses);
        model.addAttribute("model", viewModel);

        return "category-dresses-admin";
    }

    @Override
    @GetMapping("/shining-time")
    public String showShiningTimePage(
            Principal principal,
            Model model) {
        LOG.log(Level.INFO, "Show shining time dresses for " + principal.getName());

        String email = principal.getName();
        var user = authService.getUser(email);

        var timeToShineDresses = dressService.timeToShineDresses(user.getId())
                .stream()
                .map(d -> {
                    List<String> sizes = d.getSizes();
                    return new ShowDressesViewModel(d.getImageUrl(), d.getName(), d.getDescription(), d.getPrice(), d.getDeposit(), sizes, d.isDeleted());
                })
                .toList();

        var viewModel = new ShowCategoryDressesViewModel(
                createBaseViewModel("Время сиять!"),
                timeToShineDresses);
        model.addAttribute("model", viewModel);

        return "shining-time";
    }

    @Override
    @GetMapping("/{dressName}/rent")
    public String showAddRentalPage(
            @PathVariable String dressName,
            Model model) {
        LOG.log(Level.INFO, "Show rent page for dress " + dressName);
        var viewModel = new AddRentalViewModel(
                createBaseViewModel("Бронирование"),
                dressName);
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new AddRentalForm("", null, null));
        model.addAttribute("sizes", dressService.getAvailableDressSize(dressName));
        return "rental-create";
    }

    @Override
    @PostMapping("/{dressName}/rent")
    public String addRental(
            Principal principal,
            @PathVariable String dressName,
            @Valid @ModelAttribute("form") AddRentalForm form,
            BindingResult bindingResult,
            Model model) {
        String email = principal.getName();
        var user = authService.getUser(email);
        RentalCreateDTO rentalCreateDTO = new RentalCreateDTO(form.dressSize(), form.rentalDate(), form.returnDate());

        if (bindingResult.hasErrors()) {
            LOG.log(Level.INFO, "Incorrect rental of a dress " + dressName + " for " + principal.getName());
            return correctRentalCreatePage(model, form, dressName, null);
        }

        try {
            var newRentalId = rentalService.addRental(user.getId(), dressName, rentalCreateDTO);
            LOG.log(Level.INFO, "Correct rental of a dress " + dressName + " for " + principal.getName());
            return "redirect:/dress/rent/" + newRentalId;
        } catch (DressAlreadyRentedException | InvalidDataException e) {
            LOG.log(Level.INFO, "Incorrect rental of a dress " + dressName + " for " + principal.getName());
            return correctRentalCreatePage(model, form, dressName, e.getMessage());
        }
    }

    private String correctRentalCreatePage(Model model, AddRentalForm form, String dressName, String errorMessage) {
        var viewModel = new AddRentalViewModel(
                createBaseViewModel("Бронирование"),
                dressName);
        model.addAttribute("model", viewModel);
        model.addAttribute("form", form);
        model.addAttribute("sizes", dressService.getAvailableDressSize(dressName));
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
        return "rental-create";
    }

    @Override
    @GetMapping("rent/{rentalId}")
    public String showSuccessRentalPage(
            @PathVariable UUID rentalId,
            Model model) {
        var viewModel = new SuccessRentViewModel(
                createBaseViewModel("Бронирование завершено"),
                rentalId);
        model.addAttribute("model", viewModel);
        return "successful-rent";
    }

    @Override
    @GetMapping("/create")
    public String showCreateDressPage(Model model) {
        LOG.log(Level.INFO, "Show create dress page");
        var viewModel = new AddDressViewModel(createBaseViewModel("Добавить платье"));
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new AddDressForm("", "", null, null, ""));
        model.addAttribute("categories", dressService.getAllCategories());
        model.addAttribute("sizes", dressService.getAllSizes());

        return "dress-create";
    }

    @Override
    @PostMapping("/create")
    public String createDress(
            @Valid @ModelAttribute("form") AddDressForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            LOG.log(Level.INFO, "Incorrect creation new dress");
            var viewModel = new AddDressViewModel(
                    createBaseViewModel("Добавить платье"));
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            model.addAttribute("categories", dressService.getAllCategories());
            model.addAttribute("sizes", dressService.getAllSizes());
            return "dress-create";
        }

        dressService.addDress(new DressCreateDTO(form.category(), form.name(), form.sizes(), form.price(), form.description()));
        LOG.log(Level.INFO, "Correct creation new dress " + form.name());
        redirectAttributes.addAttribute("categoryName", form.category());
        return "redirect:/dress/admin/{categoryName}";
    }

    @Override
    @GetMapping("/{dressName}/edit")
    public String showEditDressPage(
            @PathVariable String dressName,
            Model model) {
        LOG.log(Level.INFO, "Show edit page for dress " + dressName);
        var dress = dressService.getDressByName(dressName);

        var viewModel = new EditDressViewModel(createBaseViewModel("Редактировать платье"));
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new EditDressForm(dress.getCategory(), dress.getName(), dress.getPrice(), dress.getDescription()));
        model.addAttribute("categories", dressService.getAllCategories());

        return "dress-edit";
    }

    @Override
    @PostMapping("/{dressName}/edit")
    public String editDress(
            @PathVariable String dressName,
            @Valid @ModelAttribute("form") EditDressForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            LOG.log(Level.INFO, "Incorrect editing dress " + dressName);
            var viewModel = new EditDressViewModel(
                    createBaseViewModel("Редактировать платье"));
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            model.addAttribute("categories", dressService.getAllCategories());
            return "dress-edit";
        }

        dressService.updateDress(dressName, new DressEditDTO(form.category(), form.name(), form.price(), form.description()));
        LOG.log(Level.INFO, "Correct editing dress " + dressName);
        redirectAttributes.addAttribute("categoryName", form.category());
        return "redirect:/dress/admin/{categoryName}";
    }

    @Override
    @PostMapping("/{dressName}/delete")
    public String deleteDress(
            @PathVariable String dressName,
            RedirectAttributes redirectAttributes) {
        LOG.log(Level.INFO, "Correct delete dress " + dressName);
        redirectAttributes.addAttribute("categoryName", dressService.getDressByName(dressName).getCategory());
        dressService.deleteDress(dressName);
        return "redirect:/dress/admin/{categoryName}";
    }
}
