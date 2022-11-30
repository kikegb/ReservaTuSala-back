# Project1-Back

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
   
## Database schema
We use a database to store all the information following the schema showed in the image below.

![Database schema](DB-schema.png)

