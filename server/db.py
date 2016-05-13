from flask import Flask
from flask.ext.sqlalchemy import SQLAlchemy
from conf import ConfigHelper

#from mysql.connector.errors import IntegrityError

#import time

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


class Product(db.Model):
    __tablename__ = 'prod_info'
    id = db.Column(db.CHAR(13), primary_key=True)
    name = db.Column(db.String(256))
    price = db.Column(db.Float(6, 2))


class DBHelper:

    def __init__(self, db):
        self._db = db

    def addUser(self, n, pa, pr=1):
        ses = self._db.session
        user = User(name=n, password=pa, priority=pr)
        try:
            ses.add(user)
            ses.commit()
        except Exception as e:
            print e.message
            ses.rollback
            return True

    def getUser(self, name):
        user = None
        try:
            user = User.query.filter_by(name=name).one()
        except Exception as e:
            print e.message
        return user

    def getProductInfo(self, prod_id):
        prod = None
        try:
            prod = Product.query.filter_by(prod_id=prod_id).one()
        except Exception as e:
            print e.message
        return prod

    def addProducts(self, prod_list):
        ses = self._db.session
        try:
            for info in prod_list:
                ses.add(Product(**info))
            ses.commit()
#        except IntegrityError:
#            ses.rollback()
#            return "Duplicate primary product id"
        except Exception as e:
            ses.rollback()
            return e.message

    def addProduct(self, prod_id, name, price):
        ses = self._db.session
        prod = Product(id=prod_id, name=name, price=price)
        try:
            ses.add(prod)
            ses.commit()
#        except IntegrityError:
#            ses.rollback()
#            return "Duplicate primary product id"
        except Exception as e:
            ses.rollback()
            return e.message

    def changeProductInfo(self, prod_id, name=None, price=None):
        pass

db_helper = DBHelper(db)
