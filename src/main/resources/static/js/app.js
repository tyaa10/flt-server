document.addEventListener('DOMContentLoaded', async () => {
    const contextPath = document.querySelector('meta[name="app-context-path"]').getAttribute('content');
    // const text = await fetch(`${contextPath}/api/text`).then(response => response.text());
    // document.querySelector('#app').innerHTML = `<h1>Context Path: ${contextPath}</h1>`;
    const isAuth = async () => {
        return (await fetch(`${contextPath}/api/auth/users/check`, {credentials: 'include'}).then(response => response.status)) === 200 ? true : false;
    }
    const setPage = pageName => {
        document.querySelector('#app').innerHTML = pageName
    }

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
            <p id="sign-in_form_status"></p>
            <button>Sign In</button>
        </form>
    `

    const loginPageScript = () => { /* Login Page Script */
        const form = document.getElementById("sign-in_form");
        const sleep = ms => new Promise(r => setTimeout(r, ms));
        async function handleSubmit(event) {
            event.preventDefault();
            const status = document.getElementById("sign-in_form_status");
            const data = new FormData(event.target);
            fetch(event.target.action, {
                method: form.method,
                body: data,
                /* headers: {'Content-Type': 'application/x-www-form-urlencoded'}, */
                credentials: 'include'
            }).then(async response => {
                if (response.ok) {
                    status.innerHTML = "You are successfully logged in";
                    form.reset();
                    await sleep(3000);
                    console.log('Navigating to the Content Management page...');
                    setPage(uploadPage)
                } else {
                    response.json().then(data => {
                        if (Object.hasOwn(data, 'message')) {
                            status.innerHTML = data["message"]
                        } else {
                            status.innerHTML = "Unknown server error"
                        }
                    })
                }
            }).catch(error => {
                status.innerHTML = "There was a problem submitting login data"
            });
        }
        form.addEventListener("submit", handleSubmit)
    }

    const registrationPage = `<!-- Registration Page View and Script -->
        <div class="background">
            <div class="shape"></div>
            <div class="shape"></div>
        </div>
        <form id="sign-up_form" action="${contextPath}/api/auth/users" method="POST">
            <h3>Registration</h3>
            <label for="username">Username</label>
            <input type="text" placeholder="Login" id="username" name="name" required pattern="[a-z0-9_\\-]{3,16}" title="Username can contain digits from 0 to 9, lowercase letters, _ and - characters, no space, and it must be 3-16 characters long">
            <label for="password">Password</label>
            <input type="password" placeholder="Password" id="password" name="password" required pattern="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.* ).{8,16}">
            <label for="password">Confirm Password</label>
            <input type="password" placeholder="Confirm Password" id="confirm-password" required pattern="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.* ).{8,16}" title="Password must contain one digit from 0 to 9, one lowercase letter, one uppercase letter, one special character, no space, and it must be 8-16 characters long">
            <p id="sign-up_form_status"></p>
            <button>Sign Up</button>
        </form>
    `

    const registrationPageScript = () => { /* Registration Page Script */
        const form = document.getElementById("sign-up_form");
        const sleep = ms => new Promise(r => setTimeout(r, ms));
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
                        status.innerHTML = "You are successfully registered";
                        form.reset();
                        await sleep(3000);
                        console.log('Navigating to the Login page...');
                        setPage(loginPage)
                        loginPageScript()
                    } else {
                        response.json().then(data => {
                            if (Object.hasOwn(data, 'message')) {
                                status.innerHTML = data["message"]
                            } else {
                                status.innerHTML = "Unknown registration error"
                            }
                        })
                    }
                }).catch(error => {
                    status.innerHTML = "There was a problem submitting registration data"
                });
            }
        }
        form.addEventListener("submit", handleSubmit)
    }

    const uploadPage = `
        <div class="background">
            <div class="shape"></div>
            <div class="shape"></div>
        </div>
        <form>
            <h3>Content Management</h3>
            <label for="words-upload">Choose a words-import xlsx file:</label>
            <input type="file" id="words-upload" name="file" />
            <button>Upload</button>
            <button>Sign Out</button>
        </form>
    `

    if (await isAuth()) {
        setPage(uploadPage)
    } else {
        setPage(registrationPage)
        registrationPageScript()
    }
});