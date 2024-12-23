package com.DressRental.controllers;

import com.DressRental.dto.input.UserEditDTO;
import com.DressRental.dto.input.UserSignUpDTO;
import com.DressRental.exceptions.PasswordMatchingException;
import com.DressRental.exceptions.UserAlreadyExistsException;
import com.DressRental.service.impl.AuthService;
import com.DressRental.service.impl.ClientRatingServiceImpl;
import com.DressRental.service.impl.UserServiceImpl;
import com.DressRentalContracts.controllers.UserController;
import com.DressRentalContracts.form.EditUserProfileForm;
import com.DressRentalContracts.form.SignInForm;
import com.DressRentalContracts.form.SignUpForm;
import com.DressRentalContracts.viewmodel.*;
import jakarta.validation.Valid;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserControllerImpl implements UserController {
    private UserServiceImpl userService;
    private AuthService authService;
    private ClientRatingServiceImpl clientRatingService;
    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
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
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        LOG.log(Level.INFO, "Show register page");
        var viewModel = new SignInViewModel(createBaseViewModel("Регистрация"));
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new SignUpForm("", "", "", ""));

        return "register";
    }

    @Override
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("form") SignUpForm form,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            LOG.log(Level.INFO, "Failed registration");
            var viewModel = new SignUpViewModel(createBaseViewModel("Регистрация"));
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "register";
        }

        try {
            authService.register(new UserSignUpDTO(form.name(), form.email(), form.password(), form.confirmPassword()));
            LOG.log(Level.INFO, "New user has been registered");
            return "redirect:/user/login";
        } catch (UserAlreadyExistsException | PasswordMatchingException e) {
            LOG.log(Level.INFO, "Failed registration");
            var viewModel = new SignUpViewModel(createBaseViewModel("Регистрация"));
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }

    @Override
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        LOG.log(Level.INFO, "Show login page");
        var viewModel = new SignInViewModel(createBaseViewModel("Авторизация"));
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new SignInForm("", ""));
        return "login";
    }

    @Override
    @PostMapping("/login-error")
    public String onFailedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String email,
            RedirectAttributes redirectAttributes) {
        LOG.log(Level.INFO, "Failed login");
        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, email);
        redirectAttributes.addFlashAttribute("errorMessage", true);
        return "redirect:/user/login";
    }

    @Override
    @GetMapping("/profile")
    public String showUserProfile(
            Principal principal,
            Model model) {
        LOG.log(Level.INFO, "Show profile for " + principal.getName());

        String email = principal.getName();
        var user = authService.getUser(email);

        BigDecimal averageRating = userService.getAverageRatingForUser(user.getId());

        List<ShowRentalViewModel> rentalHistory = userService.getRentalHistory(user.getId())
                .stream().map(r -> new ShowRentalViewModel(r.getRentalId(), user.getId(), r.getDressName(), r.getDressSize(), r.getRentalDate(),
                        r.getReturnDate(), r.getDeposit(), r.getFinalPrice(), r.getStatus(), clientRatingService.hasRatingForRental(r.getRentalId())))
                .toList();

        var viewModel = new ShowProfileClientViewModel(
                createBaseViewModel("Профиль"),
                user.getName(),
                user.getEmail(),
                averageRating,
                rentalHistory);

        model.addAttribute("model", viewModel);

        return "profile";
    }

    @Override
    @GetMapping("/profile/edit")
    public String showEditProfilePage(
            Principal principal,
            Model model) {
        LOG.log(Level.INFO, "Show edit profile page for " + principal.getName());

        String email = principal.getName();
        var user = authService.getUser(email);

        var viewModel = new EditUserProfileViewModel(createBaseViewModel("Редактировать профиль"));
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new EditUserProfileForm(user.getName(), user.getPassword(), null));

        return "profile-edit";
    }

    @Override
    @PostMapping("/profile/edit")
    public String editProfile(
            Principal principal,
            @Valid @ModelAttribute("form") EditUserProfileForm form,
            BindingResult bindingResult,
            Model model) {
        var email = principal.getName();

        if (bindingResult.hasErrors()) {
            LOG.log(Level.INFO, "Incorrect editing profile for client " + principal.getName());
            var viewModel = new EditUserProfileViewModel(createBaseViewModel("Редактировать профиль"));
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "profile-edit";
        }

        try {
            userService.updateUser(email, new UserEditDTO(form.name(), form.password(), form.confirmPassword()));
            LOG.log(Level.INFO, "Correct editing profile for client " + principal.getName());
            return "redirect:/user/profile";
        } catch (PasswordMatchingException e) {
            LOG.log(Level.INFO, "Incorrect editing profile for client " + principal.getName());
            var viewModel = new EditUserProfileViewModel(createBaseViewModel("Редактировать профиль"));
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            model.addAttribute("errorMessage", e.getMessage());
            return "profile-edit";
        }
    }
}
