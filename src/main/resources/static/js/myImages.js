// Получение списка картинок с помощью AJAX
fetch('/user/images')
    .then(response => response.json())
    .then(images => {
        const dynamicImagesDiv = document.getElementById('dynamicImages');

        // Создание и добавление новых img элементов и кнопок на страницу
        images.forEach(image => {
            const imageUrl = URL.createObjectURL(image);
            const img = document.createElement('img');
            img.src = imageUrl;
            dynamicImagesDiv.appendChild(img);
        });
    });