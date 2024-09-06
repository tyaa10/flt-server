document.addEventListener('DOMContentLoaded', async () => {

    /**
    * Общая логика frontend
    */

    // Получить базовый адрес из мета-тега главного файла
    const contextPath = document.querySelector('meta[name="app-context-path"]').getAttribute('content');
    // Проверить состояние аутентификации
    const checkAuth = async () => await fetch(`${contextPath}/api/auth/users/check`, {credentials: 'include'})
                                    .then(async response => {
                                        if (response.status === 200) {
                                            return await response.json().then(data => {
                                                if (Object.hasOwn(data, 'data')) {
                                                    return data['data'];
                                                } else {
                                                    return false;
                                                }
                                            })
                                        } else {
                                            return false;
                                        }
                                    });
    /**
     * Действие выхода из учётной записи
     * (во время его вызова в DOM должен присутствовать элемент с id="sign-out_form_status" для вывода сообщения в случае ошибки выполнения http-запроса)
    */
    const signOutAction = event => {
        event.preventDefault();
        const status = document.getElementById("sign-out_form_status");
        fetch(`${contextPath}/logout`).then(async response => {
            if (response.ok) {
                setPage(loginPage);
                loginPageScript();
            } else {
                response.json().then(data => {
                    if (Object.hasOwn(data, 'message')) {
                        showFormState(status, 'fail', data["message"])
                    } else {
                        showFormState(status, 'fail', 'Unknown server error')
                    }
                });
            }
        }).catch(error => {
            showFormState(status, 'fail', 'There was a problem performing logout')
        });
    }
    // Функция установки разметки текущего раздела в разметку главного файла
    const setPage = page => {
        document.querySelector('#app').innerHTML = page;
    }
    // Функция выдержки времени
    const sleep = ms => new Promise(r => setTimeout(r, ms));
    // Функция отображения состояния формы - текста и цвета (зелёный/красный)
    const showFormState = (elementObject, messageType, messageText) => {
        if (elementObject) {
            elementObject.innerHTML = messageText;
            if (messageType === 'success') {
                elementObject.classList.remove('fail');
                elementObject.classList.add('success');
            } else if (messageType === 'fail') {
                elementObject.classList.remove('success');
                elementObject.classList.add('fail');
            } else {
                elementObject.classList.remove('success');
                elementObject.classList.remove('fail');
            }
        }
    }

    /**
    * Пары "представление + логика представления" разделов frontend
    */

    const loginPage = `<!-- Login Page View -->
        <div class="background">
            <div class="shape"></div>
            <div class="shape"></div>
        </div>
        <form id="sign-in_form" action="${contextPath}/login" method="POST">
            <h3>Login</h3>
            <label for="username">Username</label>
            <input type="text" placeholder="Login" id="username" name="username">
            <label for="password">Password</label>
            <input type="password" placeholder="Password" id="password" name="password">
            <p id="sign-in_form_status" class="form_status"></p>
            <button>Sign In</button>
            <div class="form_nav_bar">
                <span>Don't have an account?</span><span><a id="nav-to-signup" href="#sign-up">Sign Up</a></span>
            </div>
        </form>
    `;

    const loginPageScript = () => { /* Login Page Script */
        const form = document.getElementById("sign-in_form");
        const navToSignUp = document.getElementById("nav-to-signup");
        const navToSignUpAction = event => {
            setPage(registrationPage);
            registrationPageScript();
        }
        async function handleSubmit(event) {
            event.preventDefault();
            const status = document.getElementById("sign-in_form_status");
            showFormState(status, '', '');
            const data = new FormData(event.target);
            fetch(event.target.action, {
                method: form.method,
                body: data,
                credentials: 'include'
            }).then(async response => {
                if (response.ok) {
                    // Сбросить значения полей ввода формы
                    form.reset();
                    // Получить состояние аутентификации
                    const userModel = await checkAuth();
                    // Если состояние аутентификации получено
                    if (userModel) {
                        // Если пользователь - управляющий содержимым
                        if (userModel.roleName === 'ROLE_CONTENT_MANAGER') {
                            // Установить раздел управления содержимым системы (выгрузки файлов импорта данных)
                            setPage(uploadPage);
                            uploadPageScript();
                        } else if (userModel.roleName === 'ROLE_ADMIN') {
                            // Если пользователь - администратор
                            // Установить раздел администрирования (управления пользователями)
                            setPage(adminPage);
                            adminPageScript();
                        } else {
                            // Если роль пользователя - клиент -
                            // вывести сообщение об отсутствии прав на вход в какой-либо из разделов web-панели администрирования
                            showFormState(status, 'fail', 'Access denied')
                        }
                    } else {
                        // Если пользователь не аутентифицирован -
                        // увывести сообщение об отсутствии аутентификации
                        showFormState(status, 'fail', 'Not authenticated')
                    }
                } else {
                    response.json().then(data => {
                        if (Object.hasOwn(data, 'message')) {
                            showFormState(status, 'fail', data["message"])
                        } else {
                            showFormState(status, 'fail', 'Unknown server error')
                        }
                    })
                }
            }).catch(error => {
                showFormState(status, 'fail', 'There was a problem submitting login data')
            });
        }
        form.addEventListener("submit", handleSubmit);
        navToSignUp.addEventListener("click", navToSignUpAction);
    }

    const registrationPage = `<!-- Registration Page View -->
        <div class="background">
            <div class="shape"></div>
            <div class="shape"></div>
        </div>
        <form id="sign-up_form" action="${contextPath}/api/auth/users" method="POST">
            <h3>Registration</h3>
            <label for="username">Username</label>
            <input type="text" placeholder="Login" id="username" name="name" required pattern="[a-z0-9_\\-]{3,16}" title="Username can contain digits from 0 to 9, lowercase letters, _ and - characters, no space, and it must be 3-16 characters long">
            <label for="password">Password</label>
            <input type="password" placeholder="Password" id="password" name="password" required pattern="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.* ).{8,16}" title="Password must contain one digit from 0 to 9, one lowercase letter, one uppercase letter, one special character, no space, and it must be 8-16 characters long">
            <label for="password">Confirm Password</label>
            <input type="password" placeholder="Confirm Password" id="confirm-password" required pattern="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.* ).{8,16}" title="Password must contain one digit from 0 to 9, one lowercase letter, one uppercase letter, one special character, no space, and it must be 8-16 characters long">
            <p id="sign-up_form_status" class="form_status"></p>
            <button>Sign Up</button>
            <div class="form_nav_bar">
                <span>Already have an account?</span><span><a id="nav-to-signin" href="#sign-in">Sign In</a></span>
            </div>
        </form>
    `

    const registrationPageScript = () => { /* Registration Page Script */
        const form = document.getElementById("sign-up_form");
        const passwordInput = document.getElementById('password');
        const confirmPasswordInput = document.getElementById('confirm-password');
        const navToSignIn = document.getElementById("nav-to-signin");
        const navToSignInAction = event => {
            setPage(loginPage);
            loginPageScript();
        }
        const validatePasswordMatching = () => {
            if (passwordInput.value !== confirmPasswordInput.value) {
                confirmPasswordInput.setCustomValidity("Passwords don't match");
            } else {
                confirmPasswordInput.setCustomValidity('')
            }
        }
        async function handleSubmit(event) {
            event.preventDefault();
            if (form.reportValidity()) {
                const status = document.getElementById("sign-up_form_status");
                const data = JSON.stringify(Object.fromEntries(new FormData(event.target)));
                fetch(event.target.action, {
                    method: form.method,
                    body: data,
                    headers: {'Content-Type': 'application/json'},
                    credentials: 'include'
                }).then(async response => {
                    if (response.ok) {
                        showFormState(status, 'success', 'You are successfully registered');
                        form.reset();
                        await sleep(3000);
                        console.log('Navigating to the Login page...');
                        navToSignInAction();
                    } else {
                        response.json().then(data => {
                            if (Object.hasOwn(data, 'message')) {
                                showFormState(status, 'fail', data["message"]);
                            } else {
                                showFormState(status, 'fail', 'Unknown registration error');
                            }
                        });
                    }
                }).catch(error => {
                    showFormState(status, 'fail', 'There was a problem submitting registration data');
                });
            }
        }
        form.addEventListener("submit", handleSubmit);
        passwordInput.addEventListener("input", validatePasswordMatching);
        confirmPasswordInput.addEventListener("input", validatePasswordMatching);
        navToSignIn.addEventListener("click", navToSignInAction);
    }

    const uploadPage = `<!-- Upload Page View -->
        <div class="background">
            <div class="shape"></div>
            <div class="shape"></div>
        </div>
        <div class="account_nav_bar">
            <span>Change account?</span><span><a id="sign-out" href="#sign-out">Sign Out</a></span>
        </div>
        <form id="content-management_form" action="${contextPath}/api/import/lessons/word-study/zip" method="POST">
            <h3>Content Management</h3>
            <label for="words-upload">Choose a words-import xlsx file:</label>
            <input type="file" id="words-upload" name="file" required />
            <p id="upload_form_status" class="form_status"></p>
            <button>Upload</button>
            <p id="sign-out_form_status" class="form_status"></p>
        </form>
    `;

    const uploadPageScript = () => { /* Upload Page Script */
        const form = document.getElementById("content-management_form");
        const signOut = document.getElementById('sign-out');
        async function handleSubmit(event) {
            event.preventDefault();
            const status = document.getElementById("upload_form_status");
            const data = new FormData(event.target);
            fetch(event.target.action, {
                method: form.method,
                body: data,
                credentials: 'include'
            }).then(async response => {
                if (response.ok) {
                    response.json().then(async data => {
                        if (Object.hasOwn(data, 'message')) {
                            showFormState(status, 'success', data["message"]);
                        } else {
                            showFormState(status, 'success', 'Word lesson data uploaded successfully');
                        }
                        form.reset();
                        await sleep(3000);
                        showFormState(status, '', '');
                    })
                } else {
                    response.json().then(data => {
                        if (Object.hasOwn(data, 'message')) {
                            showFormState(status, 'fail', data["message"]);
                        } else {
                            showFormState(status, 'fail', 'Unknown server error');
                        }
                    })
                }
            }).catch(error => {
                showFormState(status, 'fail', 'There was a problem submitting word lesson data');
            });
        }
        form.addEventListener("submit", handleSubmit);
        signOut.addEventListener("click", signOutAction);
    }

    const adminPage = `<!-- Admin Page View -->
        <div class="background">
            <div class="shape"></div>
            <div class="shape"></div>
        </div>
        <div class="account_nav_bar">
            <span>Change account?</span><span><a id="sign-out" href="#sign-out">Sign Out</a></span>
        </div>
        <div class="panel">
            <p id="admin_panel_status"></p>
            <h2>Users</h2>
            <table id="users-table">
                <thead>
                    <tr>
                        <th>id</th>
                        <th>login</th>
                        <th>role</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <th scope="row">1</th>
                        <td>admin</td>
                        <td>
                            <div class="dropdown">
                                <span>admin</span>
                                <div class="dropdown-content">
                                    <p>admin</p>
                                    <p>content provider</p>
                                    <p>customer</p>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">2</th>
                        <td>one</td>
                        <td>customer</td>
                    </tr>
                    <tr>
                        <th scope="row">3</th>
                        <td>two</td>
                        <td>customer</td>
                    </tr>
                    <tr>
                        <th scope="row">4</th>
                        <td>three</td>
                        <td>customer</td>
                    </tr>
                    <tr>
                        <th scope="row">5</th>
                        <td>content_manager</td>
                        <td>content provider</td>
                    </tr>
                </tbody>
            </table>
            <div style="clear: both;"></div>
    `;

    const adminPageScript = async () => { /* Admin Page Script */
        const signOut = document.getElementById('sign-out');
        const status = document.getElementById("admin_panel_status");
        const tableBody = document.querySelector("#users-table tbody");
        tableBody.innerHTML = '';
        // определить функцию действий при выборе роли для пользователя
        const changeUserRole = (userId, currentRoleId, newRoleId) => {
            // если выбрана роль, отличная от текущей
            if (currentRoleId !== newRoleId) {
                // отправить на сервер запрос изменения роли указанного пользователя
                fetch(`${contextPath}/api/auth/admin/users/${userId}/change-role/${newRoleId}`, {method: 'PATCH'}).then(async response => {
                    if (response.ok) {
                        populateUsersTable();
                    } else {
                        response.json().then(data => {
                            if (Object.hasOwn(data, 'message')) {
                                showFormState(status, 'fail', data["message"]);
                                return false;
                            } else {
                                showFormState(status, 'fail', 'Unknown server error');
                                return false;
                            }
                        });
                    }
                }).catch(error => {
                    showFormState(status, 'fail', 'There was a problem chaning user role');
                    return false;
                });
            }
        };
        window.changeUserRole = changeUserRole;
        // получить с сервера список моделей всех ролей
        const roles = await fetch(`${contextPath}/api/auth/admin/roles`).then(async response => {
            if (response.ok) {
                return response.json().then(data => {
                    if (Object.hasOwn(data, 'data')) {
                        return data["data"];
                    } else {
                        showFormState(status, 'fail', 'No role data');
                        return false;
                    }
                });
            } else {
                return response.json().then(data => {
                    if (Object.hasOwn(data, 'message')) {
                        showFormState(status, 'fail', data["message"]);
                        return false;
                    } else {
                        showFormState(status, 'fail', 'Unknown server error');
                        return false;
                    }
                });
            }
        }).catch(error => {
            showFormState(status, 'fail', 'There was a problem fetching roles');
            return false;
        });
        // функция получения с сервера списка моделей всех пользователей
        const populateUsersTable = () => fetch(`${contextPath}/api/auth/admin/users`).then(async response => {
                if (response.ok) {
                    response.json().then(data => {
                        if (Object.hasOwn(data, 'data')) {
                            tableBody.innerHTML = '';
                            const users = data["data"];
                            for (const user of users) {
                                let rolesHtml = '';
                                for (const role of roles) {
                                    const changeUserRoleHandlerHtml = `changeUserRole(${user.id}, ${user.roleId}, ${role.id})`;
                                    rolesHtml += '<p onclick="' + changeUserRoleHandlerHtml + `">${role.name.replace('ROLE_', '')}</p>`;
                                }
                                tableBody.innerHTML += `<tr>
                                                            <th scope="row">${user.id}</th>
                                                            <td>${user.name}</td>
                                                            <td>
                                                                <div class="dropdown">
                                                                    <span>${user.roleName.replace('ROLE_', '')}</span>
                                                                    <div class="dropdown-content">${rolesHtml}</div>
                                                                </div>
                                                            </td>
                                                        </tr>`;
                            }
                        } else {
                            showFormState(status, 'fail', 'No user data');
                        }
                    });
                } else {
                    response.json().then(data => {
                        if (Object.hasOwn(data, 'message')) {
                            showFormState(status, 'fail', data["message"]);
                        } else {
                            showFormState(status, 'fail', 'Unknown server error');
                        }
                    });
                }
            }).catch(error => {
                showFormState(status, 'fail', 'There was a problem fetching users');
            });
        // если список моделей ролей успешно получен с сервера
        if (roles) {
           // получить с сервера список моделей всех пользователей
           populateUsersTable();
        }
        signOut.addEventListener("click", signOutAction);
    }

    /**
    * Установка начального раздела frontend
    */

    // Получить состояние аутентификации
    const userModel = await checkAuth();
    // Если пользователь аутентифицирован
    if (userModel) {
        // Если пользователь - управляющий содержимым
        if (userModel.roleName === 'ROLE_CONTENT_MANAGER') {
            // Установить раздел управления содержимым системы (выгрузки файлов импорта данных)
            setPage(uploadPage);
            uploadPageScript();
        } else if (userModel.roleName === 'ROLE_ADMIN') {
            // Если пользователь - администратор
            // Установить раздел администрирования (управления пользователями)
            setPage(adminPage);
            adminPageScript();
        } else {
            // Если роль пользователя - клиент -
            // установить раздел входа в учётную запись
            setPage(loginPage);
            loginPageScript();
        }
    } else {
        // Если пользователь не аутентифицирован -
        // установить раздел входа в учётную запись
        setPage(loginPage);
        loginPageScript();
    }
});