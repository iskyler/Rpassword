# -*- coding: utf8 -*-
from app import db

class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(16), unique=True)
    password = db.Column(db.String(64))
    email = db.Column(db.String(64), unique=True)
    admin = db.relationship('UserAuth', backref='user',
                            lazy='dynamic', uselist=False)
    account = db.relationship('UserAccount', backref='user',
                              lazy='dynamic')

class UserAuth(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_admin = db.Column(db.String(16))
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))

class UserAccount(db.Model):
    id = db.Column(db.Integer, primary=True)
    user_account = db.Column(String(32))
    user_account_password = db.Column(String(64))
    user_account_pubkey = db.Column(String(128))
    user_id = db.Column(db.Integer, db.ForeiKey('user.id'))
    user_account_prikey = db.relationship('UserAccountKey', backref='user_account',
                                          lazy='dynamic', uselist=False)

class UserAccountKey(db.Model):
    id = db.Column(db.Integer, primary=True)
    user_account_prikey = db.Column(Sting(128))
    user_account_id = db.Column(db.Integer, db.ForeignKey('user_account.id'))

