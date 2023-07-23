# Installation Guide

Follow the steps below to set up the Vacancy Scraping System on your machine:

## 1. Clone the Git Repository:
```bash
git clone https://github.com/ivanskyi/VacancyScrapingSystem
```
## 2. Install Dependencies:
Once Maven is installed, run the following command to build the project and fetch the required dependencies:

```markdown
mvn clean install
```
## 3. Set up MySQL Database:
If you have Docker installed on your system, you can use a Docker container to run the MySQL database easily. If not, you'll need to install MySQL manually and adjust the connection configuration accordingly.

To use Docker, run the following command to set up a MySQL container:

```markdown
sudo docker run --name mysql -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 -d mysql
```

This command will start a MySQL container with a root password "root" and bind port 3306 to the host machine.

## 4. Install Database Schema:
Before running the application, you need to create the necessary database structure. You can use the provided DDL script to create the tables.

## 5. Configure the Application:
Next, you need to configure the application to connect to the MySQL database. Locate the configuration file (usually application.properties or application.properties) in the cloned repository. Update the database connection properties (e.g., URL, username, password) with the appropriate values for your MySQL setup.

## 6. Run the Application:
Once the database is set up and the application is configured, you can run the Vacancy Scraping System.

