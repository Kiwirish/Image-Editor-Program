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

/**
 * <p>
 * The controller for all of ANDIE's actions
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">cc by-nc-sa 4.0</a>
 * </p>
 * 
 * @see AndieController
 * @author Jeb Nicholson
 * @version 1.0
 */
public class ActionsController {

	// private AndieModel model;
	// private AndieController controller;

	/** Andie's Color-related actions */
	public ColourActions colourActions;
	/** Andie's Edit-related actions */
	public EditActions editActions;
	/** Andie's File-related actions */
	public FileActions fileActions;
	/** Andie's Filter-related actions */
	public FilterActions filterActions;
	/** Andie's Language-related actions */
	public LanguageActions languageActions;
	/** Andie's Macro-related actions */
	public MacroActions macroActions;
	/** Andie's Tool-related actions */
	public ToolActions toolActions;
	/** Andie's Transform-related actions */
	public TransformActions transformActions;
	/** Andie's View-related actions */
	public ViewActions viewActions;

	/**
	 * Create a new ActionsController
	 * @param model The base model
	 * @param controller The base controller
	 */
	public ActionsController(AndieModel model, AndieController controller) {}

}
