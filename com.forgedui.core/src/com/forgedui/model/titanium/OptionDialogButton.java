// LICENSE
package com.forgedui.model.titanium;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 *
 */
public class OptionDialogButton extends TitaniumUIBaseElement {
	
	private static final long serialVersionUID = 1L;

	public static final String TITLE_PROP = "OptionDialogButton.title";
	
	private String title;
	
	@Override
	public void setName(String name) {
		String oldName = getName();
		super.setName(name);
		if (oldName == null){
			setTitle(name);
		}
	}
	
	@Override
	protected String getBaseName() {
		return "Option";
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		String oldTitle = this.title;
		this.title = title;
		listeners.firePropertyChange(TITLE_PROP, oldTitle, title);
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

}
