package PageObjectModel;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Menu {
	
	WebDriver driver;
	By allMenuItems = By.xpath("//div[@id='nav-xshop-container']//a");
	
	By mobiles = By.xpath("//div[@id='nav-xshop-container']//a[contains(text(), 'Mobiles')]");
	
	By bestSellers = By.xpath("//div[@id='nav-xshop-container']//a[contains(text(), 'Best Sellers')]");
	
	By fashion = By.xpath("//div[@id='nav-xshop-container']//a[contains(text(), 'Fashion')]");
	
	By BestTopSellerMobiles = By.xpath("//div[@data-cel-widget='osa_browse_banner_1']//a");
	String homepage = "https://www.amazon.in/";
	String electronics = "https://www.amazon.in/electronics/b/?ie=UTF8&node=976419031&ref_=nav_cs_electronics";
	By electronicsBrand = By.xpath("(//ul[@class='a-unordered-list a-nostyle a-vertical a-spacing-medium'])[5]//a");
	By desiredBrand1 = By.xpath("//span[contains(text(),'Redmi')]");
	By desiredBrand2 = By.xpath("//span[contains(text(),'Samsung')]");
	
	By Seller = By.xpath("(//ul[@class='a-unordered-list a-nostyle a-vertical a-spacing-medium'])[12]");
	By sellerName = By.xpath("//span[contains(text(),'Cart2India SLP')]");
	
	By Discount = By.xpath("//span[contains(text(), '50% Off or more')]//parent::a");
	
	//constructor
	public Menu(WebDriver driver) {
		this.driver = driver;

	}
	
	//for printing all the menu items on amazon homepage
	public void printMenuItems() throws InterruptedException {
		driver.navigate().to(homepage);
		Thread.sleep(3000);
		System.out.println("Printing all the menu items: \n");
		List<WebElement> menulist = driver.findElements(allMenuItems);
		for (WebElement mlist : menulist) {
			String listOfMenuItems = mlist.getAttribute("innerText");
			System.out.println(listOfMenuItems);
			
		}
		System.out.println("\n");
	}
	
	
//	open mobile from menu and get the list of best selling mobiles on amazon
	public void openMobile() {
		driver.findElement(mobiles).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='s-refinements']")));

		
		System.out.println("Best top selling phone models are: \n");
		List<WebElement> bestMobiles = driver.findElements(BestTopSellerMobiles);
		for (WebElement mb : bestMobiles) {
		String bestSellingMobiles = mb.getAttribute("aria-label");
		System.out.println(bestSellingMobiles);
		}
		System.out.println("\n");

	}
	
//	open bestsellers from menu and taking screenshot of the result page
	public void openBestSellers() throws IOException, InterruptedException {
		driver.navigate().to(homepage);
		driver.findElement(bestSellers).click();
		Thread.sleep(3000);
		System.out.println("Opening Sest Sellers and taking screenshot... ");
		TakesScreenshot scrshot =((TakesScreenshot)driver);
		File scr = scrshot.getScreenshotAs(OutputType.FILE);
		File DestFile=new File("C:\\Users\\aavadhoo\\Documents\\ScreenshotResult\\bestsellers.png");
		FileHandler.copy(scr, DestFile);
		System.out.println("Done. \n");
		Thread.sleep(2000);
		

	}

	
	//opening fashion page from menu and getting the title to validate the result page
	public void openFashion() {
		driver.navigate().to(homepage);
		driver.findElement(fashion).click();
		String title = driver.getTitle();
		System.out.println();
		System.out.println("Opening fashion page and printing title for validation... ");
		System.out.println(title);
		System.out.println("\n");

	}
	
	
	//searching electronics by clicking on checkbox - filtering by brand and seller name and getting list of brands and sellers for a product
	public void searchBySeller() throws InterruptedException {
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		driver.navigate().to(electronics);
		System.out.println("List of Brands: \n");
		
		List<WebElement> brands = driver.findElements(electronicsBrand);
		for (WebElement b : brands) {
		String brand = b.getAttribute("innerText");
		System.out.println(brand);
		}
		
		
		Actions action = new Actions(driver);
		action.moveToElement(driver.findElement(desiredBrand1));
		
		driver.findElement(desiredBrand1).click();
		action.moveToElement(driver.findElement(desiredBrand2));
		driver.findElement(desiredBrand2).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Cart2India SLP')]")));
		
		action.moveToElement(driver.findElement(Seller));
		Thread.sleep(2000);
		System.out.println("\nPrinting list of sellers... \n");
		List<WebElement> sellers = driver.findElements(Seller);
		for (WebElement b : sellers) {
		String Sellers = b.getAttribute("innerText");
		System.out.println(Sellers);
		
		driver.findElement(sellerName).click();
		Thread.sleep(1000);
		boolean validation = driver.getPageSource().contains("RESULTS");
		System.out.println("\n" +  validation +"\n");
		Thread.sleep(1000);
		System.out.println("Seller Verified");
		
		
		}
		

	}
	
	
	//filtering products by desired percentage of discount and validating the result page with screenshot
	public void filterByDiscount() throws InterruptedException, IOException {
		
		System.out.println();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get(electronics);
		System.out.println("Getting the discounts section.. ");
		
		Actions action = new Actions(driver);
		action.moveToElement(driver.findElement(Discount));
		Thread.sleep(2000);
		driver.findElement(Discount).click();
		
		boolean validation = driver.getPageSource().contains("RESULTS");
		System.out.println("\n" +  validation +"\n");
		Thread.sleep(1000);
		System.out.println("Discount Filter applied. Showing related Products... ");
		System.out.println();
		
		Thread.sleep(2000);
		TakesScreenshot scrshot =((TakesScreenshot)driver);
		File scr = scrshot.getScreenshotAs(OutputType.FILE);
		File DestFile=new File("C:\\Users\\aavadhoo\\Documents\\ScreenshotResult\\discount.png");
		FileHandler.copy(scr, DestFile);
		System.out.println("Done. \n");
		Thread.sleep(2000);
		
		driver.close();
		
		
		
		
	}

}
