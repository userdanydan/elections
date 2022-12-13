package com.zetres.elections.controller;

import com.zetres.elections.domain.GeoLocalite;
import com.zetres.elections.model.GeoCentreDTO;
import com.zetres.elections.repos.GeoCentresAuditRepository;
import com.zetres.elections.repos.GeoLocaliteRepository;
import com.zetres.elections.service.GeoCentreService;
import com.zetres.elections.service.GeoRegionService;
import com.zetres.elections.util.WebUtils;
import org.hibernate.envers.AuditReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/geoCentres")
public class GeoCentreController {

    private final GeoCentreService geoCentreService;
    private final GeoLocaliteRepository geoLocaliteRepository;
    @Value("${INSERT_RECORDS}")
    private String insertRecordsEnvVar;
    @Autowired
    private AuditReader auditReader;

    @Autowired
    GeoRegionService geoRegionService;
    @Autowired private GeoCentresAuditRepository geoCentresAuditRepository;

    private String updateVersion(){
        Integer version = (Integer) auditReader.getRevisionNumberForDate(Timestamp.valueOf(LocalDateTime.now()));
        return Integer.toString(version);
    }
    public GeoCentreController(final GeoCentreService geoCentreService,
            final GeoLocaliteRepository geoLocaliteRepository) {
        this.geoCentreService = geoCentreService;
        this.geoLocaliteRepository = geoLocaliteRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        var localites =  geoLocaliteRepository
                .findAll()
                .stream()
                .collect(
                        Collectors.toMap(
                                GeoLocalite::getPk, GeoLocalite::getName)
                );
        model.addAttribute("fkLocaliteValues",sortByValue(localites, true));

        UUID geoCentrePrimaryKey = UUID.randomUUID();
        model.addAttribute("geoCentrePrimaryKey", geoCentrePrimaryKey.toString().replace("-", "").toUpperCase());



    }
    //https://stackoverflow.com/questions/8119366/sorting-hashmap-by-values
    private static Map<String, String> sortByValue(Map<String, String> unsortMap, final boolean order)
    {
        List<Map.Entry<String, String>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));

    }
    @GetMapping
    public String list(final Model model) {
        model.addAttribute("geoCentres", geoCentreService.findAll());
        return "geoCentre/list";
    }
    @GetMapping("/{pkLocalite}")
    public String listByPK(@PathVariable final String pkLocalite, final Model model) {
        model.addAttribute("geoCentres", geoCentreService.findByParentLieuId(pkLocalite));
        return "geoCentre/list";
    }
    @GetMapping("/add")
    public String add(@ModelAttribute("geoCentre") final GeoCentreDTO geoCentreDTO) {
        return "geoCentre/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("geoCentre") @Valid final GeoCentreDTO geoCentreDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("pk") && geoCentreDTO.getPk() == null) {
            bindingResult.rejectValue("pk", "NotNull");
        }
        if (!bindingResult.hasFieldErrors("pk") && geoCentreService.pkExists(geoCentreDTO.getPk())) {
            bindingResult.rejectValue("pk", "Exists.geoCentre.pk");
        }
        if (bindingResult.hasErrors()) {
            return "geoCentre/add";
        }
        geoCentreService.create(geoCentreDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("geoCentre.create.success"));
        return "redirect:/geoCentres/"+ geoCentreDTO.getFkLocalite();
    }

    @GetMapping("/edit/{pk}")
    public String edit(@PathVariable final String pk, final Model model) {
        model.addAttribute("geoCentre", geoCentreService.get(pk));
        return "geoCentre/edit";
    }

    @PostMapping("/edit/{pk}")
    public String edit(@PathVariable final String pk,
            @ModelAttribute("geoCentre") @Valid final GeoCentreDTO geoCentreDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "geoCentre/edit";
        }
        geoCentreService.update(pk, geoCentreDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("geoCentre.update.success"));
        return "redirect:/geoCentres";
    }

    @PostMapping("/delete/{pk}")
    public String delete(@PathVariable final String pk,
            final RedirectAttributes redirectAttributes) {
        geoCentreService.delete(pk);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("geoCentre.delete.success"));
        return "redirect:/geoCentres";
    }

    @GetMapping(value = "/web/export", produces = "application/sql")
    @ResponseBody
    public FileSystemResource export( HttpServletResponse response){
        var version = this.geoRegionService.versionOfTheCurrentHierarchicalGeoEntities();
        //this.geoRegionService.addVersionToFile();
        response.setHeader("Content-Disposition", "attachment; filename=export_Version"+version+".sql");
        return new FileSystemResource(new File(insertRecordsEnvVar));
    }
}