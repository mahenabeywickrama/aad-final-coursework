# aad-final-coursework

# CoreOne Prebuilt & Custom PC Store

## 1. Project Description
CoreOne is a web-based application that allows users to browse prebuilt gaming PCs, customize their own builds, and manage purchases. The platform features a responsive UI, interactive product filtering, and a shopping cart system, providing an enhanced experience for gamers and PC enthusiasts.

**Key Features:**
- Browse prebuilt PCs by performance and series
- Live filters for price, performance, and series
- Shopping cart with live total calculation
- Detailed PC pages with specs, badges, and images
- Secure user dashboard and profile management

---

## 2. Screenshots
Here are some screenshots showcasing the main features:

**Home Page**  
![Home Page](https://raw.githubusercontent.com/mahenabeywickrama/aad-final-coursework/refs/heads/main/frontend/screenshots/1.PNG)

**Prebuilt PCs Section**  
![Home Page](https://raw.githubusercontent.com/mahenabeywickrama/aad-final-coursework/refs/heads/main/frontend/screenshots/2.PNG)
![Home Page](https://raw.githubusercontent.com/mahenabeywickrama/aad-final-coursework/refs/heads/main/frontend/screenshots/3.PNG)

**PC Details Page**  
![Home Page](https://raw.githubusercontent.com/mahenabeywickrama/aad-final-coursework/refs/heads/main/frontend/screenshots/4.PNG)

**Shopping Cart Overlay**  
![Home Page](https://raw.githubusercontent.com/mahenabeywickrama/aad-final-coursework/refs/heads/main/frontend/screenshots/5.PNG)

**Checkout Page**  
![Home Page](https://raw.githubusercontent.com/mahenabeywickrama/aad-final-coursework/refs/heads/main/frontend/screenshots/6.PNG)

**User Dashboard**  
![Home Page](https://raw.githubusercontent.com/mahenabeywickrama/aad-final-coursework/refs/heads/main/frontend/screenshots/7.PNG)

**Admin Dashboard**  
![Home Page](https://raw.githubusercontent.com/mahenabeywickrama/aad-final-coursework/refs/heads/main/frontend/screenshots/8.PNG)

---

## 3. Setup Instructions

### Backend
1. Clone the repository:
   ```bash
   git clone https://github.com/mahenabeywickrama/aad-final-coursework.git
2. Navigate to the backend folder:
   ```bash
    cd coreone/backend

3. Install dependencies (Maven/Gradle):
   ```bash
    mvn install

4. Configure application.properties with your database credentials.

5. Run the backend:
   ```bash
    mvn spring-boot:run

### Frontend

1. Navigate to the frontend folder:
   ```bash
    cd coreone/frontend

2. Open index.html in your browser (or serve via a local server if needed):
   ```bash
    live-server

3. Ensure the backend is running on http://localhost:8080 for API calls.

## 4. Demo Video

Watch the project demo video here: [YouTube Link](https://www.youtube.com/watch?v=-MXsUC7MPLM)

## 5. Technologies Used
- **Frontend:** HTML, CSS, Bootstrap, JavaScript, jQuery
- **Backend:** Java, Spring Boot, Hibernate, MySQL
