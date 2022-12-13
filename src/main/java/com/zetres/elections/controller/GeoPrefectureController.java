package com.zetres.elections.controller;

import com.zetres.elections.domain.GeoRegion;
import com.zetres.elections.model.GeoPrefectureDTO;
import com.zetres.elections.repos.GeoRegionRepository;
import com.zetres.elections.service.GeoPrefectureService;
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
@RequestMapping("/geoPrefectures")
public class GeoPrefectureController {

    private final GeoPrefectureService geoPrefectureService;
    private final GeoRegionRepository geoRegionRepository;

    public GeoPrefectureController(final GeoPrefectureService geoPrefectureService,
            final GeoRegionRepository geoRegionRepository) {
        this.geoPrefectureService = geoPrefectureService;
        this.geoRegionRepository = geoRegionRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        var regions = geoRegionRepository
                .findAll()
                .stream()
                .collect(
                        Collectors.toMap(
                                GeoRegion::getPk, GeoRegion::getName)
                );
        model.addAttribute("fkRegionValues", MiscUtils.sortByValue(regions, true));
    }
    @GetMapping
    public String list(final Model model) {
        model.addAttribute("geoPrefectures", geoPrefectureService.findAll());
        return "geoPrefecture/list";
    }
    @GetMapping("/{pkRegion}")
    public String listByPK(@PathVariable final Object pkRegion, final Model model) {
        model.addAttribute("geoPrefectures", geoPrefectureService
                .findAll()
                .parallelStream()
                .filter(geoPrefectureDTO ->
                            geoPrefectureDTO.getFkRegion()==Integer.parseInt((String)pkRegion)).toList());
        return "geoPrefecture/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("geoPrefecture") final GeoPrefectureDTO geoPrefectureDTO) {
        return "geoPrefecture/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("geoPrefecture") @Valid final GeoPrefectureDTO geoPrefectureDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "geoPrefecture/add";
        }
        geoPrefectureService.create(geoPrefectureDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("geoPrefecture.create.success"));
        return "redirect:/geoPrefectures";
    }

    @GetMapping("/edit/{pk}")
    public String edit(@PathVariable final Integer pk, final Model model) {
        model.addAttribute("geoPrefecture", geoPrefectureService.get(pk));
        return "geoPrefecture/edit";
    }

    @PostMapping("/edit/{pk}")
    public String edit(@PathVariable final Integer pk,
            @ModelAttribute("geoPrefecture") @Valid final GeoPrefectureDTO geoPrefectureDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "geoPrefecture/edit";
        }
        geoPrefectureService.update(pk, geoPrefectureDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("geoPrefecture.update.success"));
        return "redirect:/geoPrefectures";
    }

    @PostMapping("/delete/{pk}")
    public String delete(@PathVariable final Integer pk,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = geoPrefectureService.getReferencedWarning(pk);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            geoPrefectureService.delete(pk);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("geoPrefecture.delete.success"));
        }
        return "redirect:/geoPrefectures";
    }

}
