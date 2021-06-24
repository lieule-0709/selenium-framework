package pageObjects.Railway;

import common.constant.Constant;
import dataObjects.Tabs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class GeneralPage {
    //Locators
    private final By lbWelcome = By.xpath("//div[@class='account']//strong");
    private final By tabSelected = By.cssSelector(" #menu .selected");

    //Element
    protected WebElement getTabSelected() {
        return Constant.WEBDRIVER.findElement(tabSelected);
    }

    protected WebElement getLbWelcome(){
        return Constant.WEBDRIVER.findElement(lbWelcome);
    }

    //Methods
    public WebElement getTab(String name){
        return Constant.WEBDRIVER.findElement(By.xpath("//div[@id='menu']//a[.='" + name+ "']"));
    }

    public String getWelcomeMessage(){
        return this.getLbWelcome().getText();
    }

    public void navigateToLogoutPage(){
        this.getTab(Tabs.LOGOUT).click();
    }

    public String getTextOfSelectedTab(){
        return this.getTabSelected().getText();
    }
}
