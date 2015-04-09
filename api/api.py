# coding: utf-8

from flask import Flask, request, jsonify

app = Flask(__name__)

#API
@app.route('/api/v1/user')
def get_user():
	query = request.args.get('username')
	# No query was provided
	if not query:
		# Return an error and set an appropriate status code
		return jsonify({'error': 'Bad Request', 'code': 400, 'message': 'No username parameter was provided'}), 400


#POST username, password (login) --> get token

#POST file
#GET file
#DELETE file
#PUT file

#POST user
#GET user
#PUT user
#DELETE user

#POST connection
#GET connection
#DELETE connection

if __name__ == "__main__":
	app.debug = True
	app.run()