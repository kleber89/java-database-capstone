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
