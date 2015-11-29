package com.forgedui.editor.analytics;

public interface ReportingService {

	public void reportEvent(ReportingEventTypes eventType, String[] data);

	public void reportError();
}