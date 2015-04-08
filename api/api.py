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


if __name__ == "__main__":
app.debug = True
app.run()