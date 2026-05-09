# Runtime Configuration

## Environment Variables

| Name | Required | Default | Purpose |
| --- | --- | --- | --- |
| `PORT` | No | `8080` | HTTP port used by Cloud Run and local runs. |
| `TWITCH_DEMO_MODE` | No | `true` | Uses built-in demo data when true. Set false for live Twitch API calls. |
| `TWITCH_CLIENT_ID` | Live mode only | `demo-client-id` | Twitch Developer Console client ID. |
| `TWITCH_CLIENT_SECRET` | Live mode only | `demo-client-secret` | Twitch Developer Console client secret. Store in Secret Manager for cloud deployments. |
| `TWITCH_API_LOG_LEVEL` | No | `DEBUG` | Spring log level for the Feign Twitch client. |

## Local Development

1. Copy `.env.example` to `.env`.
2. Keep `TWITCH_DEMO_MODE=true` for local UI development without credentials.
3. Run `./gradlew bootRun`.
4. Open `http://localhost:8080/health` to verify the backend.

## Database Notes

The current portfolio build stores favorites in memory so it can run on Cloud Run with zero database cost. The original project progression introduces persistence later; if persistent favorites are needed, add a managed database and replace `FavoriteService` with a repository-backed implementation.

## Secret Hygiene

Do not commit Twitch client secrets. If a real Twitch secret was ever committed to a public repository, rotate it in the Twitch Developer Console before using live mode.
