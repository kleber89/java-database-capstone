import { getAllAppointments } from './services/appointmentRecordService.js';
import { getPrescription } from './services/prescriptionServices.js';

let selectedDate = new Date().toISOString().split('T')[0]; // YYYY-MM-DD
let patientName = null;
const token = localStorage.getItem('token');

document.addEventListener('DOMContentLoaded', () => {
    renderContent();
    loadAppointments();

    // Search bar event
    document.getElementById('searchBar').addEventListener('input', (e) => {
        patientName = e.target.value.trim() || null;
        loadAppointments();
    });

    // Today button
    document.getElementById('todayButton').addEventListener('click', () => {
        selectedDate = new Date().toISOString().split('T')[0];
        document.getElementById('datePicker').value = selectedDate;
        loadAppointments();
    });

    // Date picker
    document.getElementById('datePicker').addEventListener('change', (e) => {
        selectedDate = e.target.value;
        loadAppointments();
    });

    // Set initial date
    document.getElementById('datePicker').value = selectedDate;

    // Modal close
    document.querySelector('.close').addEventListener('click', closeModal);
});

async function loadAppointments() {
    try {
        const appointments = await getAllAppointments(selectedDate, patientName || 'null', token);
        const tbody = document.getElementById('appointmentsTableBody');
        tbody.innerHTML = '';

        if (!appointments || appointments.length === 0) {
            tbody.innerHTML = '<tr><td colspan="4">No appointments found.</td></tr>';
            return;
        }

        appointments.forEach(appointment => {
            const row = createAppointmentRow(appointment);
            tbody.appendChild(row);
        });
    } catch (error) {
        console.error('Error loading appointments:', error);
        document.getElementById('appointmentsTableBody').innerHTML = '<tr><td colspan="4">Error loading appointments. Try again later.</td></tr>';
    }
}

function createAppointmentRow(appointment) {
    const tr = document.createElement('tr');
    tr.innerHTML = `
        <td>${appointment.patientName}</td>
        <td>${appointment.date} ${appointment.time}</td>
        <td>${appointment.status || 'Scheduled'}</td>
        <td>
            <button class="view-prescriptions-btn" data-appointment-id="${appointment.id}">View Prescriptions</button>
        </td>
    `;

    // Attach event listener for viewing prescriptions
    tr.querySelector('.view-prescriptions-btn').addEventListener('click', async () => {
        await viewPrescriptions(appointment.id);
    });

    return tr;
}

async function viewPrescriptions(appointmentId) {
    try {
        const prescription = await getPrescription(appointmentId, token);
        const content = document.getElementById('prescriptionContent');
        if (prescription && prescription.details) {
            content.innerHTML = `<pre>${JSON.stringify(prescription.details, null, 2)}</pre>`;
        } else {
            content.innerHTML = '<p>No prescriptions found for this appointment.</p>';
        }
        document.getElementById('prescriptionModal').style.display = 'block';
    } catch (error) {
        console.error('Error fetching prescriptions:', error);
        alert('Error loading prescriptions');
    }
}

function closeModal() {
    document.getElementById('prescriptionModal').style.display = 'none';
}
