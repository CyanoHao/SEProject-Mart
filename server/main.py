#! /usr/bin/env python2

from auth import AuthHelper, error_handler
from db import app, config_helper as ch, db_helper as dh
from flask import request, make_response

auth = AuthHelper(dh)


@app.route('/')
@auth.login_requied(0)
def hello_world():
    return "hello_world"


@app.route('/about')
def about_us():
    pass


@app.route('/register')
@auth.login_requied(0)
def register():
    pass


@app.route('/Mart/v1.0/product/add', methods=['POST'])
def addProducts():
    json = request.json
    if not json:
        return error_handler("Need json data", 404)
    if not isinstance(json, list):
        return error_handler("Wrong data format", 404)
    msg = dh.addProducts(json)
    if msg:
        return error_handler("error", 404)
    return "Success"


@app.route('/Mart/v1.0/read-record')
def read_record():
    pass


@app.route('/Mart/v1.0/write-record')
def write_record():
    pass


@app.route('/Mart/v1.0/read-record/<id>')
def read_record_with_id(id):
    pass


@app.route('/Mart/v1.0/write-record/<id>')
def write_record_with_id(id):
    pass

if __name__ == '__main__':
    app.run(host=ch.server.ip, port=ch.server.port, debug=ch.debug)
