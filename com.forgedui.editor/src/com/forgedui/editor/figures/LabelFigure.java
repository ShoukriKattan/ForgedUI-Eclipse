package com.forgedui.editor.figures;


/**
 * The label figure.
 * 
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class LabelFigure extends TitaniumTextFigure {
	
	private Alignments defaultAlignment;
	
	public LabelFigure(){
		setText("Label");
	}
	
	public void setDefaultTextHorisontalAlignment(Alignments defaultAlignment){
		this.defaultAlignment = defaultAlignment;
		setTextHorisontalAlign(defaultAlignment);
	}

	/**
	 * @param textAlign
	 */
	public void setTextAlign(com.forgedui.model.titanium.Label.Alignments textAlign) {
		if (textAlign != null) {
			switch (textAlign) {
			case TEXT_ALIGNMENT_CENTER:
				setTextHorisontalAlign(Alignments.center);
				break;
			case TEXT_ALIGNMENT_RIGHT:
				setTextHorisontalAlign(Alignments.right);
				break;
			case TEXT_ALIGNMENT_LEFT:
				setTextHorisontalAlign(Alignments.left);
			}
		} else {
			setTextHorisontalAlign(defaultAlignment);
		}
	}

}