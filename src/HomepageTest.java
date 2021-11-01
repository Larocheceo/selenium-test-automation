import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

class HomepageTest {
	private WebDriver driver = null;

	@BeforeEach
	void beforeEachTestCase() {
		final boolean headless = false;
		final boolean chrome = true;
		/**if (chrome) {
			System.setProperty("webdriver.chrome.driver", "./lib/chromedriver.exe");
			if (headless) {
				ChromeOptions options = new ChromeOptions();
				options.setHeadless(true);
				driver = new ChromeDriver(options);
			} else {
				driver = new ChromeDriver();
				driver.manage().window().maximize();
			}
		}else {*/
		System.setProperty("webdriver.gecko.driver", "./lib/geckodriver.exe");
			if (headless) {
				FirefoxOptions options = new FirefoxOptions();
				options.setHeadless(true);
				driver = new FirefoxDriver(options);
			} else {
				driver = new FirefoxDriver();
				driver.manage().window().maximize();
			}
		//}
	}

	@AfterEach
	void afterEachTestCase() {
		driver.quit();
		driver = null;
	}

	@Test
	void test_titel_index_seite() {
		String projectDirectory = System.getProperty("user.dir");
		String url = "file:///" + projectDirectory + "/www/index.html";
		driver.get(url);
		//driver.navigate().to(url);
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		assertEquals("Automatisches Test Webseite", driver.getTitle());
	}

	@Test
	void prompt_muss_angezeigt_werden_wenn_man_auf_page1_ist() {
		String projectDirectory = System.getProperty("user.dir");
		String url = "file:///" + projectDirectory + "/www/page1.html";
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		assertEquals("Eine kurze Frage. Haben Sie einen Spitznamen? Geben Sie es ein: ", driver.switchTo().alert().getText());
	}

	@Test
	void pruefen_das_der_command_promt_weg_geht_wenn_mann_auf_abbrechen_klickt(){
		String projectDirectory = System.getProperty("user.dir");
		String url = "file:///" + projectDirectory + "/www/page1.html";
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.switchTo().alert().dismiss();
		Assertions.assertThrows(NoAlertPresentException.class, () ->{
			driver.switchTo().alert().getText();
		});
	}

	@Test
	void test_titel_page1() {
		String projectDirectory = System.getProperty("user.dir");
		String url = "file:///" + projectDirectory + "/www/page1.html";
		driver.get(url);

		//driver.navigate().to(url);
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		// Dismiss the prompt command that appears
		driver.switchTo().alert().dismiss();
		assertEquals("Persoenliche Informationen", driver.getTitle());
	}

	@Test
	void nickname_muss_angezeigt_werden_wenn_man_es_im_promt_eingibt() {
		String projectDirectory = System.getProperty("user.dir");
		String url = "file:///" + projectDirectory + "/www/page1.html";
		driver.get(url);
		driver.switchTo().alert().sendKeys("MY NICKNAME");
		driver.switchTo().alert().accept();
		assertEquals("MY NICKNAME", driver.findElement(By.id("nickname")).getText());

	}

	@Test
	void kein_nickname_soll_angezeigt_werden_wenn_kein_nickname_gegeben_wurde() {
		String projectDirectory = System.getProperty("user.dir");
		String url = "file:///" + projectDirectory + "/www/page1.html";
		driver.get(url);
		driver.switchTo().alert().accept();
		assertEquals("", driver.findElement(By.id("nickname")).getText());
	}

	@Test
	void gut_ausgefuelltes_formular_versteckt_das_formular_nach_druck_auf_check_button() {
		String projectDirectory = System.getProperty("user.dir");
		String url = "file:///" + projectDirectory + "/www/index.html";
		driver.get(url);

		// Werte fuer 'required' Felder
		driver.findElement(By.id("mann")).click();
		driver.findElement(By.className("user-name")).sendKeys("Rochella Djouakeu Vofo");
		driver.findElement(By.id("liste-laender")).sendKeys("Frankreich");
		driver.findElement(By.id("submit")).click();
		WebElement element= driver.findElement(By.tagName("section"));
		element.getAttribute("class");

		// Die Seite "Page1" soll angezeigt werden, falls auf dem Button gedruckt wird.
		assertEquals("hide", element.getAttribute("class"));
	}

	@Test
	void formular_soll_nicht_gesendet_werden_falls_keine_name_besteht() {
		String projectDirectory = System.getProperty("user.dir");
		String url = "file:///" + projectDirectory + "/www/index.html";
		driver.get(url);

		// Formular ausfuellen ohne Name
		driver.findElement(By.id("weiblich")).click();
		driver.findElement(By.className("user-vorname")).sendKeys("Djouakeu");
		driver.findElement(By.id("geburtsdatum")).sendKeys("22/10/2022");
		driver.findElement(By.id("liste-laender")).sendKeys("Frankreich");

		// Wir schicken das Formular und warten auf das Promt, um zu signalisieren, dass der Name fehlt.
		(driver.findElement(By.id("submit"))).click();
		Alert alert =new WebDriverWait(driver,10 ).until(ExpectedConditions.alertIsPresent());

		// Wir prüfen, dass wir auf die gleiche Webseite bleiben, da das Formular nicht gut aufgeffült ist.
		assertEquals("Der Name muss aus mindestens 2 Buchstaben bestehen!", alert.getText());
	}

