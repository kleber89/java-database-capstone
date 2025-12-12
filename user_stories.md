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
- [Additional information or edge cases]

-----
# Admin User Stories

## Story 1: Admin Login
**Title:**
_As an administrator, I want to log in to the portal with my username and password, so that I can securely manage the platform._

**Acceptance Criteria:**
1. Admin can access a secure login page.
2. Successful authentication redirects to an admin dashboard.
3. Failed authentication shows a clear error and does not reveal sensitive info.

**Priority:** High
**Story Points:** 3
**Notes:**
- Use Spring Security for authentication; store hashed passwords and enforce account lockout after repeated failed attempts.

## Story 2: Admin Logout
**Title:**
_As an administrator, I want to log out of the portal, so that access to the system is protected when I'm not using it._

**Acceptance Criteria:**
1. Logout invalidates the session and any auth tokens.
2. After logout, accessing protected endpoints redirects to the login page.
3. There is a clear confirmation or feedback that logout succeeded.

**Priority:** High
**Story Points:** 1
**Notes:**
- Ensure logout endpoint is CSRF-protected and session invalidation is performed server-side.

## Story 3: Add Doctor
**Title:**
_As an administrator, I want to add doctors to the portal, so that I can register new providers to the system._

**Acceptance Criteria:**
1. Admin can open a form to add a doctor's name, specialty, contact info, and role.
2. The system validates required fields and rejects incorrect entries.
3. Upon successful creation, the doctor appears in the doctor list and receives a default (or generated) login and credentials if required.

**Priority:** Medium
**Story Points:** 5
**Notes:**
- Consider sending a welcome email with account instructions and enforcing role-based access control for doctor accounts.

## Story 4: Delete Doctor
**Title:**
_As an administrator, I want to delete a doctor's profile from the portal, so that outdated or erroneous records can be removed._

**Acceptance Criteria:**
1. Admin selects a doctor and confirms a delete operation using a modal or separate confirmation step.
2. System verifies the action and either performs a soft-delete or permanent deletion according to the policy.
3. Associated data (appointments/prescriptions) either cascade, reassign, or are preserved depending on data retention rules and constraints.

**Priority:** Medium
**Story Points:** 3
**Notes:**
- Prefer soft-deletes to preserve audit trail. Implement role checks and audit logging for delete operations.

## Story 5: Run Stored Procedure for Appointment Stats
**Title:**
_As an administrator, I want to run a stored procedure in the MySQL CLI to get the number of appointments per month, so that I can track usage statistics and trends._

**Acceptance Criteria:**
1. A documented stored procedure exists and is deployed in the MySQL DB.
2. Admin can execute the stored procedure via an admin area in the UI or via CLI, and results are returned in a tabular format.
3. Results include month, year, total appointments, and optionally segmented breakdowns (doctor, specialty).

**Priority:** Low
**Story Points:** 2
**Notes:**
- If executed by the UI, ensure permissions prevents non-admins from running it. Consider exporting results to CSV and scheduling a periodic job to generate the same metrics.

-----
# Patient User Stories

## Story 1: View list of doctors without logging in
**Title:**
_As a patient, I want to view a list of doctors without logging in, so that I can explore options before registering._

**Acceptance Criteria:**
1. A public page lists doctors with name, specialty and location.
2. Patients can search or filter by specialty and name.
3. No sensitive information or booking options are shown to anonymous users.

**Priority:** Medium
**Story Points:** 2
**Notes:**
- Implement pagination and rate-limiting to prevent scraping or abuse.

## Story 2: Register using email and password
**Title:**
_As a patient, I want to register using my email and password, so that I can book appointments._

**Acceptance Criteria:**
1. Registration form collects email, password and basic profile fields (name, optional phone).
2. Email format and password strength are validated; email uniqueness is enforced.
3. User receives confirmation via UI or email upon successful registration.

**Priority:** High
**Story Points:** 3
**Notes:**
- Use email verification and securely hash passwords (e.g., BCrypt).

## Story 3: Log in to manage bookings
**Title:**
_As a patient, I want to log in to the portal, so that I can view and manage my bookings._

**Acceptance Criteria:**
1. Patients can authenticate with email and password.
2. Successful login redirects to a dashboard showing bookings and profile.
3. Failed attempts produce an informative error and protections against brute-force attacks.

**Priority:** High
**Story Points:** 2
**Notes:**
- Implement login attempt throttling and secure session/JWT handling with expiration.

## Story 4: Log out to secure account
**Title:**
_As a patient, I want to log out of the portal, so that my account is secured on shared devices._

