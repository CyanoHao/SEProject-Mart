#! /usr/bin/env python2

from auth import AuthHelper, error_handler
from db import app, config_helper as ch, db_helper as dh
from flask import request, jsonify

import time

auth = AuthHelper(dh)


@app.route('/register')
@auth.login_requied(0)
def register():
    pass

# begin--------------------商品信息相关API--------------------


@app.route('/Mart/v1.0/product/get', methods=['GET'])
@auth.login_requied(2)
def getAllProduct():
    res = dh.getAllProduct()
    if not isinstance(res, list):
        return error_handler(res, 404)
    return jsonify(
        {p.id: {"name": p.name, "price": float(p.price)} for p in res})


@app.route('/Mart/v1.0/product/get/<name>', methods=['GET'])
@auth.login_requied(2)
def getProductByName(name):
    res = dh.getProductByName(name)
    if not isinstance(res, list):
        return error_handler(res, 404)
    return jsonify(
        {p.id: {"name": p.name, "price": float(p.price)} for p in res})


@app.route('/Mart/v1.0/product/add', methods=['POST'])
@auth.login_requied(1)
def addProductInfo():
    json = request.json
    if not json:
        return error_handler("Need json data", 404)
    msg = dh.addProductInfo(json)
    if msg:
        return error_handler(msg, 404)
    return "Success"


@app.route('/Mart/v1.0/product/update', methods=['POST'])
@auth.login_requied(1)
def updateProductInfo():
    json = request.json
    if not json:
        return error_handler("Need json data", 404)
    t0 = time.clock()
    msg = dh.updateProductInfo(json)
    if msg:
        return error_handler(msg, 404)
    print time.clock() - t0
    return "Success"

# end--------------------商品信息相关API--------------------


@app.route('/Mart/')
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
