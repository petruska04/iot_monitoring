document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("loginForm").addEventListener("submit", async function (event) {
        event.preventDefault();

        const login = document.getElementById("login").value.trim();
        const password = document.getElementById("password").value.trim();

        if (!login || !password) {
            alert("Пожалуйста, заполните все поля.");
            return;
        }

        const payload = {
            login: login,
            password: password
        };

        try {
            const response = await fetch('/auth/login', {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(payload)
            });

            const data = await response.json();
            if (data.success) {
                localStorage.setItem('userData', JSON.stringify(data));
                console.log("Данные сохранены в localStorage:", data);
                window.location.href = "/devices";
            } else {
                alert(data.message);
            }
        } catch (error) {
            console.error("Ошибка:", error);
            alert("Произошла ошибка при подключении к серверу.");
        }
    });
});