package com.zetres.elections.controller;

import com.zetres.elections.domain.GeoCanton;
import com.zetres.elections.model.GeoLocaliteDTO;
import com.zetres.elections.repos.GeoCantonRepository;
import com.zetres.elections.service.GeoLocaliteService;
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
@RequestMapping("/geoLocalites")
public class GeoLocaliteController {

    private final GeoLocaliteService geoLocaliteService;
    private final GeoCantonRepository geoCantonRepository;

    public GeoLocaliteController(final GeoLocaliteService geoLocaliteService,
            final GeoCantonRepository geoCantonRepository) {
        this.geoLocaliteService = geoLocaliteService;
        this.geoCantonRepository = geoCantonRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        var cantons =  geoCantonRepository
                .findAll()
                .stream()
                .collect(
                        Collectors.toMap(
                                GeoCanton::getPk, GeoCanton::getName)
                );
        model.addAttribute("fkCantonValues", MiscUtils.sortByValue(cantons, true));
    }


    @GetMapping
    public String list(final Model model) {
        model.addAttribute("geoLocalites", geoLocaliteService.findAll());
        return "geoLocalite/list";
    }
    @GetMapping("/{pkCanton}")
    public String listByPK(@PathVariable final String pkCanton, final Model model) {
        model.addAttribute("geoLocalites", geoLocaliteService
                .findByCantonId(Integer.parseInt(pkCanton)));
        return "geoLocalite/list";
    }
    @GetMapping("/add")
    public String add(@ModelAttribute("geoLocalite") final GeoLocaliteDTO geoLocaliteDTO) {
        return "geoLocalite/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("geoLocalite") @Valid final GeoLocaliteDTO geoLocaliteDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("pk") && geoLocaliteDTO.getPk() == null) {
            bindingResult.rejectValue("pk", "NotNull");
        }
        if (!bindingResult.hasFieldErrors("pk") && geoLocaliteService.pkExists(geoLocaliteDTO.getPk())) {
            bindingResult.rejectValue("pk", "Exists.geoLocalite.pk");
        }
        if (bindingResult.hasErrors()) {
            return "geoLocalite/add";
        }
        geoLocaliteService.create(geoLocaliteDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("geoLocalite.create.success"));
        return "redirect:/geoLocalites";
    }

    @GetMapping("/edit/{pk}")
    public String edit(@PathVariable final String pk, final Model model) {
        model.addAttribute("geoLocalite", geoLocaliteService.get(pk));
        return "geoLocalite/edit";
    }

    @PostMapping("/edit/{pk}")
    public String edit(@PathVariable final String pk,
            @ModelAttribute("geoLocalite") @Valid final GeoLocaliteDTO geoLocaliteDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "geoLocalite/edit";
        }
        geoLocaliteService.update(pk, geoLocaliteDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("geoLocalite.update.success"));
        return "redirect:/geoLocalites";
    }

    @PostMapping("/delete/{pk}")
    public String delete(@PathVariable final String pk,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = geoLocaliteService.getReferencedWarning(pk);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            geoLocaliteService.delete(pk);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("geoLocalite.delete.success"));
        }
        return "redirect:/geoLocalites";
    }

}
