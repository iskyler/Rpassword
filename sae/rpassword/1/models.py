# -*- coding: utf8 -*-
from app import db

class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(16), unique=True)
    password = db.Column(db.String(64))
    email = db.Column(db.String(64), unique=True)
    is_login = db.Column(db.Boolean)
    is_activate = db.Column(db.Boolean)
    admin = db.relationship('UserAuth', backref='user',
                            lazy='joined', uselist=False)
    account = db.relationship('UserAccount', backref='user',
                              lazy='dynamic')

    def __init__(self, username, password, email):
        self.username = username
        self.password = password
        self.email = email
        self.is_login = False
        self.is_activate = False

class UserAuth(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_admin = db.Column(db.String(16))
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))

    def __init__(self, user_admin):
        self.user_admin = user_admin

class UserAccount(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_account = db.Column(db.String(32))
    user_account_name = db.Column(db.String(32))
    user_account_password = db.Column(db.String(64))
    user_account_pubkey = db.Column(db.String(128))
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    user_account_prikey = db.relationship('UserAccountKey', backref='user_account',
                                          lazy='select', uselist=False)

    def __init__(self, user_account, user_account_password, user_pubkey=None):
        self.user_account = user_account
        self.user_account_password = user_account_password
        self.user_pubkey = user_pubkey

class UserAccountKey(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_account_prikey = db.Column(db.String(128))
    user_account_id = db.Column(db.Integer, db.ForeignKey('user_account.id'))

    def __init__(self, user_account_prikey=None):
        self.user_account_prikey = user_account_prikey

db.create_all()
