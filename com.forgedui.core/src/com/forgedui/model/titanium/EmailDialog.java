// LICENSE
package com.forgedui.model.titanium;

import com.forgedui.model.titanium.annotations.Unmapped;

/**
 * @author Dmitry {dmitry.grimm@gmail.com}
 * 
 */
public class EmailDialog extends TitaniumUIBaseElement implements Dialog {

	@Unmapped
	private static final long serialVersionUID = 1L;

	@Unmapped
	public static final String BAR_COLOR = "barColor";
	@Unmapped
	public static final String BCC_RECIPIENTS = "bccRecipients";
	@Unmapped
	public static final String CC_RECIPIENTS = "ccRecipients";
	@Unmapped
	public static final String MESSAGE_BODY = "messageBody";
	@Unmapped
	public static final String SUBJECT = "subject";
	@Unmapped
	public static final String TO_RECIPIENTS = "toRecipients";

	/**
	 * Bar color of the email dialog window, as a color name or hex triplet.
	 */
	private String barColor;

	/**
	 * Recipients of the email included via the BCC (Blind Carbon Copy) field.
	 */
	private String[] bccRecipients;

	/**
	 * Recipients of the email included via the CC (Carbon Copy) field.
	 */
	private String[] ccRecipients;

	/**
	 * Determines whether the email message, specifically the contents of
	 * messageBody, should be sent as HTML content type ...
	 */
	private Boolean html;

	/**
	 * Email message body.
	 */
	private String messageBody;

	/**
	 * Subject line for the email.
	 */
	private String subject;

	/**
	 * Recipients of the email included via the main TO field.
	 */
	private String[] toRecipients;

	public EmailDialog() {
		type = "Titanium.UI.EmailDialog";
	}

	public String getBarColor() {
		return barColor;
	}

	public void setBarColor(String barColor) {
		String old = this.barColor;
		this.barColor = barColor;
		listeners.firePropertyChange(BAR_COLOR, old, barColor);
	}

	public String[] getBccRecipients() {
		return bccRecipients;
	}

	public void setBccRecipients(String[] bccRecipients) {
		String[] old = this.bccRecipients;
		this.bccRecipients = bccRecipients;
		listeners.firePropertyChange(BCC_RECIPIENTS, old, bccRecipients);
	}

	public String[] getCcRecipients() {
		return ccRecipients;
	}

	public void setCcRecipients(String[] ccRecipients) {
		String[] old = this.ccRecipients;
		this.ccRecipients = ccRecipients;
		listeners.firePropertyChange(CC_RECIPIENTS, old, ccRecipients);
	}

	public Boolean getHtml() {
		return html;
	}

	public void setHtml(Boolean html) {
		this.html = html;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		String old = this.messageBody;
		this.messageBody = messageBody;
		listeners.firePropertyChange(MESSAGE_BODY, old, messageBody);
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		String old = this.subject;
		this.subject = subject;
		listeners.firePropertyChange(SUBJECT, old, subject);
	}

	public String[] getToRecipients() {
		return toRecipients;
	}

	public void setToRecipients(String[] toRecipients) {
		String[] old = this.toRecipients;
		this.toRecipients = toRecipients;
		listeners.firePropertyChange(TO_RECIPIENTS, old, toRecipients);
	}

	@Override
	protected boolean isColorFieldName(String fieldName) {
		if (BAR_COLOR.equals(fieldName)) {
			return true;
		} else {
			return super.isColorFieldName(fieldName);
		}
	}

}
