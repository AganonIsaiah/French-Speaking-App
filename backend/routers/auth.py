from fastapi import APIRouter, Depends, HTTPException
from fastapi.security import HTTPAuthorizationCredentials, HTTPBearer
from sqlalchemy.orm import Session

from database import get_db
from models import User
from schemas import AuthResponse, LoginRequest, SignupRequest, SignupResponse, UpdateLevelRequest, UserDTO
from security import create_access_token, get_password_hash, get_username_from_token, verify_password

router = APIRouter(prefix="/api/auth")
bearer = HTTPBearer()


@router.get("/test")
def test():
    return "Backend is running"


@router.post("/signup", response_model=SignupResponse)
def signup(request: SignupRequest, db: Session = Depends(get_db)):
    if db.query(User).filter(User.username == request.username).first():
        raise HTTPException(status_code=400, detail="Username already taken")
    if db.query(User).filter(User.email == request.email).first():
        raise HTTPException(status_code=400, detail="Email already in use")

    user = User(
        username=request.username,
        email=request.email,
        password=get_password_hash(request.password),
        region=request.region,
        level=request.level,
    )
    db.add(user)
    db.commit()
    db.refresh(user)

    return SignupResponse(message="User registered successfully", username=user.username)


@router.post("/login", response_model=AuthResponse)
def login(request: LoginRequest, db: Session = Depends(get_db)):
    user = db.query(User).filter(User.username == request.username).first()
    if not user or not verify_password(request.password, user.password):
        raise HTTPException(status_code=401, detail="Invalid credentials")

    token = create_access_token(user.username)

    return AuthResponse(
        jwt_token=token,
        message="Login successful",
        user=UserDTO(
            username=user.username,
            email=user.email,
            region=user.region,
            level=user.level,
        ),
    )


@router.put("/update-level")
def update_level(
    request: UpdateLevelRequest,
    credentials: HTTPAuthorizationCredentials = Depends(bearer),
    db: Session = Depends(get_db),
):
    username = get_username_from_token(credentials.credentials)
    if not username:
        raise HTTPException(status_code=401, detail="Invalid token")

    user = db.query(User).filter(User.username == username).first()
    if not user:
        raise HTTPException(status_code=404, detail="User not found")

    user.level = request.level
    db.commit()

    return {"message": "Level updated successfully", "username": user.username, "level": user.level}
