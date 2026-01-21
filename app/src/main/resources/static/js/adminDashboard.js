import { openModal } from '../components/modals.js';
import { getDoctors, filterDoctors, saveDoctor } from './services/doctorServices.js';
import { createDoctorCard } from './components/doctorCard.js';

// Vinculación de Eventos
document.getElementById('addDocBtn').addEventListener('click', () => {
    openModal('addDoctor');
});

// Cargar Tarjetas de Médicos al Cargar la Página
document.addEventListener('DOMContentLoaded', () => {
    loadDoctorCards();

    // Implementar Lógica de Búsqueda y Filtrado
    document.getElementById("searchBar").addEventListener("input", filterDoctorsOnChange);
    document.getElementById("filterTime").addEventListener("change", filterDoctorsOnChange);
    document.getElementById("filterSpecialty").addEventListener("change", filterDoctorsOnChange);
});

async function loadDoctorCards() {
    const doctors = await getDoctors();
    const contentDiv = document.getElementById("content");
    contentDiv.innerHTML = "";
    renderDoctorCards(doctors);
}

function renderDoctorCards(doctors) {
    const contentDiv = document.getElementById("content");
    doctors.forEach(doctor => {
        const card = createDoctorCard(doctor);
        contentDiv.appendChild(card);
    });
}

async function filterDoctorsOnChange() {
    const name = document.getElementById('searchBar').value.trim() || null;
    const time = document.getElementById('filterTime').value || null;
    const specialty = document.getElementById('filterSpecialty').value || null;

    const doctors = await filterDoctors(name, time, specialty);
    if (doctors.length > 0) {
        renderDoctorCards(doctors);
    } else {
        document.getElementById('content').innerHTML = '<p>No se encontraron médicos</p>';
    }
}

// Manejar el Modal de Agregar Médico
async function adminAddDoctor(event) {
    event.preventDefault();
    const name = document.getElementById('name').value;
    const specialty = document.getElementById('specialty').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const phone = document.getElementById('phone').value;
    const availableTimes = Array.from(document.querySelectorAll('input[name="availableTimes"]:checked')).map(cb => cb.value);

    const token = localStorage.getItem('token');
    if (!token) {
        alert('No authentication token found');
        return;
    }

    const doctor = { name, specialty, email, password, phone, availableTimes };

    const result = await saveDoctor(doctor, token);
    if (result.success) {
        alert('Doctor added successfully');
        // close modal, refresh list
        document.getElementById('modal').style.display = 'none';
        loadDoctorCards();
    } else {
        alert('Error adding doctor: ' + result.message);
    }
}
