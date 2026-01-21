// modals.js - Modal management functions

// Function to open modal with dynamic content
export function openModal(type) {
  let modalContent = '';

  if (type === 'addDoctor') {
    modalContent = `
      <h2>Add Doctor</h2>
      <form id="addDoctorForm">
        <input type="text" id="doctorName" placeholder="Doctor Name" class="input-field" required>
        <select id="specialization" class="input-field select-dropdown" required>
          <option value="">Select Specialization</option>
          <option value="cardiologist">Cardiologist</option>
          <option value="dermatologist">Dermatologist</option>
          <option value="neurologist">Neurologist</option>
          <option value="pediatrician">Pediatrician</option>
          <option value="orthopedic">Orthopedic</option>
          <option value="gynecologist">Gynecologist</option>
          <option value="psychiatrist">Psychiatrist</option>
          <option value="dentist">Dentist</option>
          <option value="ophthalmologist">Ophthalmologist</option>
          <option value="ent">ENT Specialist</option>
          <option value="urologist">Urologist</option>
          <option value="oncologist">Oncologist</option>
          <option value="gastroenterologist">Gastroenterologist</option>
          <option value="general">General Physician</option>
        </select>
        <input type="email" id="doctorEmail" placeholder="Email" class="input-field" required>
        <input type="password" id="doctorPassword" placeholder="Password" class="input-field" required>
        <input type="text" id="doctorPhone" placeholder="Mobile No." class="input-field" required>
        <div class="availability-container">
          <label class="availabilityLabel">Select Availability:</label>
          <div class="checkbox-group">
            <label><input type="checkbox" name="availability" value="09:00-10:00"> 9:00 AM - 10:00 AM</label>
            <label><input type="checkbox" name="availability" value="10:00-11:00"> 10:00 AM - 11:00 AM</label>
            <label><input type="checkbox" name="availability" value="11:00-12:00"> 11:00 AM - 12:00 PM</label>
            <label><input type="checkbox" name="availability" value="12:00-13:00"> 12:00 PM - 1:00 PM</label>
          </div>
        </div>
        <button type="submit" class="dashboard-btn" id="saveDoctorBtn">Save</button>
      </form>
    `;
  } else if (type === 'patientLogin') {
    modalContent = `
      <h2>Patient Login</h2>
      <form id="patientLoginForm">
        <input type="email" id="patientEmail" placeholder="Email" class="input-field" required>
        <input type="password" id="patientPassword" placeholder="Password" class="input-field" required>
        <button type="submit" class="dashboard-btn" id="patientLoginBtn">Login</button>
      </form>
    `;
  } else if (type === 'patientSignup') {
    modalContent = `
      <h2>Patient Signup</h2>
      <form id="patientSignupForm">
        <input type="text" id="patientName" placeholder="Name" class="input-field" required>
        <input type="email" id="patientEmail" placeholder="Email" class="input-field" required>
        <input type="password" id="patientPassword" placeholder="Password" class="input-field" required>
        <input type="text" id="patientPhone" placeholder="Phone" class="input-field" required>
        <input type="text" id="patientAddress" placeholder="Address" class="input-field" required>
        <button type="submit" class="dashboard-btn" id="patientSignupBtn">Signup</button>
      </form>
    `;
  } else if (type === 'adminLogin') {
    modalContent = `
      <h2>Admin Login</h2>
      <form id="adminLoginForm">
        <input type="text" id="adminUsername" name="username" placeholder="Username" class="input-field" required>
        <input type="password" id="adminPassword" name="password" placeholder="Password" class="input-field" required>
        <button type="submit" class="dashboard-btn" id="adminLoginBtn">Login</button>
      </form>
    `;
  } else if (type === 'doctorLogin') {
    modalContent = `
      <h2>Doctor Login</h2>
      <form id="doctorLoginForm">
        <input type="text" id="doctorUsername" name="username" placeholder="Username" class="input-field" required>
        <input type="password" id="doctorPassword" name="password" placeholder="Password" class="input-field" required>
        <button type="submit" class="dashboard-btn" id="doctorLoginBtn">Login</button>
      </form>
    `;
  }

  // Set modal content and show modal
  const modalBody = document.getElementById('modal-body');
  const modal = document.getElementById('modal');

  if (modalBody && modal) {
    modalBody.innerHTML = modalContent;
    modal.style.display = 'block';

    // Setup close button
    const closeBtn = document.getElementById('closeModal');
    if (closeBtn) {
      closeBtn.onclick = () => {
        modal.style.display = 'none';
      };
    }

    // Setup form event listeners (functions are defined globally in other files)
    if (type === 'addDoctor' && window.adminAddDoctor) {
      const form = document.getElementById('addDoctorForm');
      if (form) {
        form.addEventListener('submit', window.adminAddDoctor);
      }
    }

    if (type === 'patientLogin' && window.patientLoginHandler) {
      const form = document.getElementById('patientLoginForm');
      if (form) {
        form.addEventListener('submit', window.patientLoginHandler);
      }
    }

    if (type === 'patientSignup' && window.signupPatient) {
      const form = document.getElementById('patientSignupForm');
      if (form) {
        form.addEventListener('submit', window.signupPatient);
      }
    }

    if (type === 'adminLogin' && window.adminLoginHandler) {
      const form = document.getElementById('adminLoginForm');
      if (form) {
        form.addEventListener('submit', window.adminLoginHandler);
      }
    }

    if (type === 'doctorLogin' && window.doctorLoginHandler) {
      const form = document.getElementById('doctorLoginForm');
      if (form) {
        form.addEventListener('submit', window.doctorLoginHandler);
      }
    }
  }
}

// Function to open specific modal by ID
export function openModalById(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) {
    modal.style.display = 'block';
  }
}

// Function to close specific modal by ID
export function closeModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) {
    modal.style.display = 'none';
  }
}

// Close modal when clicking outside
window.onclick = function(event) {
  const modal = document.getElementById('modal');
  if (event.target === modal) {
    modal.style.display = 'none';
  }

  // Close specific modals when clicking outside
  const modals = ['adminLoginModal', 'doctorLoginModal', 'patientLoginModal'];
  modals.forEach(modalId => {
    const modalElement = document.getElementById(modalId);
    if (event.target === modalElement) {
      modalElement.style.display = 'none';
    }
  });
};

// Make functions globally available
window.openModal = openModal;
window.openModalById = openModalById;
window.closeModal = closeModal;
