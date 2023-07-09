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

	private final ModuleService moduleService;
	private final FunctionalUnitService Funitservice;

	@Autowired
	public ModuleController(ModuleService moduleService, FunctionalUnitService Funitservice) {
		this.moduleService = moduleService;
		this.Funitservice = Funitservice;
	}

	// Method to get create module JSP page
	@RequestMapping(value = "/createModule", method = RequestMethod.GET)
	public String createModule(Model model) {
		try {
			logger.info("Adding a new module");
			// Retrieve the functional units for populating the select dropdown
			List<FunctionalUnitDTO> functionalUnits = Funitservice.getAllFunctionalUnits();
			model.addAttribute("functionalUnits", functionalUnits);
			return "Addmodule";
		} catch (Exception e) {
			logger.error("Error occurred while getting create module page", e);
			model.addAttribute("error", "An error occurred while getting create module page.");
			return "errorPage";
		}
	}

	// Method for creating a module in a project
	@RequestMapping(value = "/createModulesuccess", method = RequestMethod.POST)
	public String createModuleSuccess(@Validated ModuleInput moduleInput, Model model) {
		try {
			// Create the module
			moduleService.createModule(moduleInput);
			logger.info("Module created successfully");
			Integer projectId = moduleInput.getModule_proj_id();
			return "redirect:/moduleDetailsByProjId?projectId=" + projectId;
		} catch (Exception e) {
			logger.error("Error occurred while creating module", e);
			model.addAttribute("error", "An error occurred while creating the module.");
			return "errorPage";
		}
	}

	// Method for getting module details by project ID
	@RequestMapping(value = "/moduleDetailsByProjId", method = RequestMethod.GET)
	public String getModuleDetailsByProjId(@RequestParam("projectId") Integer projectId, Model model) {
		try {
			// Get the modules for the specified project ID
			List<ModuleDTO> modules = moduleService.getModuleByProjId(projectId);
			model.addAttribute("moduleDTO", modules);
			logger.info("Module details loaded for project ID: " + projectId);
			return "moduleDetailsbyProjId";
		} catch (Exception e) {
			logger.error("Error occurred while getting module details by project ID", e);
			model.addAttribute("error", "An error occurred while getting module details by project ID.");
			return "errorPage";
		}
	}
}
