package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {
	private static ExtentReports extent;
	private static final String REPORT_LOCATION = "target/extent-report.html";

	/**
	 * Create ExtentReports and attach one or many reporter(s) reports.
	 */
	public static ExtentReports getExtentReportsInstance() {
		if (extent == null) {
			extent = new ExtentReports();
			ExtentSparkReporter htmlReporter = new ExtentSparkReporter(REPORT_LOCATION);
			extent.attachReporter(htmlReporter);
		}

		return extent;
	}

}
