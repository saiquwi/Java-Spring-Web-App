package com.example.pract.controller;

import com.example.pract.dto.NewClientReqMissingDTO;
import com.example.pract.dto.NewUserRequestDTO;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

        modelAndView.setViewName("index");
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

    @GetMapping("/interpol/users")
    public String showAllUsersPage(Model model) {
        log.info("Handling request to Interpol All Users page");
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "usersIP";
    }

    @GetMapping("/main")
    public String showMainPage(Model model, Authentication authentication) {
        log.info("Handling request to Main page");
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        model.addAttribute("username", username);
        return "main";
    }

    @GetMapping("/user/edit")
    public String showEditProfilePage(Model model, Authentication authentication) {
        log.info("Handling request to Edit Profile page");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User existingUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        log.info("Existing user: " + existingUser.getUsername());

        NewUserRequestDTO newUserRequestDTO = new NewUserRequestDTO();
        newUserRequestDTO.setUsername(existingUser.getUsername());
        newUserRequestDTO.setFirstName(existingUser.getFirstName());
        newUserRequestDTO.setLastName(existingUser.getLastName());

        model.addAttribute("newUserRequestDTO", newUserRequestDTO);
        return "userEditProfile";
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
                updatedUser.getUsername(),
                updatedUser.getPassword(),
                AuthorityUtils.createAuthorityList(updatedUser.getRole().name()),
                updatedUser.getId()
        );

        UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(
                newUserDetails, null, newUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);

        modelAndView.addObject("username", updatedUser.getUsername());
        modelAndView.setViewName("main");
        return modelAndView;
    }

    @GetMapping("/user/newRequest")
    public String showNewRequestPage(Model model) {
        log.info("Handling request to New Request page");
        model.addAttribute("newClientReqMissingDTO", new NewClientReqMissingDTO());
        return "newRequest";
    }

    @GetMapping("/user/myRequests")
    public String showMyRequestsPage(Model model, Authentication authentication) {
        log.info("Handling request to User Requests page");
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        //Optional<User> userOptional = userRepository.findById(userId);
        //User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        log.info("userId: " + userId);
        List<ClientRequest> requests = clientRequestRepository.findByClientId(userId);
        model.addAttribute("requests", requests);
        return "myRequests";
    }

    @PostMapping("/user/addRequest")
    public ModelAndView addMissingRequest(@Valid @ModelAttribute("newClientReqMissingDTO") NewClientReqMissingDTO newClientReqMissingDTO, BindingResult bindingResult, Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("errors", bindingResult.getAllErrors());
            modelAndView.addObject("newClientReqMissingDTO", newClientReqMissingDTO);
            modelAndView.setViewName("newRequest");
            log.info(String.valueOf(bindingResult.getAllErrors()));
            return modelAndView;
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));

        ClientRequest clientRequest = clientRequestService.addMissingRequest(
                user,
                newClientReqMissingDTO.getMissFirstName(),
                newClientReqMissingDTO.getMissLastName(),
                newClientReqMissingDTO.getLocLastSeen(),
                newClientReqMissingDTO.getMissingSince(),
                newClientReqMissingDTO.getOtherInfo(),
                newClientReqMissingDTO.getReward());

        modelAndView.setViewName("main");
        return modelAndView;
    }

    @PostMapping("/user/editRequest/{id}")
    public String showEditRequestPage(@PathVariable("id") Long id, Model model) {
        log.info("Handling request to Edit Request page");

        ClientRequest clientRequest = clientRequestRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Request not found"));
        model.addAttribute("reqId", id);

        NewClientReqMissingDTO newClientReqMissingDTO = new NewClientReqMissingDTO();
        newClientReqMissingDTO.setMissFirstName(clientRequest.getFirstName());
        newClientReqMissingDTO.setMissLastName(clientRequest.getLastName());
        newClientReqMissingDTO.setLocLastSeen(clientRequest.getLocation());
        newClientReqMissingDTO.setMissingSince(clientRequest.getMissingSinceDate());
        newClientReqMissingDTO.setOtherInfo(clientRequest.getOtherInfo());
        newClientReqMissingDTO.setReward(clientRequest.getReward());

        model.addAttribute("newClientReqMissingDTO", newClientReqMissingDTO);

        return "editRequest";
    }

    @PostMapping("/user/updateRequest/{id}")
    public ModelAndView updateRequest(@Valid @ModelAttribute("newClientReqMissingDTO") NewClientReqMissingDTO newClientReqMissingDTO, @PathVariable("id") Long reqId, BindingResult bindingResult, Authentication authentication) {
        log.info("Handling request to update request");
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("errors", bindingResult.getAllErrors());
            modelAndView.addObject("newClientReqMissingDTO", newClientReqMissingDTO);
            modelAndView.setViewName("editRequest");
            return modelAndView;
        }

        ClientRequest clientRequest = clientRequestService.updateRequest(
                reqId,
                newClientReqMissingDTO.getMissFirstName(),
                newClientReqMissingDTO.getMissLastName(),
                newClientReqMissingDTO.getLocLastSeen(),
                newClientReqMissingDTO.getMissingSince(),
                newClientReqMissingDTO.getOtherInfo(),
                newClientReqMissingDTO.getReward());

        modelAndView.setViewName("main");
        return modelAndView;
    }

    @PostMapping ("/user/deleteRequest/{id}")
    public String deleteRequest(@PathVariable("id") Long id) {
        log.info("Handling request to delete client request");
        clientRequestRepository.deleteById(id);
        return "main";
    }
}
