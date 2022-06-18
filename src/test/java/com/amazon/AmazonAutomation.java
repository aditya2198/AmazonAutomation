package com.amazon;

import java.io.IOException;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;

import PageObjectModel.Menu;
import PageObjectModel.Products;

public class AmazonAutomation {
//	WebDriver driver;

	public static void main(String[] args) throws InterruptedException, IOException {
		
		System.setProperty("webdriver.chrome.driver","C:\\Users\\aavadhoo\\selenium\\chromedriver.exe");		 
	    WebDriver driver= new ChromeDriver();
	    driver.get("https://www.amazon.in/");
	    driver.manage().window().maximize();
	    
	    Products p = new Products(driver);
	    p.searchProduct();
	    p.clickProductsFromSearch();
	    p.productCategory();
	    p.storeProducts();
	    p.validateProduct();
	    
	    Menu m =new Menu(driver);
	    m.printMenuItems();
	    m.openMobile();
	    m.openBestSellers();
	    m.openFashion();
	    m.searchBySeller();
	    m.filterByDiscount();
	   
	    
		}

	
    }


