package com.zetres.elections.controller;

import com.zetres.elections.model.GeoRegionDTO;
import com.zetres.elections.service.GeoRegionService;
import com.zetres.elections.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/geoRegions")
public class GeoRegionController {

    private final GeoRegionService geoRegionService;

    public GeoRegionController(final GeoRegionService geoRegionService) {
        this.geoRegionService = geoRegionService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("geoRegions", geoRegionService.findAll());
        return "geoRegion/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("geoRegion") final GeoRegionDTO geoRegionDTO) {
        return "geoRegion/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("geoRegion") @Valid final GeoRegionDTO geoRegionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "geoRegion/add";
        }
        geoRegionService.create(geoRegionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("geoRegion.create.success"));
        return "redirect:/geoRegions";
    }

    @GetMapping("/edit/{pk}")
    public String edit(@PathVariable final Integer pk, final Model model) {
        model.addAttribute("geoRegion", geoRegionService.get(pk));
        return "geoRegion/edit";
    }

    @PostMapping("/edit/{pk}")
    public String edit(@PathVariable final Integer pk,
            @ModelAttribute("geoRegion") @Valid final GeoRegionDTO geoRegionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "geoRegion/edit";
        }
        geoRegionService.update(pk, geoRegionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("geoRegion.update.success"));
        return "redirect:/geoRegions";
    }

    @PostMapping("/delete/{pk}")
    public String delete(@PathVariable final Integer pk,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = geoRegionService.getReferencedWarning(pk);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            geoRegionService.delete(pk);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("geoRegion.delete.success"));
        }
        return "redirect:/geoRegions";
    }

}
