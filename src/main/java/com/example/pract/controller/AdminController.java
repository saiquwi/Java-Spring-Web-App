package com.example.pract.controller;

import com.example.pract.dto.NewCriminalDTO;
import com.example.pract.dto.NewsDTO;
import com.example.pract.entity.ClientRequest;
import com.example.pract.entity.Criminal;
import com.example.pract.entity.News;
import com.example.pract.enums.CriminalStatus;
import com.example.pract.enums.NewsType;
import com.example.pract.enums.ReqStatus;
import com.example.pract.repository.ClientRequestRepository;
import com.example.pract.repository.CriminalRepository;
import com.example.pract.repository.NewsRepository;
import com.example.pract.service.CriminalService;
import com.example.pract.service.NewsService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    private final CriminalService criminalService;

    private final NewsService newsService;

    @Autowired
    private ClientRequestRepository clientRequestRepository;

    @Autowired
    private CriminalRepository criminalRepository;

    @Autowired
    private NewsRepository newsRepository;

    private AdminController(CriminalService criminalService, NewsService newsService) {
        this.criminalService = criminalService;
        this.newsService = newsService;
    }

    @GetMapping("/admin/unansweredRequests")
    public String handleUnansweredRequests(Model model) {
        log.info("Handling request to Interpol All Users page");
        Iterable<ClientRequest> requests = clientRequestRepository.findByStatus(ReqStatus.REQUESTED);
        model.addAttribute("requests", requests);
        return "adminUnansweredRequests";
    }

    @PostMapping("/admin/acceptRequest/{id}")
    public String handleAcceptRequest(@PathVariable("id") Long id, Model model) {
        log.info("Handling accepting request");
        return changeMissingStatus(id, ReqStatus.ACCEPTED);
    }

    @PostMapping("/admin/denyRequest/{id}")
    public String handleDenyRequest(@PathVariable("id") Long id, Model model) {
        log.info("Handling denying request");
        return changeMissingStatus(id, ReqStatus.DENIED);
    }

    @PostMapping("/admin/missingStatusFound/{id}")
    public String handleMissingChangeStatusFound(@PathVariable("id") Long id, Model model) {
        log.info("Handling request to change missing status to Found");
        return changeMissingStatus(id, ReqStatus.FOUND);
    }

    @PostMapping("/admin/missingStatusDead/{id}")
    public String handleMissingChangeStatusDead(@PathVariable("id") Long id, Model model) {
        log.info("Handling request to change missing status to Dead");
        return changeMissingStatus(id, ReqStatus.DEAD);
    }

    @PostMapping("/admin/missingStatusNoTrace/{id}")
    public String handleMissingChangeStatusNoTrace(@PathVariable("id") Long id, Model model) {
        log.info("Handling request to change missing status to No Trace");
        return changeMissingStatus(id, ReqStatus.NO_TRACE);
    }

    private String changeMissingStatus(Long id, ReqStatus status) {
        int cr = clientRequestRepository.changeStatus(status, id);
        if (status == ReqStatus.ACCEPTED || status == ReqStatus.DENIED) {
            return "redirect:/admin/unansweredRequests";
        } else {
            return "redirect:/interpol/finishedMissing";
        }
    }

    @GetMapping("/admin/newCriminal")
    public String showNewCriminalPage(Model model) {
        log.info("Handling request to New Criminal page");
        model.addAttribute("newCriminalDTO", new NewCriminalDTO());
        return "adminNewCriminal";
    }

    @PostMapping("/admin/addCriminal")
    public ModelAndView handleAddCriminal(@Valid @ModelAttribute("newCriminalDTO") NewCriminalDTO newCriminalDTO, BindingResult bindingResult, Authentication authentication, RedirectAttributes redirectAttributes) {
        log.info("Handling request to add Criminal");
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("errors", bindingResult.getAllErrors());
            modelAndView.addObject("newCriminalDTO", newCriminalDTO);
            modelAndView.setViewName("adminNewCriminal");
            log.info(String.valueOf(bindingResult.getAllErrors()));
            return modelAndView;
        }

        Criminal criminal = criminalService.addCriminal(
                newCriminalDTO.getCriminalFirstName(),
                newCriminalDTO.getCriminalLastName(),
                newCriminalDTO.getLocation(),
                newCriminalDTO.getCrime(),
                newCriminalDTO.getWantedSince(),
                newCriminalDTO.getReward());

        modelAndView.setViewName("redirect:/interpol/wanted");
        return modelAndView;
    }

    @PostMapping("/admin/criminalStatusArrested/{id}")
    public String handleCriminalChangeStatusArrested(@PathVariable("id") Long id, Model model) {
        log.info("Handling request to change criminal status to Arrested");
        return changeCriminalStatus(id, CriminalStatus.ARRESTED);
    }

    @PostMapping("/admin/criminalStatusDead/{id}")
    public String handleCriminalChangeStatusDead(@PathVariable("id") Long id, Model model) {
        log.info("Handling request to change criminal status to Dead");
        return changeCriminalStatus(id, CriminalStatus.DEAD);
    }

    @PostMapping("/admin/criminalStatusNoTrace/{id}")
    public String handleCriminalChangeStatusNoTrace(@PathVariable("id") Long id, Model model) {
        log.info("Handling request to change criminal status to No Trace");
        return changeCriminalStatus(id, CriminalStatus.NO_TRACE);
    }

    @PostMapping("/admin/criminalStatusWanted/{id}")
    public String handleCriminalChangeStatusWanted(@PathVariable("id") Long id, Model model) {
        log.info("Handling request to change criminal status to Wanted");
        return changeCriminalStatus(id, CriminalStatus.WANTED);
    }

    private String changeCriminalStatus(Long id, CriminalStatus status) {
        int cr = criminalRepository.changeStatus(status, id);
        if (status == CriminalStatus.WANTED) {
            return "redirect:/interpol/wanted";
        } else {
            return "redirect:/interpol/finishedCriminals";
        }
    }

    @GetMapping("/admin/createNews")
    public String showNewsCreationPage(Model model) {
        log.info("Handling request to News creation page");
        model.addAttribute("newsDTO", new NewsDTO());
        model.addAttribute("types", NewsType.values());
        return "adminCreateNews";
    }

    @GetMapping("/admin/archivedNews")
    public String showNewsArchivePage(Model model) {
        log.info("Handling request to News Archive page");
        Iterable<News> newsList = newsRepository.findByStatus(NewsType.ARCHIVED);
        model.addAttribute("newsList", newsList);
        return "archivedNews";
    }

    @PostMapping("/admin/addNews")
    public ModelAndView handleAddNews(@Valid @ModelAttribute("newsDTO") NewsDTO newsDTO, BindingResult bindingResult, Authentication authentication, RedirectAttributes redirectAttributes) {
        log.info("Handling request to add News");
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("errors", bindingResult.getAllErrors());
            modelAndView.addObject("newsDTO", newsDTO);
            modelAndView.setViewName("adminCreateNews");
            log.info(String.valueOf(bindingResult.getAllErrors()));
            return modelAndView;
        }

        News news = newsService.addNews(
                newsDTO.getTitle(),
                newsDTO.getDescription(),
                newsDTO.getType()
        );

        modelAndView.setViewName("redirect:/main");
        return modelAndView;
    }
}
