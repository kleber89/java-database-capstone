import { getDoctors, filterDoctors, saveDoctor, deleteDoctor } from './services/doctorServices.js';

// Function to create a doctor card
function createDoctorCard(doctor) {
    const card = document.createElement('div');
    card.className = 'doctor-card';
    card.innerHTML = `
        <h3>${doctor.name}</h3>
        <p>Specialty: ${doctor.specialty}</p>
        <p>Email: ${doctor.email}</p>
        <p>Phone: ${doctor.phone}</p>
        <p>Available Times: ${doctor.availableTimes}</p>
        <button class="delete-btn" data-id="${doctor.id}">Delete</button>
    `;
    return card;
}

// Load all doctors on page load
document.addEventListener('DOMContentLoaded', () => {
    loadDoctorCards();

    // Attach event listeners
    document.getElementById('searchBar').addEventListener('input', filterDoctorsOnChange);
    document.getElementById('timeFilter').addEventListener('change', filterDoctorsOnChange);
    document.getElementById('specialtyFilter').addEventListener('change', filterDoctorsOnChange);

    // Add doctor button
    document.getElementById('addDoctorBtn').addEventListener('click', () => openModal('addDoctor'));

    // Modal close
    document.querySelector('.close').addEventListener('click', closeModal);

    // Form submit
    document.getElementById('addDoctorForm').addEventListener('submit', adminAddDoctor);
});

async function loadDoctorCards() {
    try {
        const doctors = await getDoctors();
        renderDoctorCards(doctors);
    } catch (error) {
        console.error('Error loading doctors:', error);
    }
}

function renderDoctorCards(doctors) {
    const content = document.getElementById('content');
    content.innerHTML = '';
    doctors.forEach(doctor => {
        const card = createDoctorCard(doctor);
        content.appendChild(card);
    });

    // Attach delete event listeners
    document.querySelectorAll('.delete-btn').forEach(btn => {
        btn.addEventListener('click', async (e) => {
            const doctorId = e.target.dataset.id;
            const token = localStorage.getItem('token');
            if (!token) {
                alert('No authentication token found');
                return;
            }
            if (confirm('Are you sure you want to delete this doctor?')) {
                try {
                    const result = await deleteDoctor(doctorId, token);
                    if (result.success) {
                        alert('Doctor deleted successfully');
                        loadDoctorCards();
                    } else {
                        alert('Error deleting doctor: ' + result.message);
                    }
                } catch (error) {
                    alert('Error deleting doctor');
                }
            }
        });
    });
}

async function filterDoctorsOnChange() {
    const name = document.getElementById('searchBar').value.trim() || null;
    const time = document.getElementById('timeFilter').value || null;
    const specialty = document.getElementById('specialtyFilter').value || null;

    try {
        const result = await filterDoctors(name, time, specialty);
        if (result.doctors && result.doctors.length > 0) {
            renderDoctorCards(result.doctors);
        } else {
            document.getElementById('content').innerHTML = '<p>No doctors found with the given filters.</p>';
        }
    } catch (error) {
        alert('Error filtering doctors');
    }
}

async function adminAddDoctor(event) {
    event.preventDefault();
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const phone = document.getElementById('phone').value;
    const password = document.getElementById('password').value;
    const specialty = document.getElementById('specialty').value;
    const availableTimes = document.getElementById('availableTimes').value;

    const token = localStorage.getItem('token');
    if (!token) {
        alert('No authentication token found');
        return;
    }

    const doctor = { name, email, phone, password, specialty, availableTimes };

    try {
        const result = await saveDoctor(doctor, token);
        if (result.success) {
            alert('Doctor added successfully');
            closeModal();
            loadDoctorCards(); // Reload the list
        } else {
            alert('Error adding doctor: ' + result.message);
        }
    } catch (error) {
        alert('Error adding doctor');
    }
}

function openModal(type) {
    if (type === 'addDoctor') {
        document.getElementById('modal').style.display = 'block';
    }
}

function closeModal() {
    document.getElementById('modal').style.display = 'none';
}
