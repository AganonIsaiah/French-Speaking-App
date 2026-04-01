from typing import List, Optional

from fastapi import APIRouter, Depends, Header, HTTPException

from schemas import ChatRequest, DixPhrasesRequest, TradRapidesCorrigeesRequest, TradRapidesResult
from security import get_username_from_token
from services.chat_service import gen_convo, gen_dix_phrases, gen_trad_rapides_corrigees

router = APIRouter(prefix="/api/chat")


def require_auth(authorization: Optional[str] = Header(None)) -> str:
    if not authorization or not authorization.startswith("Bearer "):
        raise HTTPException(status_code=401, detail="Missing or invalid Authorization header")
    token = authorization.split(" ", 1)[1]
    if not get_username_from_token(token):
        raise HTTPException(status_code=401, detail="Invalid or expired token")
    return token


@router.post("/conversations")
def conversations(
    request: ChatRequest,
    _: str = Depends(require_auth),
) -> str:
    return gen_convo(request)


@router.post("/dix-phrases")
def dix_phrases(
    request: DixPhrasesRequest,
    _: str = Depends(require_auth),
) -> List[str]:
    return gen_dix_phrases(request.level)


@router.post("/traductions-rapides")
def traductions_rapides(
    request: TradRapidesCorrigeesRequest,
    _: str = Depends(require_auth),
) -> TradRapidesResult:
    return gen_trad_rapides_corrigees(request)
