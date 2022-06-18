package PageObjectModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.Select;

import com.amazon.ValidationException;



public class Products {

	WebDriver driver;

	By category = By.id("searchDropdownBox");
	By searchField = By.xpath("//input[@id='twotabsearchtextbox']");
	By searchButton = By.id("nav-search-submit-button");
	By suggestions = By.xpath("(//div[@class='autocomplete-results-container']//child::div//child::div//div)[1]");
	By firstSponsoredProduct = By.xpath("(//div[@class='a-section a-spacing-small a-spacing-top-small'])[2]//h2//a");
	By suggestedResult = By.xpath("(//div[@class='sg-col-inner'])[9]");
	By bestSellers = By.xpath("//li[@id='sobe_d_b_4_1']//a");
	By scrollBar = By.xpath("(//div[@class='sl-sobe-carousel-scroller'])[1]");

	public Products(WebDriver driver) {
		this.driver = driver;

	}

	//Handling search field and clicking on first suggestion
	public void searchProduct() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(searchField).sendKeys("OnePlus Nord");
		Thread.sleep(1000);
		driver.findElement(suggestions).click();
		Thread.sleep(2000);

		WebElement element = driver.findElement(By.xpath("(//div[@class='sg-col-inner'])[9]"));
		Actions action = new Actions(driver);
		action.moveToElement(element);
		Thread.sleep(2000);

		String suggestionResult = driver.findElement(suggestedResult).getText();
		System.out.println(suggestionResult);



	}


	//STORING LIST OF PRODUCTS IN DATABASE
	public void storeProducts() throws InterruptedException {

		Thread.sleep(2000);
		driver.navigate().to("https://www.amazon.in/s?k=samsung&crid=312WCZUOMITIT&sprefix=samsung%2Caps%2C178&ref=nb_sb_noss_1");
		Thread.sleep(2000);

		try{  

			//step1 load the driver class  
			Class.forName("oracle.jdbc.driver.OracleDriver");  

			//step2 create  the connection object  
			Connection con=DriverManager.getConnection(  
					"jdbc:oracle:thin:@localhost:1521:xe","system","AdiPu21*");  
			con.setAutoCommit(false);
			//step3 create the statement object  
			String text1 = "insert into validateProduct (PhoneDetails)"
					+ " VALUES(?)";
			PreparedStatement statement = con.prepareStatement(text1); 

			List<WebElement> list = driver.findElements(By.xpath("//div[@class='a-section a-spacing-small a-spacing-top-small']"));
			for (WebElement wb : list) {
				String text = wb.getText();
				statement.setString(1, text);
				statement.executeUpdate();

			}
			con.commit();

			Thread.sleep(3000);
			con.close();  
			System.out.println("completed");

		}
		catch(Exception e){ 
			System.out.println(e);
		}  


	}  


	//HANDLING SEARCH RESULTS PAGE
	public void clickProductsFromSearch() throws InterruptedException, IOException {
		driver.get("https://www.amazon.in/");
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		driver.findElement(searchField).sendKeys("OnePlus 9 RT");
		driver.findElement(searchButton).click();

		JavascriptExecutor js = (JavascriptExecutor)driver;				
		js.executeScript("window.scrollBy(0,200)");

		Thread.sleep(2000);

		driver.findElement(firstSponsoredProduct).click();

		//to switch to new window for taking screenshot
		String parentHandle = driver.getWindowHandle();

		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle); 
		}

		//taking screenshot of new window
		TakesScreenshot scrShot =((TakesScreenshot)driver);
		File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
		File DestFile=new File("C:\\Users\\aavadhoo\\Documents\\ScreenshotResult\\sponsoredproduct.png");
		FileHandler.copy(SrcFile, DestFile);
		Thread.sleep(2000);
		driver.close();
		driver.switchTo().window(parentHandle);

	}


	//HANDLING DROPDOWN ON HOMEPAGE
	public void productCategory() throws InterruptedException, IOException {

		driver.get("https://www.amazon.in/");
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.findElement(category).click();
		Thread.sleep(3000);

		List<WebElement> list = driver.findElements(By.xpath("//select[@id='searchDropdownBox']//child::option"));
		for (WebElement wb : list) {
			System.out.println(wb.getText());

		}
		Thread.sleep(3000);
		Select select = new Select(driver.findElement(category));
		select.selectByVisibleText("Books");


		driver.findElement(searchButton).click();

		JavascriptExecutor js = (JavascriptExecutor)driver;				
		js.executeScript("window.scrollBy(0,300)");
		Thread.sleep(1000);

		Actions actions = new Actions(driver);
		actions.moveToElement(driver.findElement(bestSellers));
		actions.perform();

		Thread.sleep(2000);
		driver.findElement(bestSellers).click();

		TakesScreenshot scrShot =((TakesScreenshot)driver);
		File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
		File DestFile=new File("C:\\Users\\aavadhoo\\Documents\\ScreenshotResult\\books.png");
		FileHandler.copy(SrcFile, DestFile);
		Thread.sleep(2000);
	}
	

	//validating the stored product list by using select statement to check presence of the product in database
	public void validateProduct() {
		try{  
			//step1 load the driver class  
			Class.forName("oracle.jdbc.driver.OracleDriver");  

			//step2 create  the connection object  
			Connection con=DriverManager.getConnection(  
					"jdbc:oracle:thin:@localhost:1521:xe","system","AdiPu21*");  
			
			con.setAutoCommit(false);
			
			
			//step3 create the statement object  
			Statement stmt=con.createStatement();  


			//step4 execute query
			ResultSet rs=stmt.executeQuery("select * from validateProduct where PhoneDetails like '%M12%'"); 

			if (!rs.next() ) {
				throw new ValidationException();
			} 

			else {
				while(rs.next())  
					System.out.println("  \n"+rs.getString(1)+"  \n");  

			}

			//Truncating table demo for reusing it for next search result
			String query = "Truncate table validateProduct";
			stmt.execute(query);

			//step5 close the connection object  
			con.close();  



		}catch(Exception e){ 
			System.err.println("No Products matching to the input");
		} 

	} 


}

	

