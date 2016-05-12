from flask import Flask
from flask.ext.sqlalchemy import SQLAlchemy
from conf import ConfigHelper

# from sqlalchemy.orm.exc import NoResultFound

config_helper = ConfigHelper()

app = Flask("SEProject-Mart")
app.config['SQLALCHEMY_DATABASE_URI'] = config_helper.db_URI
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = True

db = SQLAlchemy(app)


class User(db.Model):
    __tablename__ = 'user'
    name = db.Column(db.String(32), primary_key=True)
    password = db.Column(db.String(32))
    priority = db.Column(db.Integer)


# class Inventory(db.Model):
#    __tablename__ = 'inventory'
#    id = db.Column(db.Integer, primary_key=True)
#    prod_id = db.Column(db.CHAR(13))
#    num = db.Column(db.Integer)
#    price = db.Column(db.Decimal(6, 2))
#    date = db.Column(db.DateTime)


# class Product(db.Model):
#   __tablename__ = 'prod_info'
#   id = db.Column(db.CHAR(13))
#    name = db.Column(db.TINYTEXT)
import time


class DBHelper:

    def __init__(self, db):
        self._db = db

    def addUser(self, n, pa, pr=1):
        user = User(name=n, password=pa, priority=pr)
        try:
            self._db.session.add(user)
            self._db.session.commit()
        except Exception:
            return "Unknown error"

    def getUser(self, name):
        user = None
        t0 = time.clock()
        try:
            user = User.query.first()
        except Exception:
            pass
        print time.clock() - t0
        return user

    def getProductInfo(self, prod_id):
        pass

    def changeProduct(self, prod_id, new_name, new_price):
        pass

db_helper = DBHelper(db)
