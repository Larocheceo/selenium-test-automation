import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.TimeUnit;

import javax.swing.text.html.StyleSheet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

class HomepageTest {
	private WebDriver driver = null;

	@BeforeEach
	void beforeEachTestCase() {
		final boolean headless = false;
		final boolean chrome = true;
		if (chrome) {
			System.setProperty("webdriver.chrome.driver", "./lib/chromedriver.exe");
			if (headless) {
				ChromeOptions options = new ChromeOptions();
				options.setHeadless(true);
				driver = new ChromeDriver(options);
			} else {
				driver = new ChromeDriver();
				driver.manage().window().maximize();
			}
		} else {
			System.setProperty("webdriver.gecko.driver", "./lib/geckodriver.exe");
			if (headless) {
				FirefoxOptions options = new FirefoxOptions();
				options.setHeadless(true);
				driver = new FirefoxDriver(options);
			} else {
				driver = new FirefoxDriver();
				driver.manage().window().maximize();
			}
		}
	}

	@AfterEach
	void afterEachTestCase() {
		driver.quit();
		driver = null;
	}

	@Test
	void testTitel() {
		String projectDirectory = System.getProperty("user.dir");
		//String projectDirectory = "C:\\Users\\Ready2Go\\Desktop\\ISFATES\\Semastre5_Isfates\\Java\\Selenium-HelloWorld";
		String url = "file:///" + projectDirectory + "/www/index.html";
		driver.get(url);
		//driver.navigate().to(url);
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		assertEquals("Automatisches Test Webseite", driver.getTitle());
	}
}
