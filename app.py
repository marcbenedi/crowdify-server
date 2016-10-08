from flask import Flask, jsonify
import json
import request
app = Flask(__name__)

@app.route("/")
def index():
    return "Hello World!"

@app.route("/create_event", methods=["POST"])# POST
def create_event():
    #data = request["data"]
    #data = json.loads(request.data)
    #if data.get('password') != "gentleman123":
        #return "Wrong password!"

    print(request.data)
    return "thanks for your visit"

@app.route("/<string:pos_x>/<string:pos_y>", methods=["GET"])# GET INFO
def get_instructions(pos_x,pos_y):
    return "Geting instructions for the event " + " at " + pos_x + " " + " " + pos_y

@app.route("/add_figure/<string:event_name>", methods=["POST"])
def add_figure(event_name):
    try:
        data = json.loads(request.data)
        image_url = data["url"]
        duration = data["duration"]
    except Exception as e:
            return str(e)

if __name__ == "__main__":
    app.run()
