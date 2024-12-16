document.addEventListener("DOMContentLoaded", async function () {
    const userData = JSON.parse(localStorage.getItem('userData'));
    if (!userData || !userData.success || !userData.user || !userData.user.id) {
        alert("Вы не авторизованы. Пожалуйста, войдите снова.");
        window.location.href = "/login";
        return;
    }

    const deviceList = document.getElementById("deviceList");
    const addDeviceForm = document.getElementById("addDeviceForm");
    const refreshButton = document.getElementById("refreshDevices"); // Кнопка для обновления

    // Функция для получения всех устройств пользователя
    async function fetchAllDeviceData(userId) {
        try {
            const response = await fetch('/auth/getDeviceData', {
                method: "POST",
                headers: { "Content-Type": "application/json; charset=UTF-8" },
                body: JSON.stringify({ userId })
            });
            const data = await response.json();
            return data.success ? data.devices : null;
        } catch (error) {
            console.error("Ошибка при получении данных устройств:", error);
            return null;
        }
    }

    // Функция для обновления списка устройств
    async function updateDeviceList() {
        deviceList.innerHTML = ""; // Очищаем список устройств

        // Получаем данные об устройствах с сервера
        const devicesData = await fetchAllDeviceData(userData.user.id);
        if (!devicesData) {
            alert("Не удалось получить данные об устройствах.");
            return;
        }

        // Сохраняем данные об устройствах в localStorage
        userData.devices = devicesData;
        localStorage.setItem('userData', JSON.stringify(userData));

        // Отображаем устройства на странице
        devicesData.forEach(device => {
            const listItem = document.createElement("li");
            listItem.classList.add("device-item");
            listItem.textContent = `Устройство: ${device.typeDevice} (${device.name}), Статус: ${device.statuses}`;

            // Кнопка удаления устройства
            const deleteDeviceButton = document.createElement("button");
            deleteDeviceButton.textContent = "Удалить устройство";
            deleteDeviceButton.classList.add("delete-button");
            deleteDeviceButton.addEventListener("click", async () => {
                const response = await fetch('/auth/deleteDevice', {
                    method: "POST",
                    headers: { "Content-Type": "application/json; charset=UTF-8" },
                    body: JSON.stringify({ userId: userData.user.id, typeDevice: device.typeDevice, name: device.name })
                });
                const data = await response.json();
                if (data.success) {
                    alert(data.message);
                    userData.devices = userData.devices.filter(d => d.name !== device.name || d.typeDevice !== device.typeDevice);
                    localStorage.setItem('userData', JSON.stringify(userData));
                    updateDeviceList();
                } else {
                    alert(data.message);
                }
            });

            // Кнопка переключения статуса устройства
            const toggleButton = document.createElement("button");
            toggleButton.textContent = device.statuses === "Включено" ? "Выключить" : "Включить";
            toggleButton.addEventListener("click", async () => {
                const newStatus = device.statuses === "Включено" ? "Выключено" : "Включено";
                const response = await fetch('/auth/updateDeviceStatus', {
                    method: "POST",
                    headers: { "Content-Type": "application/json; charset=UTF-8" },
                    body: JSON.stringify({ userId: userData.user.id, typeDevice: device.typeDevice, name: device.name, statuses: newStatus })
                });
                const data = await response.json();
                if (data.success) {
                    device.statuses = newStatus;
                    localStorage.setItem('userData', JSON.stringify(userData));
                    updateDeviceList();
                } else {
                    alert(data.message);
                }
            });

            // Добавляем кнопки в зависимости от статуса
            if (device.statuses === "Выключено") {
                listItem.appendChild(toggleButton);
                listItem.appendChild(deleteDeviceButton);
            } else if (device.statuses === "Включено") {
                listItem.appendChild(toggleButton);
                listItem.appendChild(deleteDeviceButton);

                // Дополнительная информация для устройств
                if (device.typeDevice === "Кондиционер") {
                    const temperatureSlider = document.createElement("input");
                    temperatureSlider.type = "number";
                    temperatureSlider.min = 17;
                    temperatureSlider.max = 26;
                    temperatureSlider.value = device.temperature;
                    temperatureSlider.addEventListener("input", async () => {
                        const newTemperature = temperatureSlider.value;
                        const response = await fetch('/auth/updateDeviceStatus', {
                            method: "POST",
                            headers: { "Content-Type": "application/json; charset=UTF-8" },
                            body: JSON.stringify({ userId: userData.user.id, typeDevice: device.typeDevice, name: device.name, statuses: device.statuses, temperature: newTemperature })
                        });
                        const data = await response.json();
                        if (data.success) {
                            device.temperature = newTemperature;
                            localStorage.setItem('userData', JSON.stringify(userData));
                            updateDeviceList();
                        } else {
                            alert(data.message);
                        }
                    });
                    listItem.appendChild(temperatureSlider);
                } else if (device.typeDevice === "Увлажнитель") {
                    const waterLevel = document.createElement("span");
                    waterLevel.textContent = `Уровень воды: ${device.waterLevel}%`;
                    listItem.appendChild(waterLevel);
                } else if (device.typeDevice === "Термостат") {
                    const currentTemperature = document.createElement("span");
                    currentTemperature.textContent = `Текущая температура: ${device.currentTemperature}°C`;
                    listItem.appendChild(currentTemperature);
                }
            }

            deviceList.appendChild(listItem);
        });
    }

    // Обработчик для добавления нового устройства
    addDeviceForm.addEventListener("submit", async function (event) {
        event.preventDefault();
        const typeDevice = document.getElementById("typeDevice").value.trim();
        const name = document.getElementById("name").value.trim();
        if (!typeDevice || !name) {
            alert("Пожалуйста, заполните все поля.");
            return;
        }
        const defaultStatus = "Выключено";
        const response = await fetch('/auth/addDevice', {
            method: "POST",
            headers: { "Content-Type": "application/json; charset=UTF-8" },
            body: JSON.stringify({ userId: userData.user.id, typeDevice, name, statuses: defaultStatus })
        });
        const data = await response.json();
        if (data.success) {
            alert(data.message);
            userData.devices.push({ typeDevice, name, statuses: defaultStatus });
            localStorage.setItem('userData', JSON.stringify(userData));
            updateDeviceList();
        } else {
            alert(data.message);
        }
    });

    // Обработчик для кнопки обновления состояния устройств
    refreshButton.addEventListener("click", updateDeviceList);

    // Загружаем устройства сразу после загрузки страницы
    updateDeviceList();
});