package com.example.pract.controller;

import com.example.pract.dto.NewClientReqMissingDTO;
import com.example.pract.entity.ClientRequest;
import com.example.pract.entity.Criminal;
import com.example.pract.entity.User;
import com.example.pract.enums.CriminalStatus;
import com.example.pract.enums.ReqStatus;
import com.example.pract.enums.Role;
import com.example.pract.repository.ClientRequestRepository;
import com.example.pract.repository.CriminalRepository;
import com.example.pract.repository.UserRepository;
import com.example.pract.security.CustomUserDetails;
import com.example.pract.service.ClientRequestService;
import com.example.pract.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    private final ClientRequestService clientRequestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRequestRepository clientRequestRepository;

    @Autowired
    private CriminalRepository criminalRepository;

    public MainController(ClientRequestService clientRequestService) {
        this.clientRequestService = clientRequestService;
    }

    @GetMapping("/")
    public String showWelcomePage(Model model) {
        log.info("Handling request to index page");
        return "index";
    }

    @GetMapping("/home")
    public String showHomePage(Model model) {
        log.info("Handling request to home page");
        return "home";
    }

    @GetMapping("/main")
    public String showMainPage(Model model, Authentication authentication) {
        log.info("Handling request to Main page");
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        log.info("username: " + username);
        model.addAttribute("username", username);
        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"))) {
            return "adminHome";
        } else {
            return "userHome";
        }
    }

    @GetMapping("/interpol/users")
    public String showAllUsersPage(Model model) {
        log.info("Handling request to Interpol All Users page");
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "usersIP";
    }

    @GetMapping("/interpol/curmissing")
    public String showCurrentMissingPage(Model model, Authentication authentication) {
        log.info("Handling request to Interpol Current Missing page");
        Iterable<ClientRequest> missing = clientRequestRepository.findByStatus(ReqStatus.ACCEPTED);
        model.addAttribute("missing", missing);
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");
        model.addAttribute("role", role);
        return "missingIP";
    }

    @GetMapping("/interpol/newRequest")
    public String showNewRequestPage(Model model) {
        log.info("Handling request to New Request page");
        model.addAttribute("newClientReqMissingDTO", new NewClientReqMissingDTO());
        return "newRequest";
    }

    @PostMapping("/interpol/addRequest")
    public ModelAndView addMissingRequest(@Valid @ModelAttribute("newClientReqMissingDTO") NewClientReqMissingDTO newClientReqMissingDTO, BindingResult bindingResult, Authentication authentication, RedirectAttributes redirectAttributes) {
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

        if (user.getRole() == Role.ADMIN) {
            modelAndView.setViewName("redirect:/interpol/curmissing");
        } else {
            modelAndView.setViewName("redirect:/user/myRequests");
        }
        return modelAndView;
    }

    @GetMapping("/interpol/editRequest/{id}")
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

    @PostMapping("/interpol/updateRequest/{id}")
    public ModelAndView updateRequest(@Valid @ModelAttribute("newClientReqMissingDTO") NewClientReqMissingDTO newClientReqMissingDTO,
                                @PathVariable("id") Long reqId,
                                Model model,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Authentication authentication) {
        log.info("Handling request to update request");
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("errors", bindingResult.getAllErrors());
            modelAndView.addObject("newClientReqMissingDTO", newClientReqMissingDTO);
            modelAndView.setViewName("editRequest");
            log.info(String.valueOf(bindingResult.getAllErrors()));
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

        return showPageDueRole(authentication);
    }

    @PostMapping ("/interpol/deleteRequest/{id}")
    public ModelAndView deleteRequest(@PathVariable("id") Long id, Authentication authentication) {
        log.info("Handling request to delete client request");
        clientRequestRepository.deleteById(id);
        return showPageDueRole(authentication);
    }

    private ModelAndView showPageDueRole(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        ModelAndView modelAndView = new ModelAndView();
        if (user.getRole() == Role.ADMIN) {
            modelAndView.setViewName("adminUnansweredRequests");
        } else {
            modelAndView.setViewName("myRequests");
        }
        return modelAndView;
    }

    @GetMapping("/interpol/wanted")
    public String showWantedPage(Model model, Authentication authentication) {
        log.info("Handling request to Interpol Wanted page");
        Iterable<Criminal> wanted = criminalRepository.findByStatus(CriminalStatus.WANTED);
        model.addAttribute("wanted", wanted);
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");
        model.addAttribute("role", role);
        return "wantedIP";
    }

    @PostMapping("/multiply")
    public String handleMultiply(@RequestParam("number") String numberStr, Model model) {
        log.info("Handling request to multiplication page");
        int number = Integer.parseInt(numberStr);
        int result = number * 2;
        model.addAttribute("result", result);
        model.addAttribute("number", number);
        return "multiplication";
    }

    @GetMapping("/interpol/finishedCriminals")
    public String showFinishedCriminalsPage(Model model, Authentication authentication) {
        log.info("Handling request to Interpol Finished Criminal Cases page");
        Iterable<Criminal> finishedCriminals = criminalRepository.findAllExceptStatus(CriminalStatus.WANTED);
        model.addAttribute("finishedCriminals", finishedCriminals);
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");
        model.addAttribute("role", role);
        return "criminalsFinishedCases";
    }

    @GetMapping("/interpol/finishedMissing")
    public String showFinishedMissingPage(Model model, Authentication authentication) {
        log.info("Handling request to Interpol Finished Missing Cases page");
        List<ReqStatus> statuses = Arrays.asList(ReqStatus.FOUND, ReqStatus.DEAD, ReqStatus.NO_TRACE);
        Iterable<ClientRequest> finishedMissing = clientRequestRepository.findByStatuses(statuses);
        model.addAttribute("finishedMissing", finishedMissing);
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");
        model.addAttribute("role", role);
        return "missingFinishedCases";
    }
}