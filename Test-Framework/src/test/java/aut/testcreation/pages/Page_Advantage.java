package aut.testcreation.pages;

import org.openqa.selenium.By;

public class Page_Advantage {
    public static By lblMice = By.id("miceTxt");
    public static By lstMice = By.xpath("//div[@class='cell categoryRight']/ul/li");
    public static By btnAddToCart = By.name("save_to_cart");
    public static By lblShoppingCartCount = By.xpath("//a[@id='shoppingCartLink']/span");
    public static By btnUser = By.id("menuUserLink");
    public static By lnkCreateNewAccount = By.xpath("//a[@translate='CREATE_NEW_ACCOUNT']");
    public static By fldUsername = By.name("usernameRegisterPage");
    public static By fldEmail = By.name("emailRegisterPage");
    public static By fldPassword = By.name("passwordRegisterPage");
    public static By fldConfirmPassword = By.name("confirm_passwordRegisterPage");
    public static By cbxIAgree = By.name("i_agree");
    public static By btnRegister = By.id("register_btnundefined");
    public static By lblMessageRegister = By.xpath("//div[@class='center']/label[1]");
}
