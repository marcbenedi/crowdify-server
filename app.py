from flask import Flask, jsonify
from flask import json
from flask import request
import pymongo
import image_processor # file
from pymongo import MongoClient
from flask_cors import CORS, cross_origin

from bson import BSON
from bson import json_util

client = None
app = Flask(__name__)
CORS(app)
taula = None

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
    global taula
    resultat = taula.find_one({"x":int(pos_x),"y":int(pos_y)})
    return json.dumps(resultat, sort_keys=True, indent=4, default=json_util.default)
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
        global taula
        taula = db.test_collection
        #crowdify_database.insert({"duration": data["duration"]})
        #data = json.loads(request.data)
        #Giro las linies perque s'accedeix a data abans de llegirla
        data = json.loads(request.data)
        taula.insert_one({"duration": data["duration"]})
        resultat_find =taula.find_one({"duration": data["duration"]})
        print resultat_find

        image_url = data["url"]
        tpe = data["type"]
        #print taula['duration']
        n = data["n"]
        m = data["m"]
        colors = image_processor.resize_and_get_pixels(image_url,n,m)
        construct_instructions(colors)
        result = taula.find_one({"x":0,"y":1})
        print result
        return jsonify({'colors':colors})
    except Exception as e:
            return str(e)

def construct_instructions(colors_list):
    #duration = crowdify_database['duration']
    duration = ""
    #FIND DURATION IN MONGODB
    for color in colors_list:
        #add_instruction = jsonify({
        #    "color": color[0],
        #    "flash": "yest",
        #    "type": "static",
        #    #"duration": duration,
        #    "duration": "300",
        #    "shake": "yes",
        #    "vibrate": "yes"
        #})
        add_instruction = {
            "x": color[1],
            "y": color[2],
            "color": color[0],
            "flash": "yest",
            "type": "static",
            #"duration": duration,
            "duration": "300",
            "shake": "yes",
            "vibrate": "yes"
        }
        global taula
        #result = taula.insert_one({str(color[1])+"_"+str(color[2]) :add_instruction})
        result = taula.insert_one(add_instruction)


        #taula[color[1]+"_"+color[2]] = add_instruction
        #print taula[color[1]+"_"+color[2]]

if __name__ == '__main__':
      app.run(host='0.0.0.0', port=8080)
