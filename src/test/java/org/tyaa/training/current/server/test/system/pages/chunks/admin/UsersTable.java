package org.tyaa.training.current.server.test.system.pages.chunks.admin;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.tyaa.training.current.server.test.system.pages.BasePage;

import java.util.List;

@Getter
public class UsersTable extends BasePage {

    @FindBy(css = "#users-table")
    private WebElement usersTableWebElement;

    public UsersTable(WebDriver driver) {
        super(driver);
    }

    public List<String> getHeaders() {
        return usersTableWebElement.findElements(By.cssSelector("thead > tr > th")).stream()
                .map(WebElement::getText)
                .toList();
    }

    public List<Row> getRows() {
        return usersTableWebElement.findElements(By.cssSelector("tbody > tr")).stream()
                .map(rowWebElement -> new Row(
                        Long.valueOf(rowWebElement.findElement(By.cssSelector("th")).getText()),
                        rowWebElement.findElement(By.cssSelector("td:nth-child(2)")).getText(),
                        rowWebElement.findElement(By.cssSelector("td > div > span")).getText())
                ).toList();
    }

    public record Row(Long id, String login, String role) {}
}