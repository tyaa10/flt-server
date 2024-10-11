# language: ru
@cucumber
@вход
@положительный
Функциональность: Вход в WEB-панель управления

  Структура сценария: Открыть начальную страницу и выполнить попытку входа с правильными данными учётной записи пользователя
    Дано WEB-приложение, открытое в браузере '<browser>'
    Когда Пользователь выполняет попытку входа с логином '<login>' и паролем '<password>' в браузере '<browser>'
    Тогда В окне браузера '<browser>' отображается страница 'администратор'

  Примеры:
  | browser | login           | password        |
  | chrome  | admin           | AdminPassword1% |
  | gecko   | admin           | AdminPassword1% |
#  | chrome  | one             | UserPassword1%  |
#  | gecko   | one             | UserPassword1%  |
#  | chrome  | content_manager | CMPassword1%    |
#  | gecko   | content_manager | CMPassword1%    |