# README - IMDB Project

## Project Overview
This project simulates an **Internet Movie Database (IMDB)**, developed in Java. The system allows users to manage accounts, movies, series, and requests using different roles such as Admin, Contributor, and Regular users. The project integrates multiple design patterns like **Singleton**, **Builder**, **Factory**, **Observer**, and **Strategy** to ensure scalability, flexibility, and maintainability.

## User Authentication and Roles
Authentication is required. If unsuccessful, the user must retry until valid credentials are entered. After login, the system displays available options based on the user's role:

- **Regular Users**: Can view and rate productions, add to favorites, and create requests.
- **Contributors**: In addition to Regular User features, they can add or remove productions and actors.
- **Admins**: Can perform all actions, including managing users and requests.

After an action is performed, the user returns to the main menu unless they choose to log out.

## Input Files
- **accounts.json**: Stores user account information.
- **actors.json**: Stores actor information.
- **production.json**: Stores details about movies and series.
- **requests.json**: Stores user requests.
