import { getAllAppointments } from './services/appointmentRecordService.js';
import { createPatientRow } from './components/patientRows.js';

// Inicializar Variables Globales
const patientTableBody = document.getElementById('patientTableBody');
let selectedDate = new Date().toISOString().split('T')[0];
const token = localStorage.getItem('token');
let patientName = null;

// Configurar la Funcionalidad de la Barra de Búsqueda
document.getElementById('searchBar').addEventListener('input', (e) => {
    patientName = e.target.value.trim() || null;
    loadAppointments();
});

// Vincular Oyentes de Eventos a los Controles de Filtro
document.getElementById('todayButton').addEventListener('click', () => {
    selectedDate = new Date().toISOString().split('T')[0];
    document.getElementById('datePicker').value = selectedDate;
    loadAppointments();
});

document.getElementById('datePicker').addEventListener('change', (e) => {
    selectedDate = e.target.value;
    loadAppointments();
});

// Definir la Función loadAppointments()
async function loadAppointments() {
    try {
        const appointments = await getAllAppointments(selectedDate, patientName, token);
        patientTableBody.innerHTML = '';

        if (!appointments || appointments.length === 0) {
            patientTableBody.innerHTML = '<tr><td colspan="4">No se encontraron citas para hoy</td></tr>';
            return;
        }

        appointments.forEach(appointment => {
            const patientDetails = appointment.patient; // assuming appointment has patient object
            const row = createPatientRow(patientDetails);
            patientTableBody.appendChild(row);
        });
    } catch (error) {
        console.error('Error loading appointments:', error);
        patientTableBody.innerHTML = '<tr><td colspan="4">Error loading appointments. Try again later.</td></tr>';
    }
}

// Renderizado Inicial al Cargar la Página
document.addEventListener('DOMContentLoaded', () => {
    loadAppointments();
});
