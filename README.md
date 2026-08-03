# Vehicle Tracking System (VTS) – Real-Time Fleet Management Platform

## Overview

A full-stack real-time vehicle tracking platform designed to simulate and monitor a fleet of vehicles.

The system processes live truck positions, stores operational data, calculates routes and provides a real-time monitoring dashboard.

The project demonstrates practical skills in backend development, DevOps practices, containerization, event-driven architecture and system integration.

---

## Screenshots

<img width="1918" height="921" alt="map-view-1007" src="https://github.com/user-attachments/assets/051a08d8-aad5-4dd3-9ba2-1af38b19ddc1" />

<img width="1598" height="965" alt="panel-view-1007" src="https://github.com/user-attachments/assets/9fbbdd6a-3047-4d9f-a0ac-4aa78ae78a0f" />

---


# Running the Project

The entire system can be started locally using Docker Compose.


## Clone repository

git clone https://github.com/Damianos17-tech/vehicle-tracking-system.git

cd vehicle-tracking-system


## Start application

docker compose up --build


## Available services

| Service | Port |
|---|---|
| Frontend | 8080 |
| Backend | 8090 |
| PostgreSQL | 5432 |
| Kafka | 9092 |
| Elasticsearch | 9200 |
| Kibana | 5601 |
| Grafana | 3000 |
| OSRM | 5000 |


---

# Repository Structure

vehicle-tracking-system/

├── backend/

├── simulator/

├── frontend/

├── docker-compose.yml

└── README.md


---

# Monitoring & Observability

Implemented monitoring and logging:

* Grafana dashboards
* Elasticsearch event storage
* Kibana log visualization
* operational fleet statistics


<img width="1883" height="928" alt="Kibana_dashboard" src="https://github.com/user-attachments/assets/9502dc60-e553-44a4-8a60-651d7d5f5c9f" />


---

# Project Highlights

* Real-time vehicle tracking
* Kafka-based event processing
* WebSocket live communication
* Simulation of hundreds of trucks
* Integration with external routing engine
* Docker-based infrastructure
* Full-stack implementation from database to user interface


---

# Technologies

## Backend

* Java
* Spring Boot
* Kafka
* PostgreSQL
* Elasticsearch
* WebSockets


## Frontend

* React
* TypeScript
* Vite
* Leaflet


## DevOps

* Docker
* Docker Compose
* Grafana
* Kibana
* Container networking
* Monitoring


# Architecture

The system consists of independent services running in Docker containers:

* Backend: Java 23 + Spring Boot
* Frontend: React + TypeScript + Vite
* Database: PostgreSQL
* Messaging: Apache Kafka
* Routing Engine: OSRM
* Logging: Elasticsearch + Kibana
* Monitoring: Grafana


## Architecture Flow

Truck Simulator

        |
        |
        v

Apache Kafka
(truck-state topic)

        |
        |
        v

Spring Boot Backend

        |
        +----------------+
        |                |
        v                v

PostgreSQL       Elasticsearch

        |
        |
        v

WebSocket / STOMP

        |
        |
        v

React + Leaflet Fleet Dashboard


---

# Backend

Spring Boot backend responsible for:

* processing real-time truck positions
* communication with simulators
* fleet allocation and management
* route calculation
* REST API communication
* WebSocket live updates
* database synchronization
* Elasticsearch event storage


Technologies:

* Java 23
* Spring Boot
* Spring Data JPA
* WebSocket / STOMP
* Apache Kafka
* PostgreSQL
* Elasticsearch


---

# Frontend

React-based monitoring dashboard providing:

* live truck visualization on an interactive map
* vehicle status monitoring
* route visualization
* fleet statistics
* real-time updates from backend services


Technologies:

* React
* TypeScript
* Vite
* Leaflet
* STOMP WebSocket Client


---

# DevOps & Infrastructure

The complete environment is containerized using Docker Compose.

Implemented:

* multi-container deployment
* Docker networking
* persistent volumes
* environment-based configuration
* monitoring stack integration
* distributed service communication


Docker services:

* Spring Boot Backend
* React Frontend
* PostgreSQL Database
* Apache Kafka
* OSRM Routing Engine
* Elasticsearch
* Kibana
* Grafana


---
