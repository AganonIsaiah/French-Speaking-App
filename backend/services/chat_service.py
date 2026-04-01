import os
import re
from typing import List

from openai import OpenAI

from schemas import ChatRequest, TradRapidesCorrigeesRequest, TradRapidesResult

client = OpenAI(api_key=os.getenv("OPENAI_API_KEY"))
MODEL = os.getenv("OPENAI_MODEL", "gpt-4o-mini")

SYSTEM_PROMPT = (
    "Tu es un assistant rapide en français. "
    "Générer les réponses avec une ponctuation correcte, ne pas ajouter d'astérisques inutiles. "
    "Utilise un français simple et naturel. "
    "Répondez en deux phrases maximum."
)

MAX_HISTORY = 20

# Per-user conversation history: { username: [{"role": ..., "content": ...}, ...] }
conversation_memory: dict[str, list[dict]] = {}


def gen_convo(request: ChatRequest) -> str:
    username = request.username
    level = request.level
    user_message = request.message

    if username not in conversation_memory:
        conversation_memory[username] = []

    history = conversation_memory[username]

    messages = [
        {"role": "system", "content": f"{SYSTEM_PROMPT}\nNiveau de l'utilisateur: {level}"}
    ]
    messages.extend(history)
    messages.append({"role": "user", "content": user_message})

    response = client.chat.completions.create(model=MODEL, messages=messages)
    ai_response = response.choices[0].message.content

    history.append({"role": "user", "content": user_message})
    history.append({"role": "assistant", "content": ai_response})

    if len(history) > MAX_HISTORY:
        conversation_memory[username] = history[-MAX_HISTORY:]

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
