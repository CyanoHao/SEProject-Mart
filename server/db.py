from flask import Flask
from flask.ext.sqlalchemy import SQLAlchemy
from conf import ConfigHelper

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

    # begin--------------------商品信息相关API--------------------

    def getProductByName(self, name):
        try:
            return Product.query.filter_by(name=name).all()
        except Exception as e:
            return e.message

    def getAllProduct(self):
        try:
            return Product.query.all()
        except Exception as e:
            return e.message

    def addProductInfo(self, prod_list):
        ses = self._db.session
        print prod_list
        try:
            ses.add_all([Product(id=k, **v)
                         for k, v in prod_list.items()])
            ses.commit()
        except Exception as e:
            ses.rollback()
            return e.message

    def updateProductInfo(self, prod_list):
        ses = self._db.session
        try:
            for k, v in prod_list.items():
                ses.execute(Product.__table__.update().where(
                    Product.id == k).values(**v))
            ses.commit()
        except Exception as e:
            ses.rollback()
            return e.message
    # end--------------------商品信息相关API--------------------


db_helper = DBHelper(db)
