### API Specification for Social Schedule Backend

**Core Principles:**

*   **RESTful Design:** Adhering to standard HTTP methods and resource-based URLs.
*   **Clear Naming:** Intuitive and descriptive endpoint names.
*   **Versioned API:** Using `/api/v1` for future compatibility.
*   **JSON Payloads:** Standardizing on JSON for requests and responses.
*   **Separation of Concerns:** Controllers for API, Services for business logic, and DTOs for data transfer.

---

### Proposed API Endpoints

**A. People Management (`/api/v1/people`)**
*   **`GET /api/v1/people`**
    *   **Description:** Retrieves all people in the system.
    *   **Response:** JSON array of `Person` objects.
*   **`POST /api/v1/people`**
    *   **Description:** Creates a new person.
    *   **Request Body:** JSON representation of the `Person` object (without ID, createdAt, updatedAt).
    *   **Response:** The created `Person` object.
*   **`DELETE /api/v1/people/{id}`**
    *   **Description:** Deletes a person by ID.
    *   **Response:** No content.

**B. Content Type Management (`/api/v1/content-types`)**
*   **`GET /api/v1/content-types`**
    *   **Description:** Retrieves all content types in the system.
    *   **Response:** JSON array of `ContentType` objects.
*   **`POST /api/v1/content-types`**
    *   **Description:** Creates a new content type.
    *   **Request Body:** JSON representation of the `ContentType` object (without ID, createdAt).
    *   **Response:** The created `ContentType` object.
*   **`DELETE /api/v1/content-types/{id}`**
    *   **Description:** Deletes a content type by ID.
    *   **Response:** No content.

**C. Schedule Template Management (`/api/v1/schedule-templates`)**
*   **`GET /api/v1/schedule-templates`**
    *   **Description:** Retrieves all schedule templates in the system.
    *   **Response:** JSON array of `ScheduleTemplate` objects.
*   **`POST /api/v1/schedule-templates`**
    *   **Description:** Creates a new schedule template.
    *   **Request Body:** JSON representation of the `ScheduleTemplate` object (without ID, createdAt, updatedAt).
    *   **Response:** The created `ScheduleTemplate` object.
*   **`DELETE /api/v1/schedule-templates/{id}`**
    *   **Description:** Deletes a schedule template by ID.
    *   **Response:** No content.

**D. Template Rule Management (`/api/v1/template-rules`)**
*   **`GET /api/v1/template-rules`**
    *   **Description:** Retrieves all template rules in the system.
    *   **Response:** JSON array of `TemplateRule` objects.
*   **`POST /api/v1/template-rules`**
    *   **Description:** Creates a new template rule.
    *   **Request Body:** JSON representation of the `TemplateRule` object (without ID, createdAt, updatedAt).
    *   **Response:** The created `TemplateRule` object.
*   **`DELETE /api/v1/template-rules/{id}`**
    *   **Description:** Deletes a template rule by ID.
    *   **Response:** No content.

**E. Schedule Management (`/api/v1/schedules`)**
*   **`GET /api/v1/schedules/{year}/{month}`**
    *   **Description:** Retrieves schedules for a specific month and year.
    *   **Response:** JSON array of `Schedule` objects for the specified month.
*   **`GET /api/v1/schedules/{year}`**
    *   **Description:** Retrieves schedules for an entire year.
    *   **Response:** JSON array of `Schedule` objects for the specified year.
*   **`POST /api/v1/schedules`**
    *   **Description:** Creates a new schedule entry.
    *   **Request Body:** JSON representation of the `Schedule` object (without ID, createdAt, updatedAt).
    *   **Response:** The created `Schedule` object.
*   **`DELETE /api/v1/schedules/{id}`**
    *   **Description:** Deletes a schedule entry by ID.
    *   **Response:** No content.

**F. Analysis (`/api/v1/analysis`)**
*   **`GET /api/v1/analysis/person/{personId}/{year}/{month}`**
    *   **Description:** Provides a breakdown of content types for a specific person within a given month and year.
    *   **Response:** JSON object with `personId` and `typeDistribution` (map of content type names to counts).
*   **`GET /api/v1/analysis/month/{year}/{month}`**
    *   **Description:** Provides analysis (e.g., maker content count, type content count) for a specific month.
    *   **Response:** JSON object with `makerContentCount`, `typeContentCount`, and `totalContentEntries`.
*   **`GET /api/v1/analysis/year/{year}`**
    *   **Description:** Provides analysis for an entire year.
    *   **Response:** JSON object with `makerContentCount`, `typeContentCount`, and `totalContentEntries`.
