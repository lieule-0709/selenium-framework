package testCases.Railway;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import common.constant.Constant;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class BaseTest {
    protected ExtentTest logger;
    protected String pathFile = System.getProperty("user.dir") + "/src/main/java/testCases/Railway/data.json";

    @Parameters("browserName")
    @BeforeSuite
    public void beforeSuite(String browserName) {
        switch (browserName) {
            case "chrome":
                Constant.REPORT = new ExtentReports(System.getProperty("user.dir") + "\\ExtentReportResults_Chrome.html");
                break;
            case "firefox":
                Constant.REPORT = new ExtentReports(System.getProperty("user.dir") + "\\ExtentReportResults_Firefox.html");
                break;
            case "edge":
                Constant.REPORT = new ExtentReports(System.getProperty("user.dir") + "\\ExtentReportResults_Edge.html");
                break;
            default:
                Constant.REPORT = new ExtentReports(System.getProperty("user.dir") + "\\ExtentReportResults.html");
        }
        Constant.REPORT.loadConfig(new File(System.getProperty("user.dir") + "\\extent-config.xml"));
    }

    @Parameters("browserName")
    @BeforeClass
    public void beforeClass(String browserName) {
        switch (browserName) {
            case "chrome":
                ChromeOptions ChOptions = new ChromeOptions();
                ChOptions.addArguments("--headless");
                WebDriverManager.chromedriver().setup();
                Constant.WEBDRIVER = new ChromeDriver(ChOptions);
                break;
            case "firefox":
                FirefoxOptions FFOptions = new FirefoxOptions();
                FFOptions.setHeadless(true);
                WebDriverManager.firefoxdriver().setup();
                Constant.WEBDRIVER = new FirefoxDriver(FFOptions);
                break;
            case "edge":
                //Not support to run headless
                WebDriverManager.edgedriver().setup();
                Constant.WEBDRIVER = new EdgeDriver();
                break;
            default:
                WebDriverManager.chromedriver().setup();
                Constant.WEBDRIVER = new ChromeDriver();
        }
        Constant.WEBDRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        Constant.WEBDRIVER.manage().window().maximize();
    }

    @DataProvider(name = "data")
    public Object[][] getData(Method method) {
        JSONParser parser = new org.json.simple.parser.JSONParser();
        Map<String, JSONObject> dataMap = new HashMap<String, JSONObject>();
        Iterator entriesIterator = null;

        try {
            JSONObject contentObj = (JSONObject) parser.parse(new FileReader(new File(pathFile)));
            JSONObject dataObj = (JSONObject) contentObj.get(method.getName());

            dataObj.keySet().forEach(key -> {
                dataMap.put(String.valueOf(key), (JSONObject) dataObj.get(key));
            });

            Set entries = dataObj.entrySet();
            entriesIterator = entries.iterator();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Object[][] arr = new Object[dataMap.size()][1];
        int i = 0;
        while (entriesIterator.hasNext()) {
            Map.Entry mapping = (Map.Entry) entriesIterator.next();
            arr[i][0] = mapping.getValue();
            i++;
        }
        return arr;
    }

    @AfterMethod
    public void getResult(ITestResult result) {
        if (result.getStatus() == ITestResult.SUCCESS) {
            logger.log(LogStatus.PASS, "PASSED", "Test Case " + result.getName() + " is Passed");
        } else if (result.getStatus() == ITestResult.FAILURE) {
            logger.log(LogStatus.FAIL, "FAILED", "Test Case " + result.getName() + " is Failed");
            logger.log(LogStatus.FAIL, "Reason for fail", result.getThrowable().getMessage());
            String base64Screenshot = "data:image/png;base64," + ((TakesScreenshot) Constant.WEBDRIVER).getScreenshotAs(OutputType.BASE64);
            logger.log(LogStatus.INFO, "Snapshot below: " + logger.addBase64ScreenShot(base64Screenshot));
        } else if (result.getStatus() == ITestResult.SKIP) {
            logger.log(LogStatus.SKIP, "SKIPPED", "Test Case " + result.getName() + " is Skipped");
            logger.log(LogStatus.SKIP, "Reason for skip", result.getThrowable().getMessage());
        }
        Constant.REPORT.endTest(logger);
    }

    @AfterClass
    public void afterClass() {
        Constant.WEBDRIVER.quit();
    }

    @AfterSuite
    public void afterSuite() {
        Constant.REPORT.flush();
        Constant.REPORT.close();
    }
}
