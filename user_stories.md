# User Story Template

**Title:**
_As a [user role], I want [feature/goal], so that [reason]._

**Acceptance Criteria:**
1. [Criteria 1]
2. [Criteria 2]
3. [Criteria 3]

**Priority:** [High/Medium/Low]
**Story Points:** [Estimated Effort in Points]
**Notes:**

## Admin stories

**Title:**
_As an admin, I can log into the portal with my username and password, so that I can manage the platform securely._

**Acceptance Criteria:**
1. Admins can access a secure login form where username and password are required.
2. Correct credentials authenticate the admin and redirect to the Admin Dashboard.
3. Incorrect credentials show a clear error and do not grant access.
4. Login attempts are logged and rate-limited to prevent brute-force attacks.

**Priority:** High
**Story Points:** 3
**Notes:**
- Reuse or extend the existing authentication service (JWT or session-based).
- Add unit and integration tests for successful and failed logins.

**Title:**
_As an admin, I can log out of the portal to protect system access._

**Acceptance Criteria:**
1. A logout control is available from the Admin Dashboard and app navigation.
2. Clicking logout terminates the session and redirects to the login page.
3. Server-side session invalidation ensures tokens/cookies cannot be reused.
4. After logout, protected pages require re-authentication.

**Priority:** High
**Story Points:** 1
**Notes:**
- Verify logout behavior in different browsers and mobile views.
- Add tests to confirm session invalidation and access restrictions.

**Title:**
_As an admin, I can add doctors to the portal so that new providers can be registered and managed._

**Acceptance Criteria:**
1. An 'Add Doctor' form is available from the Admin Dashboard.
2. The form requires full name, specialty, email, phone, and license number and validates inputs.
3. Submitting the form creates a new doctor record and displays it in the doctors list.
4. Success and error messages are displayed appropriately; duplicate or invalid inputs are rejected.

**Priority:** Medium
**Story Points:** 5
**Notes:**
- Implement server-side validation and optional invitation email to the doctor.
- Add tests for creation, validation, and UI feedback.

**Title:**
_As an admin, I can delete a doctor's profile from the portal so that outdated or incorrect profiles can be removed._

**Acceptance Criteria:**
1. Admins can select a doctor and trigger a 'Delete' action from the doctors list.
2. A confirmation dialog is shown before deletion is performed.
3. Deleting a doctor removes the profile; handling of related data (appointments) follows policy (prevent or cascade) and is documented.
4. Deletion events are logged for audit purposes and only allowed for authorized admin roles.

**Priority:** Medium
**Story Points:** 3
**Notes:**
- Define policy for related appointments (e.g., reassign, cancel, or block deletion).
- Add tests for permission checks and deletion flows.

**Title:**
_As an admin, I can run a stored procedure in the MySQL CLI to get the number of appointments per month so that usage statistics can be tracked._

**Acceptance Criteria:**
1. An SQL script creating a stored procedure (e.g., `sp_appointments_per_month`) is added under `db/scripts/`.
2. The procedure returns rows with `month` and `appointment_count` for each month.
3. README or documentation includes instructions to run the procedure via MySQL CLI (e.g., `CALL sp_appointments_per_month();`).
4. The procedure is tested against sample data and returns accurate counts.

**Priority:** Medium
**Story Points:** 2
**Notes:**
- Optionally add an SQL view or an API endpoint to expose the same data for dashboards.
- Include example output in docs and a script to populate test data.

## Patient Stories

**Title:**
_As a patient, I can view a list of doctors without logging in, so that I can explore options before registering._

**Acceptance Criteria:**
1. A publicly accessible page lists doctors with name and specialty.
2. Each doctor entry shows basic contact info and a short bio.
3. Patients can filter/sort by specialty and search by name or location.
4. The page does not expose any private information or booking controls that require login.

**Priority:** Medium
**Story Points:** 2
**Notes:**
- Consider adding pagination and lightweight caching for performance.
- Use the existing doctors API or provide a read-only public endpoint.

**Title:**
_As a patient, I can sign up using my email and password, so that I can book appointments._

**Acceptance Criteria:**
1. A registration form collects email, password, full name, and optional phone number.
2. Email is validated and a verification email is sent upon registration.
3. Passwords follow strength rules and are stored securely (hashed).
4. After successful signup and verification, the patient can log in and start booking.

**Priority:** High
**Story Points:** 3
**Notes:**
- Reuse authentication components already present in the system.
- Add unit/integration tests for signup and email verification flows.

**Title:**
_As a patient, I can log into the portal, so that I can manage my bookings._

**Acceptance Criteria:**
1. Patients can log in using email and password through a secure form.
2. Successful login redirects the patient to their dashboard or appointments page.
3. Invalid credentials show a clear error and do not grant access.
4. Sessions are secure and protected against common web attacks (CSRF/XSS).

