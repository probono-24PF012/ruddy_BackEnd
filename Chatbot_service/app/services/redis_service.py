
from redis import Redis
import json

class redis_func:
    def __init__(self):
        self.redis_client = Redis(host='redis', port=6379,db=0)

    def init_conversation(self, user_id, chat_uuid):
        key = f"user:{user_id}:chatroom:{chat_uuid}:conversations"
        self.redis_client.delete(key)

    def add_conversation(self, user_id, chat_uuid, user_message, bot_response):
        key = f"user:{user_id}:chatroom:{chat_uuid}:conversations"
        # 대화 쌍 생성
        conversation = {
            "user_message": user_message,
            "bot_response": bot_response
        }
        # 대화 추가 (JSON 형식으로 저장)
        self.redis_client.rpush(key, json.dumps(conversation))
        
        # 대화 길이가 5를 초과하면 오래된 대화 삭제
        if self.redis_client.llen(key) > 5:
            self.redis_client.lpop(key)

    def get_conversations(self, user_id, chat_uuid):
        key = f"user:{user_id}:chatroom:{chat_uuid}:conversations"
        conversations = self.redis_client.lrange(key, 0, -1)
        return [json.loads(conv) for conv in conversations]
