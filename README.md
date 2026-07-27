# Vehicle Tracking System (VTS) – Real-Time Fleet Management Platform

## Overview

A full-stack real-time vehicle tracking platform designed to simulate and monitor a fleet of vehicles. The system processes live truck positions, calculates routes, stores operational data, and provides monitoring dashboards similar to solutions used in logistics and transportation environments.

The project was developed to demonstrate practical skills in backend development, DevOps practices, containerization, monitoring, and system integration.

---

## Architecture

The application consists of several independent services running in Docker containers:

* **Backend:** Java 23 + Spring Boot
* **Frontend:** React + TypeScript + Vite
* **Database:** PostgreSQL
* **Routing Engine:** OSRM (Open Source Routing Machine)
* **Search & Logging:** Elasticsearch + Kibana
* **Monitoring:** Grafana
* **Infrastructure:** Docker Compose

---

## Backend

Implemented a Spring Boot backend responsible for:

* real-time truck position processing
* fleet simulation
* route calculation
* REST API communication
* WebSocket-based live updates
* database synchronization
* Elasticsearch integration

Technologies:

* Java 23
* Spring Boot
* Spring Data JPA
* WebSocket / STOMP
* PostgreSQL
* Elasticsearch Client

---

## Frontend

Developed a React-based monitoring interface providing:

* live fleet visualization on an interactive map
* truck status monitoring
* route visualization
* fleet statistics
* real-time updates from backend services

Technologies:

* React
* TypeScript
* Vite
* Leaflet
* STOMP WebSocket client

---

## DevOps & Infrastructure

The complete environment is containerized using Docker Compose.

Implemented:

* multi-container application deployment
* service dependencies
* health checks
* persistent Docker volumes
* environment-based configuration
* isolated development environment

Docker services:

* Spring Boot application
* PostgreSQL database
* OSRM routing service
* Elasticsearch
* Kibana
* Grafana
* React frontend

---

## Monitoring & Observability

Implemented monitoring capabilities using:

* Grafana dashboards
* Elasticsearch logging stack
* Kibana log visualization

The system architecture allows monitoring application performance, infrastructure status, and operational data.

---

## Project Highlights

* Real-time communication between backend and frontend
* Simulation of hundreds of moving vehicles
* Integration of external routing engine
* Distributed architecture based on independent services
* Production-like Docker environment
* Full-stack implementation from database to user interface

---

## Skills Demonstrated

**Backend Development**

* Java
* Spring Boot
* REST APIs
* WebSockets
* Database integration

**Frontend Development**

* React
* TypeScript
* Real-time UI updates

**DevOps**

* Docker
* Docker Compose
* Monitoring
* Logging
* Service orchestration

**Infrastructure**

* PostgreSQL
* Elasticsearch
* Grafana
* Kibana

```
```
