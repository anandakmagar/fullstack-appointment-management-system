document.getElementById('resetPasswordForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const email = document.getElementById('email').value;
    const resetCode = document.getElementById('resetCode').value;
    const newPassword = document.getElementById('newPassword').value;

    try {
        const response = await fetch('http://localhost:8090/auth/change-password', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, resetCode: parseInt(resetCode), newPassword })
        });

        if (response.ok) {
            alert('Password successfully reset. Please log in with your new password.');
            window.location.href = '../login/login.html';
        } else {
            const errorResponse = await response.json(); // Assuming the server sends a JSON response for errors
            console.error('Failed to change password:', response.status, response.statusText, errorResponse);
            document.getElementById('errorMessage').textContent = errorResponse.message || 'Failed to change password. Please check your credentials and reset code.';
            document.getElementById('errorMessage').style.display = 'block';
        }
    } catch (error) {
        console.error('Error changing password:', error);
        document.getElementById('errorMessage').textContent = 'An error occurred while changing the password.';
        document.getElementById('errorMessage').style.display = 'block';
    }
});