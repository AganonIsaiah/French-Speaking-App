from pydantic import BaseModel
from typing import Optional


class LoginRequest(BaseModel):
    username: str
    password: str


class SignupRequest(BaseModel):
    username: str
    password: str
    email: str
    region: Optional[str] = None
    level: Optional[str] = None


class SignupResponse(BaseModel):
    message: str
    username: str


class UserDTO(BaseModel):
    username: str
    email: str
    region: Optional[str] = None
    level: Optional[str] = None


class AuthResponse(BaseModel):
    jwt_token: str
    message: str
    user: UserDTO


class UpdateLevelRequest(BaseModel):
    level: str


class DixPhrasesRequest(BaseModel):
    level: str


class ChatRequest(BaseModel):
    message: str
    level: str
    username: str


class TradRapidesCorrigeesRequest(BaseModel):
    originalFrench: str
    translatedEnglish: str


class TradRapidesResult(BaseModel):
    points: int
    feedback: str
