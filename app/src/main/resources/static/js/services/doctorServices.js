import { API_BASE_URL } from '../config/config.js';

const DOCTOR_API = `${API_BASE_URL}/doctors`;

export async function getDoctors() {
    try {
        const response = await fetch(DOCTOR_API);
        const data = await response.json();
        return data.doctors || [];
    } catch (error) {
        console.error('Error fetching doctors:', error);
        return [];
    }
}

export async function getDoctorById(doctorId) {
    try {
        const response = await fetch(`${DOCTOR_API}/${doctorId}`);
        if (response.ok) {
            const data = await response.json();
            return data.doctor;
        } else {
            console.error('Error fetching doctor:', response.statusText);
            return null;
        }
    } catch (error) {
        console.error('Error fetching doctor:', error);
        return null;
    }
}

export async function saveDoctor(doctor, token) {
    try {
        const response = await fetch(`${DOCTOR_API}?token=${token}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(doctor)
        });
        const data = await response.json();
        return {
            success: response.ok,
            message: data.message
        };
    } catch (error) {
        console.error('Error saving doctor:', error);
        return { success: false, message: 'Error saving doctor' };
    }
}

export async function updateDoctor(doctorId, doctor, token) {
    try {
        const response = await fetch(`${DOCTOR_API}/${doctorId}?token=${token}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(doctor)
        });
        const data = await response.json();
        return {
            success: response.ok,
            message: data.message
        };
    } catch (error) {
        console.error('Error updating doctor:', error);
        return { success: false, message: 'Error updating doctor' };
    }
}

export async function deleteDoctor(doctorId, token) {
    try {
        const response = await fetch(`${DOCTOR_API}/${doctorId}?token=${token}`, {
            method: 'DELETE'
        });
        const data = await response.json();
        return {
            success: response.ok,
            message: data.message
        };
    } catch (error) {
        console.error('Error deleting doctor:', error);
        return { success: false, message: 'Error deleting doctor' };
    }
}

export async function filterDoctors(name, time, specialty) {
    try {
        const params = new URLSearchParams();
        if (name) params.append('name', name);
        if (time) params.append('time', time);
        if (specialty) params.append('specialty', specialty);
        const response = await fetch(`${DOCTOR_API}/filter?${params}`);
        if (response.ok) {
            const data = await response.json();
            return data;
        } else {
            console.error('Error filtering doctors:', response.statusText);
            return { doctors: [] };
        }
    } catch (error) {
        console.error('Error filtering doctors:', error);
        alert('Error filtering doctors');
        return { doctors: [] };
    }
}

export async function searchDoctors(query) {
    try {
        const params = new URLSearchParams();
        params.append('q', query);
        const response = await fetch(`${DOCTOR_API}/search?${params}`);
        if (response.ok) {
            const data = await response.json();
            return data.doctors || [];
        } else {
            console.error('Error searching doctors:', response.statusText);
            return [];
        }
    } catch (error) {
        console.error('Error searching doctors:', error);
        return [];
    }
}
