#!/bin/bash

echo "⏰ Aktualny czas przed synchronizacją:"
date

echo "🔄 Restart chronyd..."
sudo systemctl restart chronyd

echo "⚡ Wymuszam synchronizację czasu..."
sudo chronyc makestep

echo "✅ Status synchronizacji:"
chronyc tracking

echo "⏰ Aktualny czas po synchronizacji:"
date
