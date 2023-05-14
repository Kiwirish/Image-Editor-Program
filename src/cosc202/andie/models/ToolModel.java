package cosc202.andie.models;

import java.awt.Color;

import cosc202.andie.tools.Tool;

public class ToolModel {
	private AndieModel model;
	private Tool tool;

	private Color fillColor;
	private Color strokeColor;
	private int strokeWidth;
	


	public ToolModel(AndieModel model) {
		this.model = model;
		this.tool = null;
		this.strokeColor = Color.BLACK;
		this.fillColor = Color.WHITE;
		this.strokeWidth = 12;
	}

	public Tool getTool() {
		return tool;
	}

	public void setTool(Tool tool) {
		if (this.tool != null) this.tool.deactivateTool();
		this.tool = tool;
		tool.activateTool();
	}

	public void unsetTool() {
		tool.deactivateTool();
		tool = null;
	}

	public Color getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor(Color strokeColor) {
		this.strokeColor = strokeColor;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public int getStrokeWidth() {
		return strokeWidth;
	}

	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

}
