# ReservaTuSala-Back

## Introduction
In this repository we have the backend of ReservaTuSala, a web application for managing meeting room reservations. The project has been developed with Java Spring Boot and works with a PostgreSQL database in a Docker container.


## Getting Started

### Prerequisites
1. Have Docker-Compose installed.
2. Have Maven installed.

### Installation
1. Clone this repository.
   ```shell
   git clone https://gitlab.com/profile-sevilla/practicas-22-23/reservatusala-back.git
   ```
2. Move to the new directory.
   ```shell
   cd ./reservatusala-back
   ```
3. Execute the command line below to set up the database docker container using docker compose.
   ```shell
   docker-compose up -d
   ```
4. Run the application.
   ```shell
   mvn spring-boot:run
   ```

### Swagger
With the application running, the Swagger API docuemntation is aviable [here](https://localhost:8081/swagger-ui/index.html).
   
## Database schema
We use a database to store all the information following the schema showed in the image below.

![Database schema](DB-schema.png)

