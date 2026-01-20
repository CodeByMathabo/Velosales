## Project Overview

**Velosales** acts as a backend utility for a larger inventory system. It solves a specific business problem: **"How fast are products selling?"**

Instead of a monolithic approach, this service uses a decoupled architecture to ensure scalability and maintainability. It is designed to accept sales data batches and return stock velocity metrics using configurable calculation strategies.

---

## Architecture & Design

I followed the **Separation of Concerns** principle, ensuring each component has a single responsibility. This makes the codebase easy to read and maintain.

### The "Office" Analogy
To understand the flow, visualize the service as an office hierarchy:

1.  **The Receptionist (`VelocityController`)**: The entry point. Validates requests and delegates work.
2.  **The Manager (`VelocityService`)**: Orchestrates the flow. Fetches data and selects the math strategy.
3.  **The Archivist (`SalesRepository`)**: Handles raw data retrieval from the database.
4.  **The Calculator (`VelocityStrategy`)**: A polymorphic interface for business logic (Math).

### System Flow Diagram

```mermaid
graph TD
    User([User / Frontend]) -->|POST /velocity| Controller[Receptionist<br/>VelocityController]
    
    subgraph "Core Business Logic"
        Controller -->|Validates & Delegates| Service[Manager<br/>VelocityService]
        Service -->|Fetch Raw Sales| Repo[Archivist<br/>SalesRepository]
        Repo -->|List<Sale>| Service
        Service -->|Apply Formula| Strategy[Calculator<br/>VelocityStrategy]
    end
    
    subgraph "Data Layer"
        Repo <-->|SQL| DB[(PostgreSQL)]
    end
    
    Strategy -->|Velocity Score| Service
    Service -->|ApiResponse| Controller
    Controller -->|JSON| User
```

## How to run the application

I deployed this application on **Render** so you can see it working in real-time without installing anything. 
I also added a visual dashboard **(Swagger UI)** that allows you to click buttons to test the code.

### Steps to test the app:

**1. Open the Dashboard:** Go to https://velosales-app.onrender.com/swagger-ui/index.html.

**2. Find the Calculator:** Look for the green bar labeled **POST /api/velocity**. Click it to open the details.

**3. Start Testing:** Click the **Try it out** button on the right side.

**4. Enter Test Data:** In the text box, you will see brackets **[ ]** with **"string"**. Type some made-up Product IDs inside quotes, like this: **"ITEM-101", "ITEM-102"**

**5. Run:** Click the big blue **Execute** button. *Wait while loading*

**6. View Results:** Scroll down slightly to the Server Response. You will see the calculation results for the items you entered **(e.g., {"ITEM-102": 0,"ITEM-101": 0})**.

## Why is the result 0?

   If you run the demo and see a result of **0.0** or **0**, the application is working correctly.

### Here is what is happening behind the scenes:

**1. Real Database:** This demo connects to a **live PostgreSQL database** on **Render**, not a fake in-memory list.

**2.Fresh Start:** Since this is a fresh deployment, the database starts completely empty (no sales history).

**3.The Math:** The logic asks: *"How many items were sold in the last 7 days?"*
   * The answer is **0**. There is  no sales history yet

   * Therefore, the velocity calculation is *0 sales divided by 7 days = 0.0 or 0*.

*In a real-world scenario, another system **(like a Checkout App)** would be adding sales data into this database, and you would see those numbers reflected immediately.*