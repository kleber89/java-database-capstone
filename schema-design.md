## MySQL Database Design

## Patients Table
- **Table Name**: `patients`
- **Columns**:
  - `patient_id` INT PRIMARY KEY AUTO_INCREMENT
  - `first_name` VARCHAR(50) NOT NULL
  - `last_name` VARCHAR(50) NOT NULL
  - `email` VARCHAR(100) UNIQUE NOT NULL
  - `phone` VARCHAR(15) NOT NULL
  - `date_of_birth` DATE NOT NULL
  - `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
 
  ## Doctors Tables
  - **Table Name**: `doctors`
- **Columns**:
  - `doctor_id` INT PRIMARY KEY AUTO_INCREMENT
  - `first_name` VARCHAR(50) NOT NULL
  - `last_name` VARCHAR(50) NOT NULL
  - `specialization` VARCHAR(100) NOT NULL
  - `email` VARCHAR(100) UNIQUE NOT NULL
  - `phone` VARCHAR(15) NOT NULL
  - `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
 
  ## Appoinments Table
  - **Table Name**: `appointments`
- **Columns**:
  - `appointment_id` INT PRIMARY KEY AUTO_INCREMENT
  - `patient_id` INT NOT NULL
  - `doctor_id` INT NOT NULL
  - `appointment_date` DATETIME NOT NULL
  - `status` ENUM('scheduled', 'completed', 'canceled') DEFAULT 'scheduled'
  - `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
- **Foreign Keys**:
  - `FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE`
  - `FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id) ON DELETE CASCADE`

  ## Admin Table
  - **Table Name**: `admin`
- **Columns**:
  - `admin_id` INT PRIMARY KEY AUTO_INCREMENT
  - `username` VARCHAR(50) UNIQUE NOT NULL
  - `password` VARCHAR(255) NOT NULL
  - `email` VARCHAR(100) UNIQUE NOT NULL
  - `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP

  ## Clinic Locations Table
  - **Table Name**: `clinic_locations`
- **Columns**:
  - `location_id` INT PRIMARY KEY AUTO_INCREMENT
  - `address` VARCHAR(255) NOT NULL
  - `city` VARCHAR(100) NOT NULL
  - `state` VARCHAR(100) NOT NULL
  - `zip_code` VARCHAR(10) NOT NULL
  - `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP

  ## Payments Table
  - **Table Name**: `payments`
- **Columns**:
  - `payment_id` INT PRIMARY KEY AUTO_INCREMENT
  - `appointment_id` INT NOT NULL
  - `amount` DECIMAL(10, 2) NOT NULL
  - `payment_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
- **Foreign Key**:
  - `FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id) ON DELETE CASCADE`

  ## Considerations
- **Constraints**:
  - Fields like `first_name`, `last_name`, `email`, and `phone` in the `patients` and `doctors` tables should be `NOT NULL` and `UNIQUE` where applicable.
  - Email and phone number formats can be validated later in the application code.
  
- **Deletion Behavior**:
  - If a patient is deleted, their associated appointments should also be deleted (`ON DELETE CASCADE`).
  
- **Overlapping Appointments**:
  - It is advisable to prevent overlapping appointments for doctors to avoid scheduling conflicts. This can be enforced through application logic or additional constraints in the database.


## MongoDB Collection Design

## 1. Patient Reference in Documents
### Should MongoDB Documents Include the Full Patient Object or Just an ID?
- **Recommendation**: It’s generally better to include just the patient ID in MongoDB documents rather than the full patient object. This approach has several advantages:
  - **Data Redundancy**: Storing only the ID avoids duplicating patient information across multiple documents, which can lead to inconsistencies.
  - **Flexibility**: If the patient’s information changes (e.g., name, phone number), updates are easier to manage since they only need to be applied in one place.
  - **Performance**: Reducing the size of documents by using IDs can improve performance, especially when dealing with large datasets.

## 2. Chat Message Document
### Example Document for Chat Messages
```json
{
  "message_id": "msg123456",
  "sender_id": "patient_12345",  // or "doctor_54321" depending on the sender
  "receiver_id": "doctor_54321", // or "patient_12345" depending on the receiver
  "timestamp": "2026-01-07T10:45:00Z",
  "message_content": "Can you please provide an update on my treatment plan?",
  "attachments": [
    {
      "file_name": "treatment_plan.pdf",
      "file_type": "application/pdf",
      "url": "https://example.com/attachments/treatment_plan.pdf"
    }
  ],
  "status": "sent", // Could be 'sent', 'delivered', 'read'
  "metadata": {
    "is_urgent": false,
    "reply_to": null // Can reference another message if it's a reply
  }
}
