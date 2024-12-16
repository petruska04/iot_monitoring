document.addEventListener("DOMContentLoaded", function () {
    // Обработка кнопки "Войти"
    document.getElementById("loginButton").addEventListener("click", function () {
        // Открываем новую страницу
        window.location.href = "/login";
    });

    // Обработка кнопки "Зарегистрироваться"
    document.getElementById("registerButton").addEventListener("click", function () {
        // Открываем новую страницу
        window.location.href = "/register";
    });
});
