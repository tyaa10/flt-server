package org.tyaa.training.current.server.test.system.pages;

import org.tyaa.training.current.server.test.system.pages.chunks.common.AccountNavBar;

/**
 * Абстракция для страниц, отображаемых после входа в учётную запись пользователя
 * */
public interface ILoggedIn {

    AccountNavBar getAccountNavBar();
}
