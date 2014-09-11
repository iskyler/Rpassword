# -*- coding: utf-8 -*-
from flask import Flask, request, Response
from flask.ext.sqlalchemy import SQLAlchemy

from config import *
app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = URI
app.debug = True

db = SQLAlchemy(app)

from models import *

@app.route('/')
def index():
    return "hello, 2333"

@app.route('/register', methods=['POST'])
def register():
    user_form = request.get_json()
    try:
        username = user_form['username']
        password = user_form['password']
        email = user_form['email']
        admin = user_form['admin']
    except:
        return DATA_ERR, 403

    user = User.query.filter_by(username=username).first()
    if user is not None:
        return USER_EXIST, 403

    user = User.query.filter_by(email=email).first()
    if user is not None:
        return USER_EXIST, 403

    user = User(username, password, email)
    user.admin = UserAuth(admin)
    db.session.add(user)
    db.session.commit()
    return '', 200

@app.route('/login', methods=['POST'])
def login():
    user_form = request.get_json()
    try:
        username = user_form['username']
        password = user_form['password']
    except:
        return DATA_ERR, 403

    user = User.query.filter_by(username=username).fisrt()
    if user is None:
        return USER_NOT_EXIST, 403

    if user.is_login is True:
        return HAS_LOGINED, 200

    user.is_login = True
    return '', 200

@app.route('/logout', methods=['POST'])
def logout():
    user_form = request.get_json()
    try:
        username = user_form['username']
    except:
        return DATA_ERR, 403

    user = User.query.filter_by(username=username).first()
    if user is None:
        return USER_NOT_EXIST, 403

    user.is_login = False
    return '', 200

@app.route('/add', methods=['POST'])
def add():
    pass

@app.route('/work', methods=['POST'])
def work():
    # query

    # update

    # delete

    pass
