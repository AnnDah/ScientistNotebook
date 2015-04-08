import json
import urllib2
from urllib import urlopen, quote_plus as urlencode

class User(object):
	def __init__(self, json):
		self.name = json['name']
		self.password = json['password']


def get_user(query):
	user = None
	
	#Do database stuff

	return user