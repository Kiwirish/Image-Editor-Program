package cosc202.andie.controllers;

import cosc202.andie.actions.ColourActions;
import cosc202.andie.actions.EditActions;
import cosc202.andie.actions.FileActions;
import cosc202.andie.actions.FilterActions;
import cosc202.andie.actions.LanguageActions;
import cosc202.andie.actions.MacroActions;
import cosc202.andie.actions.ToolActions;
import cosc202.andie.actions.TransformActions;
import cosc202.andie.actions.ViewActions;
import cosc202.andie.models.AndieModel;

public class ActionsController {

	// private AndieModel model;
	// private AndieController controller;

	public ColourActions colourActions;
	public EditActions editActions;
	public FileActions fileActions;
	public FilterActions filterActions;
	public LanguageActions languageActions;
	public MacroActions macroActions;
	public ToolActions toolActions;
	public TransformActions transformActions;
	public ViewActions viewActions;

	public ActionsController(AndieModel model, AndieController controller) {
	}



}
