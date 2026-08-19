document.getElementById('login-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const errorBox = document.getElementById('error-message');
    errorBox.textContent = '';

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        if (!response.ok) {
            errorBox.textContent = 'Email ou mot de passe incorrect.';
            return;
        }

        const data = await response.json();
        // Cookie lisible par le filtre côté serveur, envoyé automatiquement par le navigateur
        document.cookie = `jwt=${data.token}; path=/; max-age=86400; SameSite=Strict`;
        window.location.href = '/ui/promotions';
    } catch (err) {
        errorBox.textContent = 'Erreur de connexion au serveur.';
    }
});