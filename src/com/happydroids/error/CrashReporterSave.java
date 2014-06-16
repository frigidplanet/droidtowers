package com.happydroids.error;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.acra.ACRA;
import org.acra.ACRAConstants;
import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

import android.content.Context;
import android.util.Log;

/**
 * A generic crashreport save function.
 * 
 * inspired by: http://stackoverflow.com/questions/8970810/acra-how-can-i-write-acra-report-to-file-in-sd-card
 * 
 * @author frigidplanet
 *
 */
public class CrashReporterSave implements ReportSender {
	
	private static final String TAG = CrashReporterSave.class.getSimpleName();
	private final Map<ReportField, String> mMapping = new HashMap<ReportField, String>() ;
	private FileOutputStream crashReport = null; 

	public CrashReporterSave(Context ctx) {
	    // the destination
	    try {
	        crashReport = ctx.openFileOutput("crashReport", Context.MODE_PRIVATE);
	    } catch (FileNotFoundException e) {
	        Log.e(TAG, "IO ERROR",e);
	    }
	}

	/* Original Project Code for quick reference
	 * 
	 * 
	 * public void send(CrashReportData data) throws ReportSenderException {
		RavenClient ravenClient = new RavenClient(HappyDroidConsts.SENTRY_DSN);
		JSONObject extra = new JSONObject();
		extra.put("app_version", data.getProperty(ReportField.APP_VERSION_CODE));
		extra.put("android_version",
				data.getProperty(ReportField.ANDROID_VERSION));
		extra.put("available_mem_size",
				data.getProperty(ReportField.AVAILABLE_MEM_SIZE));
		extra.put("total_mem_size",
				data.getProperty(ReportField.TOTAL_MEM_SIZE));
		extra.put("user_email", data.getProperty(ReportField.USER_EMAIL));
		extra.put("user_comment", data.getProperty(ReportField.USER_COMMENT));
		extra.put("display", data.getProperty(ReportField.DISPLAY));
		if (TowerGameService.hasBeenInitialised()) {
			extra.put("device_id", TowerGameService.instance().getDeviceId());
		}
		extra.put("device_build", data.getProperty(ReportField.BUILD));
		extra.put("device_brand", data.getProperty(ReportField.BRAND));
		extra.put("device_product", data.getProperty(ReportField.PRODUCT));
		extra.put("device_model", data.getProperty(ReportField.PHONE_MODEL));

		ravenClient.captureException(data.getProperty(ReportField.STACK_TRACE),
				RavenUtils.getTimestampLong(), "root", 50, null, null, extra);
		}
	 */
	@Override
	public void send(CrashReportData report) throws ReportSenderException {

	    final Map<String, String> finalReport = remap(report);

	    try {
	        OutputStreamWriter osw = new OutputStreamWriter(crashReport);

	        Set<Entry<String, String>> set = finalReport.entrySet();
	        Iterator<Entry<String, String>> i = set.iterator();

	        while (i.hasNext()) {
	            Map.Entry<String,String> me = (Entry<String, String>) i.next();
	            osw.write("[" + me.getKey() + "]=" + me.getValue());
	        }

	        osw.flush();
	        osw.close();
	    } catch (IOException e) {
	        Log.e(TAG, "IO ERROR",e);
	    }

	}

	private Map<String, String> remap(Map<ReportField, String> report) {

	    ReportField[] fields = ACRA.getConfig().customReportContent();
	    if (fields.length == 0) {
	        fields = ACRAConstants.DEFAULT_REPORT_FIELDS;
	    }

	    final Map<String, String> finalReport = new HashMap<String, String>(
	            report.size());
	    for (ReportField field : fields) {
	        if (mMapping == null || mMapping.get(field) == null) {
	            finalReport.put(field.toString(), report.get(field));
	        } else {
	            finalReport.put(mMapping.get(field), report.get(field));
	        }
	    }
	    return finalReport;
	}

}
