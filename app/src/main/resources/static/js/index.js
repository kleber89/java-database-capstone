// index.js - Landing page logic for role selection and login handling

import { API_BASE_URL } from './config.js';

// Role selection function
function selectRoleType(role) {
    // Store the selected role in localStorage
    localStorage.setItem('selectedRole', role);

    // Open the appropriate login modal
    switch(role) {
        case 'admin':
            openModalById('adminLoginModal');
            break;
        case 'doctor':
            openModalById('doctorLoginModal');
            break;
        case 'patient':
            openModalById('patientLoginModal');
            break;
        default:
            console.error('Invalid role selected:', role);
    }
}

// Admin login handler
async function adminLoginHandler(event) {
    event.preventDefault();

    const username = document.getElementById('adminUsername').value;
    const password = document.getElementById('adminPassword').value;

    try {
        const response = await fetch(`${API_BASE_URL}/admin/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password }),
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('authToken', data.token);
            localStorage.setItem('userRole', 'admin');
            // Redirect to admin dashboard
            window.location.href = '/admin/dashboard';
        } else {
            alert('Invalid admin credentials');
        }
    } catch (error) {
        console.error('Admin login error:', error);
        alert('Login failed. Please try again.');
    }
}

// Doctor login handler
async function doctorLoginHandler(event) {
    event.preventDefault();

    const username = document.getElementById('doctorUsername').value;
    const password = document.getElementById('doctorPassword').value;

    try {
        const response = await fetch(`${API_BASE_URL}/doctor/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password }),
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('authToken', data.token);
            localStorage.setItem('userRole', 'doctor');
            // Redirect to doctor dashboard
            window.location.href = '/doctor/dashboard';
        } else {
            alert('Invalid doctor credentials');
        }
    } catch (error) {
        console.error('Doctor login error:', error);
        alert('Login failed. Please try again.');
    }
}

// Patient login handler
async function patientLoginHandler(event) {
    event.preventDefault();

    const username = document.getElementById('patientUsername').value;
    const password = document.getElementById('patientPassword').value;

    try {
        const response = await fetch(`${API_BASE_URL}/patient/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password }),
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('authToken', data.token);
            localStorage.setItem('userRole', 'patient');
            // Redirect to patient dashboard
            window.location.href = '/patient/dashboard';
        } else {
            alert('Invalid patient credentials');
        }
    } catch (error) {
        console.error('Patient login error:', error);
        alert('Login failed. Please try again.');
    }
}

// Initialize the page
document.addEventListener('DOMContentLoaded', function() {
    // Add event listeners for login forms
    const adminLoginForm = document.getElementById('adminLoginForm');
    const doctorLoginForm = document.getElementById('doctorLoginForm');
    const patientLoginForm = document.getElementById('patientLoginForm');

    if (adminLoginForm) {
        adminLoginForm.addEventListener('submit', adminLoginHandler);
    }
    if (doctorLoginForm) {
        doctorLoginForm.addEventListener('submit', doctorLoginHandler);
    }
    if (patientLoginForm) {
        patientLoginForm.addEventListener('submit', patientLoginHandler);
    }
});