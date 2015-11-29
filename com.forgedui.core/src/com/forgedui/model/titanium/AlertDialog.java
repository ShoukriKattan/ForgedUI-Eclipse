// LICENSE
package com.forgedui.model.titanium;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.forgedui.model.property.MinmaxValidator;
import com.forgedui.model.property.NumberCellEditor;
import com.forgedui.model.property.NumberPropertyDescriptor;
import com.forgedui.model.titanium.annotations.SupportedPlatform;
import com.forgedui.model.titanium.annotations.Unmapped;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 * 
 */
public class AlertDialog extends TitaniumUIContainer implements Dialog {

	@Unmapped
	private static final long serialVersionUID = 1L;

	@Unmapped
	public static final String TITLE_PROP = "title";

	@Unmapped
	public static final String TITLE_ID_PROP = "titleid";

	@Unmapped
	public static final String OK_PROP = "ok";

	@Unmapped
	public static final String OK_ID_PROP = "okid";

	@Unmapped
	public static final String MESSAGE_ID_PROP = "messageid";

	@Unmapped
	public static final String MESSAGE_PROP = "message";

	@Unmapped
	public static final String CANCEL_PROP = "cancel";

	private String title, titleid;

	private String message, messageId;

	private Integer cancel;

	private String ok;

	public AlertDialog() {

		type = "Titanium.UI.AlertDialog";
	}

	@SupportedPlatform(platforms = { "iphone", "ipad", "mobile" })
	private String okid;

	@Override
	public void setName(String name) {
		String oldName = getName();
		super.setName(name);
		if (oldName == null) {
			setTitle("Put your message here");
			setMessage("Put you explanation here if necessary");
		}
	}

	private void setDialogOptionsNumber(int num) {
		for (int i = children.size() - 1; i >= num; i--) {
			removeChild(children.get(i));
		}
		for (int i = children.size(); children.size() < num; i++) {
			addChild(new AlertDialogButton());
		}
	}

	/**
	 * @param title
	 *            the title to set
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

	/**
	 * @param explanation
	 *            the explanation to set
	 */
	public void setMessage(String explanation) {
		String oldValue = this.message;
		this.message = explanation;
		listeners.firePropertyChange(MESSAGE_PROP, oldValue, explanation);
	}

	/**
	 * @return the explanation
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param messageId
	 *            the messageId to set
	 */
	public void setMessageId(String messageId) {
		String old = this.messageId;
		this.messageId = messageId;
		listeners.firePropertyChange(MESSAGE_ID_PROP, old, messageId);
	}

	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return messageId;
	}

	public void setCancel(Integer cancel) {
		Integer old = this.cancel;
		this.cancel = cancel;
		listeners.firePropertyChange(CANCEL_PROP, old, cancel);
	}

	public Integer getCancel() {
		return cancel;
	}

	public void setTitleid(String titleid) {
		String old = this.titleid;
		this.titleid = titleid;
		listeners.firePropertyChange(TITLE_ID_PROP, old, titleid);
	}

	public String getTitleid() {
		return titleid;
	}

	public void setOk(String ok) {
		String old = this.ok;
		this.ok = ok;
		listeners.firePropertyChange(OK_PROP, old, ok);
	}

	public String getOk() {
		return ok;
	}

	public void setOkid(String okid) {
		String old = this.okid;
		this.okid = okid;
		listeners.firePropertyChange(OK_ID_PROP, old, okid);
	}

	public String getOkid() {
		return okid;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> props = new ArrayList<IPropertyDescriptor>();
		TextPropertyDescriptor nameDescr = new TextPropertyDescriptor(PROPERTY_NAME, PROPERTY_NAME);
		nameDescr.setValidator(getDiagram().getUniqueNameValidator(this));
		props.add(nameDescr);
		props.add(new TextPropertyDescriptor(TITLE_PROP, TITLE_PROP));
		props.add(new TextPropertyDescriptor(TITLE_ID_PROP, TITLE_ID_PROP));
		props.add(new TextPropertyDescriptor(OK_PROP, OK_PROP));
		if (!getPlatform().isAndroid()) {
			props.add(new TextPropertyDescriptor(OK_ID_PROP, OK_ID_PROP));
		}
		props.add(new TextPropertyDescriptor(MESSAGE_PROP, MESSAGE_PROP));
		props.add(new TextPropertyDescriptor(MESSAGE_ID_PROP, MESSAGE_ID_PROP));

		NumberPropertyDescriptor npd = new NumberPropertyDescriptor(PROPERTY_CHILDREN, "optionsCount",
				NumberCellEditor.INTEGER, true);
		// set Maximum number of buttons to 3 under Android platform
		int maxValue = Platform.Android.equals(getPlatform()) ? 3 : 30;
		npd.setValidator(new MinmaxValidator(1, maxValue));
		props.add(npd);

		npd = new NumberPropertyDescriptor(CANCEL_PROP, CANCEL_PROP, NumberCellEditor.INTEGER, true);
		npd.setValidator(new MinmaxValidator(0, MinmaxValidator.MIN_ONLY));
		props.add(npd);

		return props.toArray(new IPropertyDescriptor[props.size()]);
	}

	@Override
	public Object getPropertyValue(Object propertyId) {
		if (PROPERTY_CHILDREN.equals(propertyId)) {
			int count = getChildren().size();
			return count > 0 ? count : null;
		} else if (CANCEL_PROP.equals(propertyId)) {
			return getCancel() != null ? getCancel() : "";
		} else if (OK_PROP.equals(propertyId)) {
			return getOk() != null ? getOk() : "";
		} else if (OK_ID_PROP.equals(propertyId)) {
			return getOkid() != null ? getOkid() : "";
		} else if (MESSAGE_PROP.equals(propertyId)) {
			return getMessage() != null ? getMessage() : "";
		} else if (MESSAGE_ID_PROP.equals(propertyId)) {
			return getMessageId() != null ? getMessageId() : "";
		} else if (TITLE_PROP.equals(propertyId)) {
			return getTitle() != null ? getTitle() : "";
		} else if (TITLE_ID_PROP.equals(propertyId)) {
			return getTitleid() != null ? getTitleid() : "";
		}
		return super.getPropertyValue(propertyId);
	}

	public OptionDialogButton getOptionDialogButton(Integer i) {
		if (i != null && getChildren().size() > i) {
			return (OptionDialogButton) getChildren().get(i);
		}
		return null;
	}

	@Override
	public void setPropertyValue(Object propertyId, Object value) {
		if ("".equals(value)) {
			value = null;
		} else if (value instanceof Integer && (Integer) value < 0) {
			value = null;
		}
		if (PROPERTY_CHILDREN.equals(propertyId)) {
			if (value == null) {
				value = 0;
			}
			setDialogOptionsNumber((Integer) value);
		} else if (CANCEL_PROP.equals(propertyId)) {
			setCancel((Integer) value);
		} else if (OK_PROP.equals(propertyId)) {
			setOk((String) value);
		} else if (OK_ID_PROP.equals(propertyId)) {
			setOkid((String) value);
		} else if (MESSAGE_PROP.equals(propertyId)) {
			setMessage((String) value);
		} else if (MESSAGE_ID_PROP.equals(propertyId)) {
			setMessageId((String) value);
		} else if (TITLE_PROP.equals(propertyId)) {
			setTitle((String) value);
		} else if (TITLE_ID_PROP.equals(propertyId)) {
			setTitleid((String) value);
		} else
			super.setPropertyValue(propertyId, value);
	}

}