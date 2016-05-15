#! /usr/bin/env python2
# -*- coding:utf-8 -*-

from auth import AuthHelper, error_handler
from db import app, config_helper as ch, db_helper as dh
from flask import request, jsonify

#import time

auth = AuthHelper(dh)

# 验证身份


@app.route('/Mart/v1.0/auth', methods=['GET'])
def autorization():
    priority = auth.login_check()
    if priority is not None:
        return jsonify({"priority": priority})
    return error_handler("Login failed", 401)

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


@app.route('/Mart/v1.0/product/get/<id>', methods=['GET'])
@auth.login_requied(2)
def getProductById(id):
    res = dh.getProductById(id)
    if isinstance(res, str):
        return error_handler(res, 404)
    return jsonify({"name": res.name, "price": res.price})


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
    msg = dh.updateProductInfo(json)
    if msg:
        return error_handler(msg, 404)
    return "Success"

# end--------------------商品信息相关API--------------------

# begin--------------------入库记录相关API--------------------


@app.route('/Mart/v1.0/inventory/add', methods=['POST'])
@auth.login_requied(1)
def addInventoryRecord():
    json = request.json
    if not json:
        return error_handler("Need json data", 404)
    msg = dh.addInventoryRecord(json)
    if msg:
        return error_handler(msg, 404)
    return "Success"


@app.route('/Mart/v.10/inventory/get', methods=['GET'])
@auth.login_requied(0)
def getInventoryRecord():
    res = dh.getInventoryRecord()
    if not isinstance(res, list):
        return error_handler(res, 404)
    return [i.toDict() for i in res]

# end--------------------入库记录相关API--------------------
# begin--------------------交易记录相关API--------------------


@app.route('/Mart/v1.0/sale/add', methods=['POST'])
@auth.login_requied(1)
def addSaleRecord():
    json = request.json
    if not json:
        return error_handler("Need json data", 404)
    sale_list = []
    sale_detail_list = []
    try:
        for k, s in json.items():
            sale_list.append({'id': k, 'date': s[
                'date'], 'discount': s['discount']})
            sale_detail_list.extend([dict(sale_id=k, **sd)
                                     for sd in s['detail']])
    except Exception:
        return error_handler("Wrong data format", 404)
    msg = dh.addSaleRecord(sale_list, sale_detail_list)
    if msg:
        return error_handler(msg, 404)
    return "Success"


@app.route('/Mart/v1.0/sale/get', methods=['POST'])
@auth.login_requied(1)
def getSaleRecord():
    json = request.json
    if not json or 'start' not in json:
        return error_handler("Need json data or Wrong format", 404)
    res = dh.getSaleRecord(json['start'], json.get('end', None))
    if not isinstance(res, list):
        return error_handler(res, 404)
    result = {}
    for r in res:
        s = result.get(r[0], None)
        if s:
            s['detail'].append(
                {'prod_id': r[5], 'num': r[3], 'price': float(r[4])})
        else:
            result[r[0]] = {
                'date': str(r[1]),
                'discount': float(r[2]),
                'detail': [
                    {'prod_id': r[5], 'num': r[3], 'price': float(r[4])}
                ]
            }
    return jsonify(result)

# end--------------------交易记录相关API--------------------

if __name__ == '__main__':
    app.run(host=ch.server.ip, port=ch.server.port, debug=ch.debug)
