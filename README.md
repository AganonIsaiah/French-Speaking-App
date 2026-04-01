# FrançaisPro

A French language learning app powered by AI. Practice conversation, drill vocabulary with quick translations, and get real-time feedback — all in the browser.

**Live site:** [francaispro.vercel.app](https://francaispro.vercel.app)

---

## Features

- **Conversations** — Chat with an AI tutor in French, adapted to your proficiency level, with persistent conversation memory per session
- **Traductions Rapides** — Translate French phrases to English and receive an instant score (0–100) with detailed feedback
- **Dix Phrases** — Generate 10 French practice sentences tailored to your level
- **Auth** — Sign up, log in, and track your level across sessions

---

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | Angular 19, TypeScript |
| Backend | FastAPI (Python 3.12) |
| Database | PostgreSQL (Supabase) |
| AI | OpenAI API (`gpt-4o-mini`) |
| Auth | JWT (HS256) + BCrypt |
| Deployment | Vercel (frontend), Render (backend) |

---

## Project Structure

```
French-Speaking-App/
├── frontend/          # Angular app
│   └── src/app/
│       ├── components/
│       │   ├── accueil/              # Home page
│       │   ├── conversations/        # AI chat interface
│       │   ├── login/                # Auth page (login + signup)
│       │   └── traductions-rapides/  # Translation exercise
│       ├── services/                 # HTTP service layer
│       ├── interceptors/             # JWT + error interceptors
│       └── guards/                   # Route auth guard
└── backend/           # FastAPI app
    ├── main.py                  # App entry point + CORS
    ├── database.py              # SQLAlchemy engine + session
    ├── models.py                # User ORM model
    ├── schemas.py               # Pydantic request/response schemas
    ├── security.py              # JWT generation/validation + BCrypt
    ├── routers/
    │   ├── auth.py              # /api/auth/* endpoints
    │   └── chat.py             # /api/chat/* endpoints
    └── services/
        └── chat_service.py     # OpenAI integration + conversation memory
```

---

## Running Locally

### Prerequisites

- Node.js 18+
- Python 3.12+
- An [OpenAI API key](https://platform.openai.com/api-keys)

### Backend

```bash
cd backend

# 1. Create and activate a virtual environment
python3 -m venv venv
source venv/bin/activate      # Windows: venv\Scripts\activate

# 2. Install dependencies
pip install -r requirements.txt

# 3. Configure environment variables
cp .env.example .env
# Edit .env and fill in:
#   OPENAI_API_KEY   — your OpenAI key
#   JWT_SECRET_KEY   — run: python3 -c "import secrets; print(secrets.token_hex(32))"
#   DATABASE_URL     — your PostgreSQL connection string

# 4. Start the server
uvicorn main:app --host 0.0.0.0 --port 8080 --reload
```

The API will be available at `http://localhost:8080`.
Interactive docs: `http://localhost:8080/docs`

### Frontend

```bash
cd frontend

# 1. Install dependencies
npm install

# 2. Start in dev mode (points to http://localhost:8080)
npm run start:dev
```

The app will be available at `http://localhost:4200`.

### Docker (Backend)

```bash
cd backend
docker build -t francaispro-backend .
docker run -p 8080:8080 \
  -e OPENAI_API_KEY=your_key \
  -e JWT_SECRET_KEY=your_secret \
  -e DATABASE_URL=your_db_url \
  francaispro-backend
```

---

## Database Setup (Supabase)

In your Supabase project, open the **SQL Editor** and run:

```sql
-- Auto-update helper (run once)
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id         SERIAL PRIMARY KEY,
    username   VARCHAR NOT NULL UNIQUE,
    email      VARCHAR NOT NULL UNIQUE,
    password   VARCHAR NOT NULL,
    region     VARCHAR,
    level      VARCHAR,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ
);

CREATE OR REPLACE TRIGGER users_updated_at
BEFORE UPDATE ON users
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- Conversation memory (one row per user, persists across restarts)
CREATE TABLE IF NOT EXISTS conversation_memory (
    username   VARCHAR PRIMARY KEY,
    messages   JSONB NOT NULL DEFAULT '[]',
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE OR REPLACE TRIGGER conversation_memory_updated_at
BEFORE UPDATE ON conversation_memory
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- Scenario memory (one row per user+scenario combo)
CREATE TABLE IF NOT EXISTS scenario_memory (
    id         VARCHAR PRIMARY KEY,  -- "{username}_{scenario}"
    messages   JSONB NOT NULL DEFAULT '[]',
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE OR REPLACE TRIGGER scenario_memory_updated_at
BEFORE UPDATE ON scenario_memory
FOR EACH ROW EXECUTE FUNCTION set_updated_at();
```

Then set your `DATABASE_URL` in `.env` to the **Transaction Pooler** connection string found in Supabase under **Project Settings → Database → Connection string** (port `6543`).

---

## Environment Variables

| Variable | Description |
|---|---|
| `OPENAI_API_KEY` | OpenAI API key |
| `OPENAI_MODEL` | Model to use (default: `gpt-4o-mini`) |
| `DATABASE_URL` | PostgreSQL connection string |
| `JWT_SECRET_KEY` | Secret used to sign JWT tokens |

---

## API Endpoints

### Auth — `/api/auth`

| Method | Path | Description |
|---|---|---|
| `POST` | `/signup` | Register a new user |
| `POST` | `/login` | Authenticate and receive a JWT |
| `PUT` | `/update-level` | Update the authenticated user's level |
| `GET` | `/test` | Health check |

### Chat — `/api/chat` *(requires Bearer token)*

| Method | Path | Description |
|---|---|---|
| `POST` | `/conversations` | Send a message and get an AI response |
| `POST` | `/dix-phrases` | Generate 10 practice sentences for a given level |
| `POST` | `/traductions-rapides` | Grade a French-to-English translation |
