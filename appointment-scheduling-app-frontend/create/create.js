document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('logoutButton').addEventListener('click', () => {
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');
        window.location.href = '../login/login.html';
    });

    document.getElementById('clientCreateForm').addEventListener('submit', async (event) => {
        event.preventDefault();
        await createUser('client');
    });

    document.getElementById('staffCreateForm').addEventListener('submit', async (event) => {
        event.preventDefault();
        await createUser('staff');
    });

    document.getElementById('adminCreateForm').addEventListener('submit', async (event) => {
        event.preventDefault();
        await createUser('admin');
    });
});

function handleUserTypeChange() {
    const userType = document.getElementById('userType').value;
    document.querySelectorAll('.user-form').forEach(form => form.style.display = 'none');
    if (userType === 'client') {
        document.getElementById('clientForm').style.display = 'block';
    } else if (userType === 'staff') {
        document.getElementById('staffForm').style.display = 'block';
    } else if (userType === 'admin') {
        document.getElementById('adminForm').style.display = 'block';
    }
}

async function createUser(type) {
    let apiUrl = '';
    let payload = {};

    if (type === 'client') {
        apiUrl = 'http://localhost:8090/auth/client/admin/create';
        payload = {
            name: document.getElementById('clientName').value,
            email: document.getElementById('clientEmail').value,
            phoneNumber: document.getElementById('clientPhoneNumber').value,
            clientAddressDTO: {
                streetAddress: document.getElementById('clientStreetAddress').value,
                city: document.getElementById('clientCity').value,
                state: document.getElementById('clientState').value,
                postalCode: document.getElementById('clientPostalCode').value
            }
        };
    } else if (type === 'staff') {
        apiUrl = 'http://localhost:8090/auth/staff/admin/create';
        payload = {
            name: document.getElementById('staffName').value,
            email: document.getElementById('staffEmail').value,
            phoneNumber: document.getElementById('staffPhoneNumber').value,
            staffAddressDTO: {
                streetAddress: document.getElementById('staffStreetAddress').value,
                city: document.getElementById('staffCity').value,
                state: document.getElementById('staffState').value,
                postalCode: document.getElementById('staffPostalCode').value
            }
        };
    } else if (type === 'admin') {
        apiUrl = 'http://localhost:8090/auth/admin/registerAdmin';
        payload = {
            name: document.getElementById('adminName').value,
            email: document.getElementById('adminEmail').value,
            password: document.getElementById('adminPassword').value
        };
    }

    const loadingOverlay = document.getElementById('loading');
    if (loadingOverlay) {
        loadingOverlay.style.display = 'flex';
    }

    try {
        const response = await fetch(apiUrl, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            const data = await response.json();
            let idField = '';

            if (type === 'client') {
                idField = `Client created successfully and the clientId is ${data.clientId}.`;
            } else if (type === 'staff') {
                idField = `Staff created successfully and the staffId is ${data.staffId}.`;
            } else if (type === 'admin') {
                idField = `Admin created successfully.`;
            }

            alert(idField);
            document.getElementById(`${type}CreateForm`).reset();
            handleUserTypeChange(); // Hide all forms and show user type selection
            document.getElementById('userType').value = ''; // Reset userType selection
        } else {
            alert(`Failed to create ${type}`);
        }
    } catch (error) {
        console.error(`Error creating ${type}:`, error);
        alert(`An error occurred while creating the ${type}`);
    } finally {
        if (loadingOverlay) {
            loadingOverlay.style.display = 'none';
        }
    }
}
