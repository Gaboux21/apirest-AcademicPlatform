## :books: Academic Platform Backend :school:
This is the backend system for an Academic Platform, built with Spring Boot. It handles all the data and logic for managing students, teachers, courses, grades, and more. Think of it as the "brain" behind a school's online system.

### :star: Features
- User Management: Register and manage different types of users:
- Admins: Full control over the platform.
- Professors: Manage courses and assign grades.
- Students: View their grades and academic history.

### :closed_lock_with_key: Secure Authentication & Authorization:
- Users can sign up and sign in securely using JSON Web Tokens (JWT).
- Access to different parts of the API is controlled based on user roles.
- Academic Data Management (CRUD Operations):

### :school_satchel: Create, Read, Update, and Delete information for:
- Courses (e.g., "Advanced Mathematics")
- Subjects (e.g., "Calculus I")
- Academic Periods (e.g., "Fall 2025 Semester")
- Grades (Notas): Record student grades for specific subjects within a course.
- Reporting & Analytics: Generate useful reports to track academic performance:
- Average Grades by Course and Subject: See the average score for each subject in every course.
- Student Grade History: Get a full list of all grades for a specific student.
- Course Performance Summary: View overall student performance within a particular course.

### :computer: Technologies Used
- Spring Boot: The main framework for building the application.
- Spring Data JPA: Makes it easy to interact with the database using Java objects.
- Spring Security: Handles user authentication and access control.
- JSON Web Tokens (JWT): For secure user sessions.
- PostgreSQL: The database used to store all academic data.
- Lombok: A library that reduces boilerplate code (e.g., getters, setters).
- Maven: A tool for project building and dependency management.
