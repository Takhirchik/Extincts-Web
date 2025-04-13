document.addEventListener('DOMContentLoaded', function() {
    // Handle sign in form submission
    const signInForm = document.getElementById('signInForm');
    if (signInForm) {
        signInForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const login = document.getElementById('login').value;
            const password = document.getElementById('password').value;

            fetch('/sign-in', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ login, password })
            })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error('Login failed');
            })
            .then(data => {
                localStorage.setItem('token', data.token);
                window.location.href = '/';
            })
            .catch(error => {
                alert(error.message);
            });
        });
    }

    // Handle sign up form submission
    const signUpForm = document.getElementById('signUpForm');
    if (signUpForm) {
        signUpForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const nickname = document.getElementById('nickname').value;
            const login = document.getElementById('login').value;
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value

            fetch('/sign-up', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ nickname, login, email, password, confirmPassword })
            })
            .then(response => {
                if (response.ok) {
                    alert('Registration successful! Please sign in.');
                    window.location.href = '/sign-in';
                } else {
                    throw new Error('Registration failed');
                }
            })
            .catch(error => {
                alert(error.message);
            });
        });
    }

    // Handle logout
    const logoutButton = document.getElementById('logout');
    if (logoutButton) {
        logoutButton.addEventListener('click', function(e) {
            e.preventDefault();
            localStorage.removeItem('token');
            window.location.href = '/';
        });
    }
});