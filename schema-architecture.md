# Architecture summary

- This Spring Boot application uses both MVC and REST controllers. Thymeleaf templates are used for the Admin and Doctor dashboards, while REST APIs serve all other modules. The application interacts with two databases—MySQL (for patient, doctor, appointment, and admin data) and MongoDB (for prescriptions). All controllers route requests through a common service layer, which in turn delegates to the appropriate repositories. MySQL uses JPA entities while MongoDB uses document models.

## Numbered flow of data and control

- 1 User accesses AdminDashboard or Appointment pages (click, navigation or form submit). Static assets and frontend scripts in `src/main/resources/static` render the UI and trigger requests.
- 2 The action is routed to the appropriate Thymeleaf or REST controller (MVC route or REST endpoint). Routing is handled by Spring MVC mappings.
- 3 The controller validates request parameters and checks authentication/authorization. It maps input to DTOs and calls the service layer.
- 4 The service layer implements business logic and orchestration. It performs complex validations, starts transactions when needed, and delegates persistence operations to repositories.
- 5 Repositories (`com.project.back_end.repo`) perform persistence: JPA repositories operate on MySQL entities, while Mongo repositories handle prescription documents. Repos translate service requests into queries/commands to the databases.
- 6 Databases execute operations and return results or errors. The repository maps results back to entities/DTOs; the service layer handles errors, applies rollback on transactional failures, and prepares response models.
- 7 The controller constructs the HTTP response (JSON, redirect, or rendered Thymeleaf view) with appropriate status codes. The frontend receives the response and updates the UI; any errors are displayed to the user and logged for auditing.

Notes: authentication/authorization, input validation, error handling, logging, and transaction management are cross-cutting concerns applied at controller, service, or filter/interceptor levels as appropriate.
