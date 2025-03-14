#!/bin/bash

docker-compose up -d postgres

echo "Waiting for PostgreSQL to start..."
while ! docker-compose exec postgres pg_isready -U your_user -d your_database; do
  sleep 2
done

docker-compose up -d extincts-web