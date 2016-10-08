from flask import Flask
import json
app = Flask(__name__)

@app.route("/")
def index():
    return "Hello World!"

@app.route("/create_event", methods=["POST"])# POST
def create_event():
    ata = json.loads(request.data)
    return "Creating event!"

@app.route("/<string:event_name>/<string:pos_x>/<string:pos_y>", methods=["GET"])# POST
def get_instructions(event_name,pos_x,pos_y):
    return "Geting instructions for the event " + event_name + " at " + pos_x + " " + " " + pos_y

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
