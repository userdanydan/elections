package com.zetres.elections.controller;

import com.zetres.elections.domain.GeoCommune;
import com.zetres.elections.model.GeoCantonDTO;
import com.zetres.elections.repos.GeoCommuneRepository;
import com.zetres.elections.service.GeoCantonService;
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
@RequestMapping("/geoCantons")
public class GeoCantonController {

    private final GeoCantonService geoCantonService;
    private final GeoCommuneRepository geoCommuneRepository;

    public GeoCantonController(final GeoCantonService geoCantonService,
            final GeoCommuneRepository geoCommuneRepository) {
        this.geoCantonService = geoCantonService;
        this.geoCommuneRepository = geoCommuneRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        var cantons =  geoCommuneRepository
                .findAll()
                .stream()
                .collect(
                        Collectors.toMap(
                                GeoCommune::getPk, GeoCommune::getName)
                );
        model.addAttribute("fkCommuneValues", MiscUtils.sortByValue(cantons, true));
    }
    @GetMapping
    public String list(final Model model) {
        model.addAttribute("geoCantons", geoCantonService.findAll());
        return "geoCanton/list";
    }
    @GetMapping("/{pkCommune}")
    public String listByPK(@PathVariable final Object pkCommune, final Model model) {
        model.addAttribute("geoCantons", geoCantonService
                .findAll()
                .parallelStream()
                .filter(geoCantonDTO ->
                        geoCantonDTO.getFkCommune()==Integer.parseInt((String)pkCommune)).toList());
        return "geoCanton/list";
    }
    @GetMapping("/add")
    public String add(@ModelAttribute("geoCanton") final GeoCantonDTO geoCantonDTO) {
        return "geoCanton/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("geoCanton") @Valid final GeoCantonDTO geoCantonDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "geoCanton/add";
        }
        geoCantonService.create(geoCantonDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("geoCanton.create.success"));
        return "redirect:/geoCantons";
    }

    @GetMapping("/edit/{pk}")
    public String edit(@PathVariable final Integer pk, final Model model) {
        model.addAttribute("geoCanton", geoCantonService.get(pk));
        return "geoCanton/edit";
    }

    @PostMapping("/edit/{pk}")
    public String edit(@PathVariable final Integer pk,
            @ModelAttribute("geoCanton") @Valid final GeoCantonDTO geoCantonDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "geoCanton/edit";
        }
        geoCantonService.update(pk, geoCantonDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("geoCanton.update.success"));
        return "redirect:/geoCantons";
    }

    @PostMapping("/delete/{pk}")
    public String delete(@PathVariable final Integer pk,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = geoCantonService.getReferencedWarning(pk);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            geoCantonService.delete(pk);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("geoCanton.delete.success"));
        }
        return "redirect:/geoCantons";
    }

}
