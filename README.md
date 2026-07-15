# Todo Scala App

A sample todo list application built with Scala 3, ScalaJS, and Tyrian.

## Project Structure

- **common** — Shared models (e.g., `ListItem`) and Circe codecs
- **backend** — HTTP server using http4s (runs on port 8080)
- **frontend** — SPA client using ScalaJS + Tyrian

## Building & Running

```bash
# Compile everything
sbt compile

# Run the backend server
sbt backend/run

# Run tests
sbt test

# Generate frontend bundle
sbt frontend/fastLinkJS
```

## API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/api/todos` | List all todos |
| POST   | `/api/todos` | Add a todo (`{"text": "..."}`) |
| PUT    | `/api/todos/:id` | Toggle todo completion |
| DELETE | `/api/todos/:id` | Delete a todo |
