package com.example.pract.controller;

import com.example.pract.dto.*;
import com.example.pract.entity.ClientRequest;
import com.example.pract.entity.User;
import com.example.pract.repository.ClientRequestRepository;
import com.example.pract.repository.UserRepository;
import com.example.pract.security.CustomUserDetails;
import com.example.pract.service.ClientRequestService;
import com.example.pract.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    private final UserService userService;
    private final ClientRequestService clientRequestService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientRequestRepository clientRequestRepository;
    public UserController(UserService userService, ClientRequestService clientRequestService) {
        this.userService = userService;
        this.clientRequestService = clientRequestService;
    }

    @PostMapping("/signup")
    public ModelAndView processRegistration(@Valid @ModelAttribute("newUserRequestDTO") NewUserRequestDTO newUserRequestDTO, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("errors", bindingResult.getAllErrors());
            modelAndView.addObject("newUserRequestDTO", newUserRequestDTO);
            modelAndView.setViewName("signup");
            return modelAndView;
        }

        if (userRepository.existsByUsername(newUserRequestDTO.getUsername())) {
            modelAndView.addObject("existingUser", "Such user already exists");
            modelAndView.setViewName("signup");
            return modelAndView;
        }

        if (!newUserRequestDTO.getPassword().equals(newUserRequestDTO.getConfirmPassword())) {
            modelAndView.addObject("passMismatch", "Password mismatch");
            modelAndView.addObject("newUserRequestDTO", newUserRequestDTO);
            modelAndView.setViewName("signup");
            return modelAndView;
        }

        User user = userService.registerUser(newUserRequestDTO.getUsername(),
                newUserRequestDTO.getPassword(),
                newUserRequestDTO.getFirstName(),
                newUserRequestDTO.getLastName());

        modelAndView.setViewName("redirect:/auth");
        return modelAndView;
    }

    @GetMapping("/auth")
    public String showAuthPage(Model model) {
        log.info("Handling request to authenticate page");
        return "signin";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        log.info("Handling request to register page");
        model.addAttribute("newUserRequestDTO", new NewUserRequestDTO());
        return "signup";
    }

    @GetMapping("/user/edit")
    public String showEditProfilePage(Model model, Authentication authentication) {
        log.info("Handling request to Edit Profile page");
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        log.info("Current user: " + currentUser.getUsername());

        NewUsernameDTO newUsernameDTO = new NewUsernameDTO();
        newUsernameDTO.setUsername(currentUser.getUsername());

        NewProfileDTO newProfileDTO = new NewProfileDTO();
        newProfileDTO.setFirstName(currentUser.getFirstName());
        newProfileDTO.setLastName(currentUser.getLastName());

        //NewUserRequestDTO newUserRequestDTO = new NewUserRequestDTO();
        //newUserRequestDTO.setUsername(existingUser.getUsername());
        //newUserRequestDTO.setFirstName(existingUser.getFirstName());
        //newUserRequestDTO.setLastName(existingUser.getLastName());

        //model.addAttribute("newUserRequestDTO", newUserRequestDTO);

        model.addAttribute("newUsernameDTO", newUsernameDTO);
        model.addAttribute("newProfileDTO", newProfileDTO);
        //model.addAttribute("username", currentUser.getUsername());

        return "userEditProfile";
    }

    @GetMapping("/user/changeUsername")
    public ModelAndView showChangeUsernamePage(Authentication authentication) {
        log.info("Handling request to Change Username page");
        ModelAndView modelAndView = new ModelAndView();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        log.info("Current user: " + currentUser.getUsername());

        NewUsernameDTO newUsernameDTO = new NewUsernameDTO();
        newUsernameDTO.setUsername(currentUser.getUsername());

        modelAndView.addObject("newUsernameDTO", newUsernameDTO);
        modelAndView.setViewName("userChangeUsername");
        return modelAndView;
    }

    @GetMapping("/user/changeProfile")
    public String ShowChangeProfilePage(Model model, Authentication authentication) {
        log.info("Handling request to Change Profile page");
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        log.info("Current user: " + currentUser.getUsername());

        NewProfileDTO newProfileDTO = new NewProfileDTO();
        newProfileDTO.setFirstName(currentUser.getFirstName());
        newProfileDTO.setLastName(currentUser.getLastName());

        model.addAttribute("newProfileDTO", newProfileDTO);
        return "userChangeProfile";
    }

    @GetMapping("/user/changePassword")
    public String showChangePasswordPage(Model model, Authentication authentication) {
        log.info("Handling request to Change Password page");
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        log.info("Current user: " + currentUser.getUsername());

        NewPasswordDTO newPasswordDTO = new NewPasswordDTO();
        model.addAttribute("newPasswordDTO", newPasswordDTO);
        return "userChangePassword";
    }

    @PostMapping("/user/updateUsername")
    public ModelAndView updateUsername(@Valid @ModelAttribute("newUsernameDTO") NewUsernameDTO newUsernameDTO, Model model, BindingResult bindingResult, Authentication authentication) {
        log.info("Handling request to update username");
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("errors", bindingResult.getAllErrors());
            modelAndView.addObject("newUsernameDTO", newUsernameDTO);
            modelAndView.setViewName("userChangeUsername");
            log.info(String.valueOf(bindingResult.getAllErrors()));
            return modelAndView;
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long curId = userDetails.getId();

        if (userRepository.findByUsername(newUsernameDTO.getUsername()).isPresent() && !userRepository.findByUsername(newUsernameDTO.getUsername()).get().getId().equals(curId)) {
            modelAndView.addObject("existingUser", "Such user already exists");
            modelAndView.setViewName("userChangeUsername");
            return modelAndView;
        }

        userRepository.updateUsername(curId, newUsernameDTO.getUsername());
        userDetails.setUsername(newUsernameDTO.getUsername());
        modelAndView.setViewName("redirect:/main");
        return modelAndView;
    }

    @PostMapping("/user/updateProfile")
    public ModelAndView updateProfile(@Valid @ModelAttribute("newProfileDTO") NewProfileDTO newProfileDTO, Model model, BindingResult bindingResult, Authentication authentication) {
        log.info("Handling request to update firstname, lastname");
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("errors", bindingResult.getAllErrors());
            modelAndView.addObject("newProfileDTO", newProfileDTO);
            modelAndView.setViewName("userChangeProfile");
            log.info(String.valueOf(bindingResult.getAllErrors()));
            return modelAndView;
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long curId = userDetails.getId();

        userRepository.updateProfile(curId, newProfileDTO.getFirstName(), newProfileDTO.getLastName());

        modelAndView.setViewName("redirect:/main");
        return modelAndView;
    }

    @PostMapping("/user/updatePassword")
    public ModelAndView updatePassword(@Valid @ModelAttribute("newPasswordDTO") NewPasswordDTO newPasswordDTO, Model model, BindingResult bindingResult, Authentication authentication) {
        log.info("Handling request to update password");
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("errors", bindingResult.getAllErrors());
            modelAndView.addObject("newPasswordDTO", newPasswordDTO);
            modelAndView.setViewName("userChangePassword");
            log.info(String.valueOf(bindingResult.getAllErrors()));
            return modelAndView;
        }

        if (!newPasswordDTO.getPassword().equals(newPasswordDTO.getConfirmPassword())) {
            modelAndView.addObject("passMismatch", "Password mismatch");
            modelAndView.addObject("newPasswordDTO", newPasswordDTO);
            modelAndView.setViewName("userChangePassword");
            return modelAndView;
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long curId = userDetails.getId();

        userService.updatePassword(curId, newPasswordDTO.getPassword());
        userDetails.setPassword(newPasswordDTO.getPassword());
        modelAndView.setViewName("redirect:/main");
        return modelAndView;
    }

    @PostMapping("/user/updateUser")
    public ModelAndView updateUserInfo(@Valid @ModelAttribute("newUserRequestDTO") NewUserRequestDTO newUserRequestDTO, BindingResult bindingResult, Authentication authentication) {
        log.info("Handling request to update user");
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("errors", bindingResult.getAllErrors());
            modelAndView.addObject("newUserRequestDTO", newUserRequestDTO);
            modelAndView.setViewName("userEditProfile");
            return modelAndView;
        }

        User existingUser = userRepository.findByUsername(newUserRequestDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found")); //другой пользователь с таким же именем в базе
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long curId = userDetails.getId(); //id текущего пользователя до редактирования

        if (existingUser != null && !existingUser.getId().equals(curId)) {
            // Новое имя занято другим пользователем
            modelAndView.addObject("existingUser", "Such user already exists");
            modelAndView.setViewName("userEditProfile");
            return modelAndView;
        }

        if (!newUserRequestDTO.getPassword().equals(newUserRequestDTO.getConfirmPassword())) {
            modelAndView.addObject("passMismatch", "Password mismatch");
            modelAndView.addObject("newUserRequestDTO", newUserRequestDTO);
            modelAndView.setViewName("userEditProfile");
            return modelAndView;
        }

        User updatedUser = userService.updateUser(
                userDetails.getId(),
                newUserRequestDTO.getUsername(),
                newUserRequestDTO.getPassword(),
                newUserRequestDTO.getFirstName(),
                newUserRequestDTO.getLastName());

        CustomUserDetails newUserDetails = new CustomUserDetails(
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.getPassword(),
                AuthorityUtils.createAuthorityList(updatedUser.getRole().name())
        );

        UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(
                newUserDetails, null, newUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);

        modelAndView.setViewName("redirect:/main");
        return modelAndView;
    }

    @GetMapping("/user/myRequests")
    public String showMyRequestsPage(Model model, Authentication authentication) {
        log.info("Handling request to User Requests page");
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        log.info("userId: " + userId);
        List<ClientRequest> requests = clientRequestRepository.findByClientId(userId);
        model.addAttribute("requests", requests);
        return "myRequests";
    }
}
