document.addEventListener("DOMContentLoaded", function () {
    const registerForm = document.getElementById("registerForm");

    registerForm.addEventListener("submit", handleRegister);
});

function handleRegister(event) {
    event.preventDefault();
    const login = document.getElementById("login").value;
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;
    const city = document.getElementById("city").value;

    if (!validateForm(password, confirmPassword)) return;

    sendRegisterRequest(login, password, city);
}

function validateForm(password, confirmPassword) {
    if (password !== confirmPassword) {
        alert("Пароли не совпадают!");
        return false;
    }
    return true;
}

function sendRegisterRequest(login, password, city) {
    fetch("/auth/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ login, password, city }),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            if (data.success) {
                alert(data.message);
                window.location.href = "/login";
            } else {
                alert(data.message);
            }
        })
        .catch(error => {
            alert("Ой, что-то пошло не так. Попробуйте позже.");
            console.error("Ошибка при запросе:", error);
        });
}