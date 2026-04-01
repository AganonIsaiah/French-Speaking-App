import os
import re
from typing import List

from openai import OpenAI
from sqlalchemy.orm import Session

from models import ConversationMemory, ScenarioMemory
from schemas import ChatRequest, ScenarioRequest, TradRapidesCorrigeesRequest, TradRapidesResult

client = OpenAI(api_key=os.getenv("OPENAI_API_KEY"))
MODEL = os.getenv("OPENAI_MODEL", "gpt-4o-mini")

SYSTEM_PROMPT = (
    "Tu es un assistant rapide en français. "
    "Générer les réponses avec une ponctuation correcte, ne pas ajouter d'astérisques inutiles. "
    "Utilise un français simple et naturel. "
    "Répondez en deux phrases maximum."
)

MAX_HISTORY = 20


def gen_convo(request: ChatRequest, db: Session) -> str:
    record = db.query(ConversationMemory).filter(
        ConversationMemory.username == request.username
    ).first()

    history: list[dict] = list(record.messages) if record else []

    messages = [
        {"role": "system", "content": f"{SYSTEM_PROMPT}\nNiveau de l'utilisateur: {request.level}"}
    ]
    messages.extend(history)
    messages.append({"role": "user", "content": request.message})

    response = client.chat.completions.create(model=MODEL, messages=messages)
    ai_response = response.choices[0].message.content

    history.append({"role": "user", "content": request.message})
    history.append({"role": "assistant", "content": ai_response})

    if len(history) > MAX_HISTORY:
        history = history[-MAX_HISTORY:]

    if record:
        record.messages = history
    else:
        db.add(ConversationMemory(username=request.username, messages=history))

    db.commit()
    return ai_response


def gen_dix_phrases(level: str) -> List[str]:
    prompt = (
        f"Génère 10 phrases en français pour un niveau {level}. "
        "Numérote-les de 1 à 10. "
        "Chaque phrase doit être simple, naturelle et adaptée au niveau."
    )

    response = client.chat.completions.create(
        model=MODEL,
        messages=[
            {"role": "system", "content": SYSTEM_PROMPT},
            {"role": "user", "content": prompt},
        ],
    )

    text = response.choices[0].message.content
    phrases = re.split(r"\d+\.", text)
    return [p.strip() for p in phrases if p.strip()]


def gen_trad_rapides_corrigees(request: TradRapidesCorrigeesRequest) -> TradRapidesResult:
    prompt = (
        f'Voici une phrase en français: "{request.originalFrench}"\n'
        f'Voici la traduction en anglais proposée par l\'utilisateur: "{request.translatedEnglish}"\n'
        "Évalue la traduction et attribue un score de 0 à 100. "
        'Réponds exactement dans ce format: "Points: XX; Feedback: [ton feedback]"'
    )

    response = client.chat.completions.create(
        model=MODEL,
        messages=[
            {"role": "system", "content": "Tu es un professeur de français expert en traduction."},
            {"role": "user", "content": prompt},
        ],
    )

    text = response.choices[0].message.content
    points = 0
    feedback = text

    points_match = re.search(r"Points:\s*(\d+)", text)
    feedback_match = re.search(r"Feedback:\s*(.+)", text, re.DOTALL)

    if points_match:
        points = int(points_match.group(1))
    if feedback_match:
        feedback = feedback_match.group(1).strip()

    return TradRapidesResult(points=points, feedback=feedback)


def gen_scenario(request: ScenarioRequest, db: Session) -> str:
    mem_key = f"{request.username}_{request.scenario}"

    record = db.query(ScenarioMemory).filter(ScenarioMemory.id == mem_key).first()

    if request.reset:
        history: list[dict] = []
        if record:
            record.messages = []
            db.commit()
    else:
        history = list(record.messages) if record else []

    system = (
        f"Tu joues le rôle de {request.character}. "
        f"Guide l'utilisateur dans une conversation en français adaptée au niveau {request.level}. "
        "Reste dans le personnage. Utilise un français simple et naturel. "
        "Réponds en deux phrases maximum. Ne pas ajouter d'astérisques."
    )

    messages = [{"role": "system", "content": system}]
    messages.extend(history)
    messages.append({"role": "user", "content": request.message})

    response = client.chat.completions.create(model=MODEL, messages=messages)
    ai_response = response.choices[0].message.content

    history.append({"role": "user", "content": request.message})
    history.append({"role": "assistant", "content": ai_response})

    if len(history) > MAX_HISTORY:
        history = history[-MAX_HISTORY:]

    if record:
        record.messages = history
    else:
        db.add(ScenarioMemory(id=mem_key, messages=history))

    db.commit()
    return ai_response
