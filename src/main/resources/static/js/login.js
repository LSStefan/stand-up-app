document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const errorDiv = document.getElementById('mesajEroare');
    errorDiv.style.display = 'none';

    const date = {
        username: document.getElementById('username').value,
        parola: document.getElementById('parola').value
    };

    fetch('/api/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(date)
    })
    .then(response => {
        if (response.ok) {
            window.location.href = '/dashboard';
        } else {
            return response.json().then(data => {
                errorDiv.innerText = data.mesaj || "Eroare la logare!";
                errorDiv.style.display = 'block';
            });
        }
    })
    .catch(err => {
        console.error('Eroare:', err);
        errorDiv.innerText = "Eroare conexiune server.";
        errorDiv.style.display = 'block';
    });
});