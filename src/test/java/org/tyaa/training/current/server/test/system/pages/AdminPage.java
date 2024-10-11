package org.tyaa.training.current.server.test.system.pages;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.tyaa.training.current.server.test.system.pages.annotations.PageModel;
import org.tyaa.training.current.server.test.system.pages.chunks.admin.UsersTable;
import org.tyaa.training.current.server.test.system.pages.chunks.common.AccountNavBar;

@Getter
@PageModel(name = "администратор")
public class AdminPage extends BasePage implements ILoggedIn {

    private final AccountNavBar accountNavBar;
    private final UsersTable usersTable;

    public AdminPage(WebDriver driver) {
        super(driver);
        accountNavBar = new AccountNavBar(driver);
        usersTable = new UsersTable(driver);
    }

    @Override
    public AccountNavBar getAccountNavBar() {
        return accountNavBar;
    }
}
