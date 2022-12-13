package com.zetres.elections.controller;

import com.zetres.elections.domain.GeoPrefecture;
import com.zetres.elections.model.GeoCommuneDTO;
import com.zetres.elections.repos.GeoPrefectureRepository;
import com.zetres.elections.service.GeoCommuneService;
import com.zetres.elections.util.MiscUtils;
import com.zetres.elections.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/geoCommunes")
public class GeoCommuneController {

    private final GeoCommuneService geoCommuneService;
    private final GeoPrefectureRepository geoPrefectureRepository;

    public GeoCommuneController(final GeoCommuneService geoCommuneService,
            final GeoPrefectureRepository geoPrefectureRepository) {
        this.geoCommuneService = geoCommuneService;
        this.geoPrefectureRepository = geoPrefectureRepository;
    }


    @ModelAttribute
    public void prepareContext(final Model model) {
        var prefectures = geoPrefectureRepository
                .findAll()
                .stream()
                .collect(
                        Collectors.toMap(
                                GeoPrefecture::getPk, GeoPrefecture::getName)
                );
        model.addAttribute("fkPrefectureValues", MiscUtils.sortByValue(prefectures, true));
    }
    @GetMapping
    public String list(final Model model) {
        model.addAttribute("geoCommunes", geoCommuneService.findAll());
        return "geoCommune/list";
    }
    @GetMapping("/{pkPrefecture}")
    public String listByPK(@PathVariable final Object pkPrefecture, final Model model) {
        model.addAttribute("geoCommunes", geoCommuneService
                .findAll()
                .parallelStream()
                .filter(geoCommuneDTO ->
                        geoCommuneDTO.getFkPrefecture()==Integer.parseInt((String)pkPrefecture)).toList());
        return "geoCommune/list";
    }
    @GetMapping("/add")
    public String add(@ModelAttribute("geoCommune") final GeoCommuneDTO geoCommuneDTO) {
        return "geoCommune/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("geoCommune") @Valid final GeoCommuneDTO geoCommuneDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "geoCommune/add";
        }
        geoCommuneService.create(geoCommuneDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("geoCommune.create.success"));
        return "redirect:/geoCommunes";
    }

    @GetMapping("/edit/{pk}")
    public String edit(@PathVariable final Integer pk, final Model model) {
        model.addAttribute("geoCommune", geoCommuneService.get(pk));
        return "geoCommune/edit";
    }

    @PostMapping("/edit/{pk}")
    public String edit(@PathVariable final Integer pk,
            @ModelAttribute("geoCommune") @Valid final GeoCommuneDTO geoCommuneDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "geoCommune/edit";
        }
        geoCommuneService.update(pk, geoCommuneDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("geoCommune.update.success"));
        return "redirect:/geoCommunes";
    }

    @PostMapping("/delete/{pk}")
    public String delete(@PathVariable final Integer pk,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = geoCommuneService.getReferencedWarning(pk);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            geoCommuneService.delete(pk);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("geoCommune.delete.success"));
        }
        return "redirect:/geoCommunes";
    }

}