	@Test
	void das_land_muss_mit_den_vorgeschlagenen_laender_in_der_liste_stimmen() {
		String projectDirectory = System.getProperty("user.dir");
		String url = "file:///" + projectDirectory + "/www/index.html";
		driver.get(url);

		// Alle required Felder ausfuellen
		driver.findElement(By.id("liste-laender")).sendKeys("Kamerun");
		driver.findElement(By.className("user-name")).sendKeys("Djouakeu");
		driver.findElement(By.id("weiblich")).click();
		driver.findElement(By.id("submit")).click();

		// Prompt muss angezeigt werden, wenn falsches Lander eingegeben wird
		Alert alert = new WebDriverWait(driver,10 ).until(ExpectedConditions.alertIsPresent());
		assertEquals("Waehlen Sie ein Land aus der Liste aus!", alert.getText());
	}

	@Test
	void rosa_background_farbe_wenn_der_user_eine_Frau_ist() {
		String projectDirectory = System.getProperty("user.dir");
		String url = "file:///" + projectDirectory + "/www/index.html";
		driver.get(url);

		// formular aufuellen und schicken
		driver.findElement(By.id("weiblich")).click();
		driver.findElement(By.className("user-name")).sendKeys("Djouakeu");
		driver.findElement(By.id("liste-laender")).sendKeys("China");
		(driver.findElement(By.id("submit"))).click();

		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		String color = driver.findElement(By.cssSelector("body")).getCssValue("background-color");
		assertEquals("rgb(188, 143, 143)", color);
	}

	@Test
	void gruen_background_farbe_wenn_der_user_ein_mann_ist() {
		String projectDirectory = System.getProperty("user.dir");
		String url = "file:///" + projectDirectory + "/www/index.html";
		driver.get(url);

		// formular aufuellen und schicken
		driver.findElement(By.id("mann")).click();
		driver.findElement(By.className("user-name")).sendKeys("Yepmo");
		driver.findElement(By.id("liste-laender")).sendKeys("China");
		(driver.findElement(By.id("submit"))).click();

		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		String color = driver.findElement(By.cssSelector("body")).getCssValue("background-color");
		assertEquals("rgb(46, 139, 87)", color);
	}

	@Test
	void rote_farbe_nach_hover_auf_der_check_button() {
		String projectDirectory = System.getProperty("user.dir");
		String url = "file:///" + projectDirectory + "/www/index.html";
		driver.get(url);

		// Zum button navigieren
		Actions actions = new Actions(driver);
		WebElement target = driver.findElement(By.id("submit"));
		actions.moveToElement(target).perform();

		// Pruefen, dass die Farbe jetzt Rot ist.
		assertEquals("rgb(255, 0, 0)", target.getCssValue("background-color"));
	}

	@Test
	void nach_click_auf_zurueck_kehren_landet_man_auf_homepage_seite() {
		String projectDirectory = System.getProperty("user.dir");
		String url = "file:///" + projectDirectory + "/www/page1.html";
		driver.get(url);
		// send key to promt and dismmiss it
		driver.switchTo().alert().sendKeys("Roche");
		driver.switchTo().alert().accept();

		driver.findElement(By.tagName("a")).click();
		// Ckeck that we have switched to the next page
		assertEquals("Automatisches Test Webseite", driver.getTitle());

	}

	@Test
	void pruefen_dass_die_anrede_entprischt_dem_gender_frau() {
		String projectDirectory = System.getProperty("user.dir");
		String url = "file:///" + projectDirectory + "/www/index.html";
		driver.get(url);

		driver.findElement(By.id("weiblich")).click();
		driver.findElement(By.className("user-name")).sendKeys("Vofo");
		driver.findElement(By.id("liste-laender")).sendKeys("China");
		(driver.findElement(By.id("submit"))).click();

		String content = driver.findElement(By.tagName("h2")).getText();
		assertTrue(content.contains("Frau"));
	}

	@Test
	void pruefen_dass_die_anrede_entprischt_dem_gender_mann() {
		String projectDirectory = System.getProperty("user.dir");
		String url = "file:///" + projectDirectory + "/www/index.html";
		driver.get(url);

		driver.findElement(By.id("mann")).click();
		driver.findElement(By.className("user-name")).sendKeys("Brok");
		driver.findElement(By.id("liste-laender")).sendKeys("Moroco");
		(driver.findElement(By.id("submit"))).click();

		String content = driver.findElement(By.tagName("h2")).getText();
		assertFalse(content.contains("Frau"));
	}

	@Test
	void falsche_ausfuellen_des_formulars_und_fehler_behebung() {
		String projectDirectory = System.getProperty("user.dir");
		String url = "file:///" + projectDirectory + "/www/index.html";
		driver.get(url);
		// Formular falsch ausfuellen: kein Gender, kein Name

		driver.findElement(By.className("user-name")).sendKeys("");
		driver.findElement(By.className("user-name")).sendKeys("Rochella");
		driver.findElement(By.id("liste-laender")).sendKeys("Deutschland");
		(driver.findElement(By.id("submit"))).click();

		// Alle alert catchen
		try {
			while(true){
				driver.switchTo().alert().dismiss();
			}
		} catch (NoAlertPresentException e){
			e.getMessage();
		}
		// Formular diesmal gut ausfuellen
		driver.findElement(By.id("mann")).click();
		driver.findElement(By.className("user-name")).sendKeys("Djouakeu");

		// Pruefen, dass das formular diesmal gesendet wird
		(driver.findElement(By.id("submit"))).click();
		String content = driver.findElement(By.tagName("h2")).getText();
		assertTrue(content.contains("Herr"));
	}
}
