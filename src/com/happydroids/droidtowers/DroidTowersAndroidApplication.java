/*
 * Copyright (c) 2012. HappyDroids LLC, All rights reserved.
 */

package com.happydroids.droidtowers;

import static org.acra.ReportField.AVAILABLE_MEM_SIZE;
import static org.acra.ReportField.DISPLAY;
import static org.acra.ReportField.TOTAL_MEM_SIZE;
import static org.acra.ReportField.USER_COMMENT;
import static org.acra.ReportField.USER_EMAIL;

import org.acra.ACRA;
import org.acra.ACRAConfiguration;
import org.acra.ACRAConfigurationException;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

import com.happydroids.error.CrashReporterSave;

@ReportsCrashes(formKey = "")
public class DroidTowersAndroidApplication extends Application {
	@Override
	public void onCreate() {

		/*
		 * The following line triggers the initialization of ACRA ACRA is used
		 * to help with debug logs etc.
		 */

		ACRA.init(this);
		// ACRA.getErrorReporter().setReportSender(new RavenReportSender());
		ACRA.getErrorReporter().setReportSender(new CrashReporterSave(this));

		ACRAConfiguration conf = ACRA.getNewDefaultConfig(this);
		try {
			conf.setCustomReportContent(new ReportField[] { DISPLAY, USER_COMMENT, USER_EMAIL, TOTAL_MEM_SIZE, AVAILABLE_MEM_SIZE });
			conf.setResDialogCommentPrompt(R.string.crash_dialog_comment_prompt);
			conf.setResToastText(R.string.crash_toast_text);
			conf.setResDialogText(R.string.crash_dialog_text);
			conf.setResDialogEmailPrompt(R.string.crash_dialog_email_prompt);
			conf.setResDialogTitle(R.string.crash_dialog_title);
			conf.setResDialogOkToast(R.string.crash_dialog_ok_toast);
			conf.setMode(ReportingInteractionMode.DIALOG);
		} catch (ACRAConfigurationException e) {
			e.printStackTrace();
		}

		ACRA.setConfig(conf);

		// Have to call this to continue loading Android
		super.onCreate();
	}

}
