from flask import Flask, jsonify
from flask import json
from flask import request
import pymongo
import image_processor # file
from pymongo import MongoClient
from flask_cors import CORS, cross_origin

client = None
app = Flask(__name__)
CORS(app)

@app.route("/")
def index():
    return "Hello World!"

@app.route('/authenticate', methods=['PUT'])
def authenticate():
    info = json.loads(request.data)
    try:
        pwd = info['password']
        # get an admin session
        return jsonify({'pwd': pwd}), 200
    except Exception as e:
        return str(e)

@app.route('/get_instructions/<string:pos_x>/<string:pos_y>', methods=["GET"])
def get_instructions(pos_x,pos_y):
    return jsonify({'behavior': [{
                        'color': "#FFF000",
                        'flash': "yes",
                        'vibrate': "yes",
                        'duration': "1000",
                        'shake': "yes"
                        },{
                        'color': "#F00FFF",
                        'flash': "no",
                        'vibrate': "no",
                        'duration': "1000",
                        'shake': "no"
                        }
                    ]})

@app.route("/add_figure", methods=["POST"])
def add_figure():
    try:
        client = MongoClient()
        db = client.crowdify_database
        crowdify_database.insert({"duration": data["duration"]})
        data = json.loads(request.data)
        image_url = data["url"]
        tpe = data["type"]
        print crowdify_database['duration']
        n = data["n"]
        m = data["m"]
        colors = image_processor.resize_and_get_pixels(image_url,n,m)
        construct_instructions(colors)
        return jsonify({'colors':colors})
    except Exception as e:
            return str(e)

def contruct_instructions(colors_list):
    #duration = crowdify_database['duration']
    duration = ""
    #FIND DURATION IN MONGODB
    for color in colors_list:
        add_instruction = jsonify({
            "color": color[0],
            "flash": "yest",
            "type": "static",
            "duration": duration,
            "shake": "yes",
            "vibrate": "yes"
        })
        crowdify_database[color[1]+"_"+color[2]] = add_instruction
        print crowdify_database[color[1]+"_"+color[2]]

if __name__ == '__main__':
      app.run(host='0.0.0.0', port=8080)
