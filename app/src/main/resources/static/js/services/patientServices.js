// patientServices
import { API_BASE_URL } from "../config/config.js";
const PATIENT_API = API_BASE_URL + '/patient'

// For creating a patient in db
export async function patientSignup(data) {
  try {
    const response = await fetch(`${PATIENT_API}`,
      {
        method: "POST",
        headers: {
          "Content-type": "application/json"
        },
        body: JSON.stringify(data)
      }
    );
    const result = await response.json();
    if (!response.ok) {
      throw new Error(result.message);
    }
    return { success: response.ok, message: result.message }
  }
  catch (error) {
    console.error("Error :: patientSignup :: ", error)
    return { success: false, message: error.message }
  }
}

// For logging in patient
export async function patientLogin(data) {
  console.log("patientLogin :: ", data)
  return await fetch(`${PATIENT_API}/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  });
}

// For getting all patients (admin functionality)
export async function getPatients(token) {
  try {
    const response = await fetch(`${PATIENT_API}?token=${token}`);
    const data = await response.json();
    if (response.ok) {
      return data.patients || [];
    }
    return [];
  } catch (error) {
    console.error("Error fetching patients:", error);
    return [];
  }
}

// For getting a single patient by ID
export async function getPatientById(patientId, token) {
  try {
    const response = await fetch(`${PATIENT_API}/${patientId}?token=${token}`);
    const data = await response.json();
    if (response.ok) {
      return data.patient;
    }
    return null;
  } catch (error) {
    console.error("Error fetching patient:", error);
    return null;
  }
}

// For getting patient data (name, id, etc). Used in booking appointments
export async function getPatientData(token) {
  try {
    const response = await fetch(`${PATIENT_API}/${token}`);
    const data = await response.json();
    if (response.ok) return data.patient;
    return null;
  } catch (error) {
    console.error("Error fetching patient details:", error);
    return null;
  }
}

// For updating patient information
export async function updatePatient(patientId, patientData, token) {
  try {
    const response = await fetch(`${PATIENT_API}/${patientId}?token=${token}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(patientData)
    });
    const data = await response.json();
    return {
      success: response.ok,
      message: data.message
    };
  } catch (error) {
    console.error("Error updating patient:", error);
    return { success: false, message: "Error updating patient" };
  }
}

// For deleting a patient
export async function deletePatient(patientId, token) {
  try {
    const response = await fetch(`${PATIENT_API}/${patientId}?token=${token}`, {
      method: "DELETE"
    });
    const data = await response.json();
    return {
      success: response.ok,
      message: data.message
    };
  } catch (error) {
    console.error("Error deleting patient:", error);
    return { success: false, message: "Error deleting patient" };
  }
}

// For filtering patients by name, email, or other criteria
export async function filterPatients(name, email, token) {
  try {
    const params = new URLSearchParams();
    if (name) params.append('name', name);
    if (email) params.append('email', email);
    const response = await fetch(`${PATIENT_API}/filter?${params}&token=${token}`);
    if (response.ok) {
      const data = await response.json();
      return data.patients || [];
    } else {
      console.error("Error filtering patients:", response.statusText);
      return [];
    }
  } catch (error) {
    console.error("Error filtering patients:", error);
    return [];
  }
}

// For searching patients with a general query
export async function searchPatients(query, token) {
  try {
    const params = new URLSearchParams();
    params.append('q', query);
    const response = await fetch(`${PATIENT_API}/search?${params}&token=${token}`);
    if (response.ok) {
      const data = await response.json();
      return data.patients || [];
    } else {
      console.error("Error searching patients:", response.statusText);
      return [];
    }
  } catch (error) {
    console.error("Error searching patients:", error);
    return [];
  }
}

// the Backend API for fetching the patient record(visible in Doctor Dashboard) and Appointments (visible in Patient Dashboard) are same based on user(patient/doctor).
export async function getPatientAppointments(id, token, user) {
  try {
    const response = await fetch(`${PATIENT_API}/${id}/${user}/${token}`);
    const data = await response.json();
    console.log(data.appointments)
    if (response.ok) {
      return data.appointments;
    }
    return null;
  }
  catch (error) {
    console.error("Error fetching patient details:", error);
    return null;
  }
}

export async function filterAppointments(condition, name, token) {
  try {
    const response = await fetch(`${PATIENT_API}/filter/${condition}/${name}/${token}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (response.ok) {
      const data = await response.json();
      return data;

    } else {
      console.error("Failed to fetch doctors:", response.statusText);
      return { appointments: [] };

    }
  } catch (error) {
    console.error("Error:", error);
    alert("Something went wrong!");
    return { appointments: [] };
  }
}
