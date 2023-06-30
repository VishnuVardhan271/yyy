package com.pennant.prodmtr.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.pennant.prodmtr.model.Dto.ModuleDTO;
import com.pennant.prodmtr.model.Input.ModuleInput;
import com.pennant.prodmtr.service.Interface.FunctionalUnitService;
import com.pennant.prodmtr.service.Interface.ModuleService;

@Controller
public class ModuleController {
	private static final Logger logger = LoggerFactory.getLogger(ModuleController.class);

	ModuleService moduleService;
	@Autowired
	FunctionalUnitService Funitservice;

	@Autowired
	public ModuleController(ModuleService moduleService) {
		this.moduleService = moduleService;
	}

	// Method to get create module jsp page
	@RequestMapping(value = "/createModule", method = RequestMethod.GET)
	public String createModule(Model model) {
		logger.info("module added");

		return "Addmodule";
	}

	// Method for creating module in project
	@RequestMapping(value = "/createModulesuccess", method = RequestMethod.POST)
	public String Createmodulesuccess(@Validated ModuleInput moduleinput, Model model) {

		moduleService.createModule(moduleinput);
		logger.info("module success");

		Integer projectId = moduleinput.getModule_proj_id();
		return "redirect:/moduleDetailsByProjId?projectId=" + projectId;

	}

	// Method for getting modules using project id
	@RequestMapping(value = "/moduleDetailsByProjId", method = RequestMethod.GET)
	public String getModuleDetailsByProjId(@RequestParam("projectId") Integer projectId, Model model) {

		List<ModuleDTO> modules = moduleService.getModuleByProjId(projectId);

		model.addAttribute("moduleDTO", modules);
		logger.info("module details by projects");
		return "moduleDetailsbyProjId";
	}

}
