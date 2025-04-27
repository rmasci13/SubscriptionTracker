// Function used in the sign up page to send sign up information to the public API route creating a new user
async function signUp(event) {
    // Prevent default submit so submission logic can be handled manually
    event.preventDefault();
    // Extract sign up information from the form fields and create a user object
    const user = {
        username: document.getElementById('username').value,
        email: document.getElementById('email').value,
        password: document.getElementById('password').value
    };
    // Fetch POST request to public API route to create user
    fetch('/public/api/user', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(user)
    })
        // Wait for response, make sure it's ok
        .then(response => {
            if (!response.ok) {
                throw new Error('Error in sign up');
            }
            return response.json();
        })
        // If the response was ok then it worked, log that it worked, redirect to login page
        .then(data => {
            console.log('User successfully created', data);
            window.location.href = "/login";
        })
        .catch(error => {
            console.error('Signup failed', error);
            alert('Signup failed: ' + error.message);
        });
}