document.getElementById('registerForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const mesajDiv = document.getElementById('mesajStatus');
    mesajDiv.style.display = 'none';

    const date = {
        nume: document.getElementById('regNume').value,
        prenume: document.getElementById('regPrenume').value,
        email: document.getElementById('regEmail').value,
        telefon: document.getElementById('regTelefon').value,
        username: document.getElementById('regUsername').value,
        parola: document.getElementById('regParola').value
    };

    fetch('/api/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(date)
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === 'succes') {
            mesajDiv.className = 'alert alert-success';
            mesajDiv.innerText = 'Cont creat! Te redirecționăm...';
            mesajDiv.style.display = 'block';
            setTimeout(() => window.location.href = '/login', 2000);
        } else {
            mesajDiv.className = 'alert alert-danger';
            mesajDiv.innerText = 'Eroare: ' + data.mesaj;
            mesajDiv.style.display = 'block';
        }
    })
    .catch(err => console.error('Eroare:', err));
});