**Acceptance Criteria:**
1. Patients can log out from any protected page.
2. Logout invalidates the session and prevents access to protected routes without re-authentication.
3. The UI confirms successful logout.

**Priority:** High
**Story Points:** 1
**Notes:**
- Ensure CSRF protections and client-side token cleanup on logout.

## Story 5: Log in and book a one-hour appointment
**Title:**
_As a patient, I want to log in and book a one-hour appointment with a doctor, so that I can schedule my consultation._

**Acceptance Criteria:**
1. Patients can select a doctor, date and an available 1-hour timeslot and confirm the booking.
2. The system validates availability and prevents double-booking of the same timeslot.
3. Reservation confirmation is sent by email and appears in the patient's booking history.

**Priority:** High
**Story Points:** 5
**Notes:**
- Support cancellation and rescheduling policies; consider business rules for adjacent time blocking.

## Story 6: View my upcoming appointments
**Title:**
_As a patient, I want to view my upcoming appointments so that I can prepare properly._

**Acceptance Criteria:**
1. The dashboard lists all future appointments with date, time, doctor and location.
2. Upcoming appointments are shown first and include links to details and cancellation when allowed.
3. If no appointments exist, the UI displays a friendly message with a call to action to book.

**Priority:** High
**Story Points:** 2
**Notes:**
- Optionally allow exporting or calendar sync and include reminder notifications.
  
--------
# Doctor User Stories

## Story 1: Doctor login
**Title:**
_As a doctor, I want to log in to the portal, so that I can view and manage my appointments._

**Acceptance Criteria:**
1. Doctors can authenticate with their email/username and password.
2. Successful login redirects to the doctor dashboard with appointment summary.
3. Failed login attempts are handled with appropriate messages and rate limiting.

**Priority:** High
**Story Points:** 2
**Notes:**
- Use secure session management and include device/session listing for doctors.

## Story 2: Doctor logout
**Title:**
_As a doctor, I want to log out of the portal, so that my account and patient data are protected._

**Acceptance Criteria:**
1. Doctors can log out from any protected page.
2. Logout invalidates the server session and clears tokens client-side.
3. Confirmation feedback is shown and protected pages require re-authentication.

**Priority:** High
**Story Points:** 1
**Notes:**
- Include CSRF protection and audit logging for logout events.

## Story 3: View appointment calendar
**Title:**
_As a doctor, I want to view my appointment calendar, so that I can stay organized and plan my availability._

**Acceptance Criteria:**
1. The doctor dashboard shows a calendar with all appointments by day/week/month.
2. Appointments display patient name, time, and appointment type.
3. Doctors can filter or navigate the calendar to different dates.

**Priority:** High
**Story Points:** 3
**Notes:**
- Provide local timezone handling and quick links to appointment details.

## Story 4: Mark unavailability
**Title:**
_As a doctor, I want to mark my unavailability for certain dates/times, so that I'm not shown as bookable for those slots._

**Acceptance Criteria:**
1. Doctors can select date ranges or specific times to mark as unavailable via the calendar or a form.
2. Events marked as unavailable are prevented from being booked by patients and reflect on the calendar.
3. Doctors can remove or edit unavailability entries and changes are audited.

**Priority:** Medium
**Story Points:** 3
**Notes:**
- Support recurring unavailability (e.g., every Friday) and notify affected patients when removing an unavailable slot.

## Story 5: Update profile (specialty & contact info)
**Title:**
_As a doctor, I want to update my profile with specialty and contact information, so that patients see accurate details._

**Acceptance Criteria:**
1. A profile edit page allows doctors to update specialty, contact info, and office hours.
2. Changes are validated and saved; a confirmation message appears after successful updates.
3. Profile updates reflect in public doctor lists and booking pages.

**Priority:** Medium
**Story Points:** 2
**Notes:**
- Consider moderation workflow if profile changes must be reviewed by admin.

## Story 6: View patient details for upcoming appointments
**Title:**
_As a doctor, I want to view patient details for upcoming appointments, so that I can be prepared for consultations._

**Acceptance Criteria:**
1. Doctors can view patient basic info (name, age, reason for visit, contact) for each upcoming appointment.
2. Sensitive data (medical history or personal IDs) are only shown if permissions and privacy policies allow.
3. Doctors can click through to detailed records if they have the right privileges.

**Priority:** High
**Story Points:** 3
**Notes:**
- Ensure role-based access control and logging when patient data is viewed by a doctor.

-----
