# Introduction

This app is used as hackathon project. It's just simply social app for who requesting advice. There are specialists who helps people for their problems. App can hide users who wants advice for privacy concerns.

# Stack 
Imported from gitlab.
App is written in Java, Spring boot. Used Postgresql DB. 
Deployed in Heroku.
Front-end written in Angular. Check app in [here](https://github.com/fermanquliyev/adviceMe). Thanks to [fermanquliyev](https://github.com/fermanquliyev)

# Run Application
1. `gradle clean build` - Build application and create `jar` file.
2. `docker-compose up` . Then enter the browser and enter the link: `localhost:8080/swagger-ui/`.
3. After finishing your work press `Ctrl + Z` for quitting from logs. And write: `docker-compose down --rmi all`.