**Priority:** High
**Story Points:** 2
**Notes:**
- Ensure rate-limiting and logging of login attempts.
- Add tests for login success and failure cases.

**Title:**
_As a patient, I can log out of the portal, so that my account is secured when I'm finished._

**Acceptance Criteria:**
1. A visible logout action is available on the patient's dashboard.
2. Clicking logout terminates the session and redirects to the public homepage or login page.
3. Server-side session invalidation prevents reuse of tokens/cookies after logout.

**Priority:** High
**Story Points:** 1
**Notes:**
- Add tests to confirm session invalidation across browsers and devices.

**Title:**
_As a patient, I can log in and book an hour-long appointment, so that I can consult with a doctor._

**Acceptance Criteria:**
1. Logged-in patients can select a doctor, date, and a 60-minute time slot to book an appointment.
2. The system shows available time slots (no overlapping bookings) and prevents double-booking.
3. Upon booking, an appointment record is created with confirmation shown and an email sent to the patient.
4. The appointment appears in the patient's upcoming appointments list.

**Priority:** High
**Story Points:** 5
**Notes:**
- Validate timezone handling and edge cases (end-of-day slots).
- Add tests for concurrency to ensure slots aren’t double-booked.

**Title:**
_As a patient, I can view my upcoming appointments, so that I can prepare accordingly._

**Acceptance Criteria:**
1. Logged-in patients see a list of upcoming appointments with date, time, doctor, and location/details.
2. Appointments are sorted chronologically and show status (confirmed, pending, canceled).
3. Each appointment has actions where applicable (e.g., view details, cancel within policy window).
4. The list updates when a new appointment is booked or canceled.

**Priority:** Medium
**Story Points:** 2
**Notes:**
- Consider adding calendar export (ICS) or reminders via email/SMS.
- Add tests for listing, status changes, and cancellation flows.

**Title:**
_As a doctor, I can log into the portal, so that I can manage my appointments._

**Acceptance Criteria:**
1. Doctors can log in using a secure form with email/username and password.
2. Successful login redirects the doctor to their appointment dashboard/calendar.
3. Invalid credentials show an appropriate error and do not grant access.
4. Login attempts are logged and subject to rate-limiting to prevent abuse.

**Priority:** High
**Story Points:** 2
**Notes:**
- Reuse existing authentication and session handling; add tests for doctor login flows.

**Title:**
_As a doctor, I can log out of the portal to protect my data._

**Acceptance Criteria:**
1. A visible logout action is available from the doctor's dashboard and navigation.
2. Clicking logout terminates the session and redirects to the login or public page.
3. Server-side session invalidation ensures tokens/cookies cannot be reused.

**Priority:** High
**Story Points:** 1
**Notes:**
- Add tests to confirm session invalidation across browsers/devices and mobile.

**Title:**
_As a doctor, I can view my appointment calendar to stay organized._

**Acceptance Criteria:**
1. The calendar shows scheduled appointments by day/week/month with time and patient name.
2. Doctors can navigate between dates and filter by appointment status (confirmed, pending, canceled).
3. Clicking an appointment shows details and quick actions (e.g., mark as completed, cancel).

**Priority:** Medium
**Story Points:** 3
**Notes:**
- Consider using a calendar UI component (FullCalendar or similar) and lazy-loading events.

**Title:**
_As a doctor, I can mark my unavailability so that patients see only available slots._

**Acceptance Criteria:**
1. Doctors can add unavailable time ranges (one-off or recurring) via the calendar or a form.
2. Unavailable slots are excluded from patient-facing availability and booking flows.
3. Doctors can edit or remove unavailability entries and see them on their calendar.

**Priority:** Medium
**Story Points:** 3
**Notes:**
- Ensure timezones are respected and provide conflict warnings for existing appointments.

**Title:**
_As a doctor, I can update my profile with specialization and contact information so that patients have up-to-date information._

**Acceptance Criteria:**
1. Doctors can edit profile fields (full name, specialization, contact email/phone, bio, photo).
2. Changes are validated and saved to the doctor's profile; updated information appears publicly where applicable.
3. Profile edits are auditable and require authentication.

**Priority:** Medium
**Story Points:** 2
**Notes:**
- Allow optional verification for license number or certifications if applicable.

**Title:**
_As a doctor, I can view patient details for upcoming appointments so that I can be prepared._

**Acceptance Criteria:**
1. For each upcoming appointment, doctors can view relevant patient info (name, age, contact, brief medical notes) respecting privacy rules.
2. Sensitive data is shown only when necessary and with appropriate permissions/auditing.
3. Doctors can read past appointment notes and add new notes to the appointment record.

**Priority:** High
**Story Points:** 3
**Notes:**
- Ensure compliance with privacy requirements and limit data exposure to consented fields.
