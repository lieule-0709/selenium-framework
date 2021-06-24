package common.utilities;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class Utilities {
    public static void selectOption(WebElement selectBox, String option){
        Select dropdown = new Select(selectBox);
        dropdown.selectByVisibleText(option);
    }
}
