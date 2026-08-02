#!/bin/bash

echo "⏰ Sync time"
sudo chronyc makestep

echo "🐳 Restart Kafka"
docker restart kafka_lab

echo "🚀 Start VTS"
docker compose up -d
