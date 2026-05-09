# Twitch Backend

Spring Boot API for a Twitch discovery demo. It exposes games, grouped streams/videos/clips, favorites, and recommendations.

## Features

- `GET /game` returns top games or searches by `game_name`.
- `GET /search?game_id=` returns streams, videos, and clips grouped by type.
- `GET /recommendation` returns content for a favorite game or a popular game.
- `GET /favorite`, `POST /favorite`, `DELETE /favorite/{twitchId}` manage demo favorites.
- `GET /health` supports quick deployment smoke checks.
- Demo mode works without Twitch API credentials; live mode uses Twitch client credentials.

## Local Run

```bash
cp .env.example .env
./gradlew bootRun
```

The API runs at `http://localhost:8080`.

For live Twitch API calls, set:

```bash
export TWITCH_DEMO_MODE=false
export TWITCH_CLIENT_ID=your-client-id
export TWITCH_CLIENT_SECRET=your-client-secret
./gradlew bootRun
```

## Build

```bash
./gradlew clean test
./gradlew clean bootJar
docker build -t twitch-api:local .
```

## Deployment

This repo includes `cloudbuild.yaml` for Cloud Run. The default deployment uses `TWITCH_DEMO_MODE=true`, `min-instances=0`, and `max-instances=1` to keep cost low.

For live mode on Google Cloud, store the Twitch secret in Secret Manager and update Cloud Run:

```bash
gcloud secrets create twitch-client-secret --replication-policy=automatic
printf '%s' "$TWITCH_CLIENT_SECRET" | gcloud secrets versions add twitch-client-secret --data-file=-
gcloud run services update twitch-api \
  --region us-central1 \
  --set-env-vars TWITCH_DEMO_MODE=false,TWITCH_CLIENT_ID="$TWITCH_CLIENT_ID" \
  --set-secrets TWITCH_CLIENT_SECRET=twitch-client-secret:latest
```

## Documentation

- [Runtime configuration](docs/configuration.md)
- [Postman collection](postman/Twitch Backend.postman_collection.json)
