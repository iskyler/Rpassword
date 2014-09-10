# -*- coding: utf-8 -*-
import sae.const

from flask import Flask
from flask.ext.sqlalchemy import SQLAlchemy

"""
database const:
    sae.const.MYSQL_DB
    sae.const.MYSQL_USER
    sae.const.MYSQL_PASS
    sae.const.MYSQL_HOST
    sae.const.MYSQL_PORT
"""
URI = "mysql://" + sae.const.MYSQL_USER + ":" + sae.const.MYSQL_PASS \
        + "@" + sae.const.MYSQL_HOST + ":" + sae.const.MYSQL_PORT \
        + '/' + sae.const.MYSQL_DB

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = URI
app.debug = True

db = SQLAlchemy(app)

from models import *

@app.route('/')
def index():
    return "hello, 2333"

@app.route('/register/')
def register():
    pass

@app.route('/login/')
def login():
    pass

@app.route('/logout/')
def logout():
    pass

@app.route('/auth/')
def auth():
    pass

@app.route('/ask/')
def ask():
    pass

@app.route('/add/')
def add():
    pass

@app.route('/modify/')
def modify():
    # update

    # delete

    pass
