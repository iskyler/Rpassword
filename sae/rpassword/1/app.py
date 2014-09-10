# -*- coding: utf-8 -*-
import sae.const

from flask import Flask, request, Response
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
    if 'POST' == request.method:
        user_form = request.get_json()
        try:
            username = user_form.username
            password = user_form.password
            email = user_form.email
        except:
            return '', 403

        user = db.session.query(User).filter_by(usermame=username)
        if user is not None:
            return '', 403

        user = db.session.query(User).filter)_by(email=email)
        if user is not None:
            return '', 403

        user = User(username, password, email)
        db.session.add(user)
        db.session.commit()
        return '', 200

    return '', 404

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
