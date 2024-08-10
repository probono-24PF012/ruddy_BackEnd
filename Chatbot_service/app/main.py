from fastapi import FastAPI
from pydantic import BaseModel
from typing import Annotated
from fastapi import HTTPException, Depends, status
from fastapi.security import OAuth2PasswordBearer
from services.mongodb_service import *
from dotenv import load_dotenv
from services.gpt_service import *
from services.redis_service import *
from services.jwt_auth import verify_token
import os
import jwt
import asyncio


mongodb_func=Mongodb_service()
    
app=FastAPI()

class Item_answer(BaseModel):
    answer: str
class Item_input(BaseModel):
    chat_uuid: str
    question: str
    language: str


@app.get('/')
def test():
    return {"Hello": "World"}

@app.get("/chatbot/", summary="Root 테스트 API")
def read_root():
    return {"Hello": "World"}

@app.get("/chatbot/chat", summary="질문 요청 APi", response_model=Item_answer,
        responses={
        200: {
            "content": {
            "application/json": {
                "example": "오늘 강남구 개포동 아침 날씨는 맑고 기온은 29.4℃입니다. 강수확률은 0.0%이며, 남실바람이 불고 습도는 47.0%입니다."
            }
        }}})
async def chat(username: Annotated[str , Depends(verify_token)], Item: Item_input):
    """
    # 요청 헤더: JWT 토큰 
    # 입력 파라미터
    ## chat_uuid : 채팅방 고유 ID
    ## question : 유저의 질문
    ## language : 유저의 언어
    # 반환 값
    ## answer : 봇의 답변
    """
    gpt = gpt_func()
    redis=redis_func()
    answer=gpt.Rag_output(username, Item.chat_uuid, Item.question, Item.language)
    redis.add_conversation(username, Item.chat_uuid, Item.question, answer)
    mongodb_func.save_chat(username, Item.chat_uuid, Item.question, answer)
    return answer

@app.get("/chatbot/list", summary= "유저 채팅 기록 list 반환",        
        responses={
        200: {
            "content": {
            "application/json": {
                "example": [
                    {
                        "username": "matkimchi",
                        "chat_uuid": "b1171df0-5547-11ef-8e9c-0242ac160003",
                        "chat_name": "new_chat"
                    }
                ]
            }
        }}})
async def chat_list( username: Annotated[str , Depends(verify_token)]):
    
    """
    # 요청 헤더: JWT 토큰 
    # 반환 값: 
    ## username : 유저의 ID
    ## chat_uuid : 채팅방 고유 ID 
    ## chat_name : 목록에 보일 채팅 이름, 기본값: new_chat
    """
    print(username)
    datas=[]
    data=mongodb_func.get_chat_list(username)
    for a in data:
        datas.append(a)
    return datas

@app.get('/chatbot/load/{chat_uuid}', summary= "채팅방의 대화 로드",        
        responses={
        200: {
            "content": {
            "application/json": {
                "example": [
                    {
                        "question": [
                            "오늘날씨 알려줘"
                        ],
                        "answer": [
                            "오늘 강남구 개포동 아침 날씨는 맑고 기온은 29.4℃입니다. 강수확률은 0.0%이며, 남실바람이 불고 습도는 47.0%입니다."
                        ]
                    }
                ]
            }
        }}})
async def chat_load(chat_uuid: str, username: str = Depends(verify_token)):
    """
    # 요청 헤더 : JWT 토큰
    # 입력 파라미터
    ## chat_uuid : 채팅방 고유 ID
    # 반환 값 
    ## question : 유저의 질문 (list)
    ## answer : 봇의 답변 (list)
    """
    datas=[]
    conversation = mongodb_func.load_chat(username, chat_uuid)
    for a in conversation:
        datas.append(a)
    return datas

@app.post('/chatbot/create', summary= "새로운 채팅 생성",
        responses={
        200: {
            "content": {
            "application/json": {
                "example": "success"
            }
        }}})
async def chat_create(username: str = Depends(verify_token)):
    """
    # 요청 헤더: JWT 토큰 
    """
    mongodb_func.create_newchat(username)
    return "success"

@app.delete("/chatbot/delete/{chat_uuid}", summary= "채팅방 삭제",
        responses={
        200: {
            "content": {
            "application/json": {
                "example": "success"
            }
        }}})
async def chat_delete(chat_uuid: str, username: str = Depends(verify_token)):
    """
    # 요청 헤더: JWT 토큰
    # 입력 파라미터
    - chat_uuid : 채팅방 고유 ID
    """
    mongodb_func.delete_chat(username, chat_uuid)
    return "success"



    
    