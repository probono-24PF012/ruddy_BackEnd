from pymongo import MongoClient
import uuid
import json
class Mongodb_service:
    def __init__(self):
        mongo = MongoClient(host='34.47.74.227', port=27017)
        mongodb=mongo['local']
        self.mongodb_collection=mongodb['Test']
    
    def get_chat_list(self, username):
        query= {"username":username}
        projection = {"_id": 0, "username": 1, "chat_uuid": 1, "chat_name": 1}
        return self.mongodb_collection.find(query, projection)

    def create_newchat(self, username):
        uuid1= uuid.uuid1()
        dic={
            "username":username,
            "chat_uuid":str(uuid1),
            "question":[],
            "answer":[],
            "chat_name": "new_chat"
        }
        self.mongodb_collection.insert_one(dic)
        return "success"
    
    def load_chat(self, username, chat_uuid):
        query= {"username":username, "chat_uuid":chat_uuid}
        projection = {"_id": 0,"question": 1, "answer": 1}
        return self.mongodb_collection.find(query, projection)

    def delete_chat(self, username, chat_uuid):
        query= {"username":username, "chat_uuid":chat_uuid}
        self.mongodb_collection.delete_one(query)
        return "success"
    
    def save_chat(self, username, chat_uuid, question, answer):
        self.mongodb_collection.update_one(
        {"username":username,"chat_uuid":chat_uuid},
        {"$push": {"question":question,"answer":answer}},
        upsert=True 
    )