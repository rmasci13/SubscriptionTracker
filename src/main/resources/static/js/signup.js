async function signUp(event) {
    event.preventDefault();

    const user = {
        username: document.getElementById('username').value,
        email: document.getElementById('email').value,
        password: document.getElementById('password').value
    };

    fetch('/public/api/user', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(user)
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(err => {
                    throw new Error(err);
                });
            }
            return response.json();
        })
        .then(data => {
            console.log('User successfully created', data);
            window.location.href = "/login";
        })
        .catch(error => {
            console.error('Signup failed', error);
            alert('Signup failed: ' + error.message);
        });
}