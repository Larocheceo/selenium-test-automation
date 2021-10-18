import static org.junit.jupiter.api.Assertions.*;


import java.util.concurrent.TimeUnit;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
	void testTitelIndexSeite() {
		String projectDirectory = System.getProperty("user.dir");
		String url = "file:///" + projectDirectory + "/www/index.html";
		driver.get(url);
		//driver.navigate().to(url);
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		assertEquals("Automatisches Test Webseite", driver.getTitle());
	}

	@Test
	void testTitelPage1() {
		String projectDirectory = System.getProperty("user.dir");
		String url = "file:///" + projectDirectory + "/www/page1.html";
		driver.get(url);
		//driver.navigate().to(url);
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
		assertEquals("Persoenliche Informationen", driver.getTitle());
	}

	@Test
	void testBestaetigenDruck() {
		String projectDirectory = System.getProperty("user.dir");
		String url = "file:///" + projectDirectory + "/www/index.html";
		driver.get(url);
		// Werte fuer 'required' Felder hinzufuegen
		WebElement name = driver.findElement(By.className("user-name"));
		name.sendKeys("Rochella Djouakeu Vofo");

		WebElement element = driver.findElement(By.id("submit"));
		element.click();
		try {
			WebElement el = new WebDriverWait(driver, 10).until(
					ExpectedConditions.presenceOfElementLocated(By.id("header-text")));

		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		// Die Seite "Page1" soll angezeigt werden, falls auf dem Button gedruckt wird.
		assertEquals("Persoenliche Informationen", driver.getTitle());
	}
}
