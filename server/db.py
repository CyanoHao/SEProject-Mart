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


class DBHelper:

    def __init__(self, db):
        self._db = db

    def addUser(self, n, pa, pr=1):
        user = User(name=n, password=pa, priority=pr)
        try:
            self._db.session.add(user)
            self._db.session.commit()
#        except IntegrityError:
#           return "Duplicate user name"
        except Exception as e:
            print type(e)
            return "Unknown error"

    def getUser(self, name):
        user = None
        try:
            user = User.query.filter_by(name=name).one()
        except Exception:
            pass
        return user

db_helper = DBHelper(db)
