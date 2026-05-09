# Twitch Backend

Spring Boot API for a Twitch discovery portfolio project. It exposes game search, grouped Twitch content, favorites, and recommendations. The default deployment uses demo data so the project is usable without Twitch developer credentials.

## Live Service

- Cloud Run service: `twitch-api`
- Cloud Run URL: `https://twitch-api-888561484971.us-central1.run.app`
- Frontend service: `https://twitch-888561484971.us-central1.run.app`
- Portfolio URL: `https://twitch.junliu.dev`
- Google Cloud project: `caramel-vim-441513-e1`
- Region: `us-central1`

## Tech Stack

- Java 21
- Spring Boot 3.5
- Spring Web
- Spring Security
- Spring Cloud OpenFeign
- OAuth2 client credentials support
- JavaFaker/DataFaker-style demo data
- Gradle wrapper
- Docker, Google Cloud Build, Google Cloud Run
- Postman collection for API testing

## Project Structure

```text
Twitch_backend/
  src/main/java/twitch/
    controller/
    db/
    external/
    model/
    service/
  src/main/resources/
    application.yml
  docs/
    configuration.md
  postman/
    Twitch Backend.postman_collection.json
  Dockerfile
  cloudbuild.yaml
  .env.example
```

## Features

- Search top games or games by name.
- Fetch streams, videos, and clips for a game.
- Save and remove favorite Twitch items.
- Return recommendations based on favorites or popular demo data.
- Run in demo mode without external credentials.
- Switch to live Twitch Helix API mode with a Twitch client id and secret.

## Local Development

Create a local env file:

```bash
cp .env.example .env
```

Run the backend:

```bash
./gradlew bootRun
```

Expected local URL:

```text
http://localhost:8080
```

For the paired frontend, it is often convenient to run the backend on port `8084`:

```bash
PORT=8084 TWITCH_DEMO_MODE=true ./gradlew bootRun
```

Expected result:

- `/health` returns a healthy response.
- `/game` returns demo games.
- `/search?game_id=...` returns grouped streams, videos, and clips.
- `/recommendation` returns recommended items.

## Environment Variables

Demo mode:

```env
PORT=8080
TWITCH_DEMO_MODE=true
TWITCH_CLIENT_ID=
TWITCH_CLIENT_SECRET=
TWITCH_API_LOG_LEVEL=DEBUG
```

Live Twitch API mode:

```env
TWITCH_DEMO_MODE=false
TWITCH_CLIENT_ID=your_twitch_client_id
TWITCH_CLIENT_SECRET=your_twitch_client_secret
```

Do not commit real Twitch credentials. On Google Cloud, store the secret in Secret Manager.

## API Endpoints

| Method | Path | Description |
| --- | --- | --- |
| `GET` | `/health` | Health check. |
| `GET` | `/status` | Runtime status. |
| `GET` | `/hello` | Simple smoke endpoint. |
| `GET` | `/game` | Top games or search by `game_name`. |
| `GET` | `/search?game_id={id}` | Streams, videos, and clips for a game. |
| `GET` | `/recommendation` | Recommended content. |
| `GET` | `/favorite` | List saved favorites. |
| `POST` | `/favorite` | Add a favorite item. |
| `DELETE` | `/favorite/{twitchId}` | Delete one favorite. |
| `DELETE` | `/favorite` | Clear favorites. |

## Postman

Import:

```text
postman/Twitch Backend.postman_collection.json
```

Suggested variables:

```text
baseUrl=http://localhost:8080
```

For Cloud Run:

```text
baseUrl=https://twitch-api-888561484971.us-central1.run.app
```

## Tests And Build

```bash
./gradlew clean test
./gradlew clean bootJar
```

Build a local image:

```bash
docker build -t twitch-api:local .
```

## Cloud Deployment

Manual deployment:

```bash
gcloud builds submit \
  --config cloudbuild.yaml \
  --project caramel-vim-441513-e1
```

The checked-in Cloud Build config deploys demo mode:

```text
TWITCH_DEMO_MODE=true
```

Cloud Run cost controls:

- `min-instances=0`
- `max-instances=1`
- no database
- no paid Twitch calls
- no persistent storage

To switch Cloud Run to live Twitch API mode:

```bash
gcloud secrets create twitch-client-secret --replication-policy=automatic
printf '%s' "$TWITCH_CLIENT_SECRET" | gcloud secrets versions add twitch-client-secret --data-file=-
gcloud run services update twitch-api \
  --region us-central1 \
  --set-env-vars TWITCH_DEMO_MODE=false,TWITCH_CLIENT_ID="$TWITCH_CLIENT_ID" \
  --set-secrets TWITCH_CLIENT_SECRET=twitch-client-secret:latest
```

## Frontend Pairing

Frontend repo:

```text
/Users/junliu/git_repo/Twitch_frontend
https://github.com/trickywork/Twitch_Frontend
```

The deployed frontend should use:

```env
REACT_APP_API_BASE_URL=https://twitch-api-888561484971.us-central1.run.app
```

## Expected Portfolio Behavior

A visitor should be able to search games, browse streams/videos/clips, add favorites, remove favorites, and view recommendations without needing a Twitch account or API credentials.

## Additional Notes

Runtime details are in:

```text
docs/configuration.md
```
