document.getElementById('requestResetForm').addEventListener('submit', async function(event) {
    event.preventDefault();
    const email = document.getElementById('email').value;

    try {
        const response = await fetch(`http://localhost:8090/auth/send-reset-code?email=${encodeURIComponent(email)}`, {
            method: 'GET'
        });

        if (response.ok) {
            window.location.href = '../password-reset/password-reset.html';
        } else {
            console.error('Failed to send reset code:', response.status, response.statusText);
            alert('Failed to send reset code. Please check the email provided.');
        }
    } catch (error) {
        console.error('Error sending reset code:', error);
        alert('An error occurred while sending the reset code.');
    }
});
