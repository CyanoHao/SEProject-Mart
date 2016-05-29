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


@app.route('/Mart/v1.0/echo-json', methods=['POST'])
def echoJson():
    json = request.json
    if not json:
        return error_handler("Need json data", 404)
    f = open("/tmp/echo-json", "w+")
    f.write(str(json))
    f.close
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

# begin--------------------前台相关API--------------------


@app.route('/Mart/v1.0/counter/count', methods=['GET'])
@auth.login_requied(2)
def getCounterCount():
    inventory_rec = dh.getInventoryRecord()
    if not isinstance(inventory_rec, list):
        return error_handler(inventory_rec, 404)
    sale_rec = dh.getSaleRecord()
    if not isinstance(sale_rec, list):
        return error_handler(sale_rec, 404)
    count = {}
    try:
        for p in inventory_rec:
            if p.prod_id != '':
                if p.num < 0:  ## 出库的为负
                    if p.prod_id in count.keys():
                        count[p.prod_id] = count[p.prod_id] - p.num
                    else:
                        count[p.prod_id] = -p.num
        for p in sale_rec:
            if p.prod_id != '':
                if p.prod_id in count.keys():
                    count[p.prod_id] = count[p.prod_id] - p.num
                else:
                    count[p.prod_id] = -p.num
    except Exception:
        return error_handler("Database Error", 404)
    return jsonify(
        {k: {"count": count[k]} for k in count})

# end--------------------前台相关API--------------------

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


@app.route('/Mart/v1.0/inventory/get', methods=['GET'])
@auth.login_requied(0)
def getInventoryRecord():
    res = dh.getInventoryRecord()
    if not isinstance(res, list):
        return error_handler(res, 404)
    return jsonify(
        {p.id: {"prod_id": p.prod_id, "num": p.num, "price": (float(p.price) if p.price else "null"), "date": p.date} for p in res})

# end--------------------入库记录相关API--------------------

# begin--------------------库存相关API--------------------


@app.route('/Mart/v1.0/inventory/count', methods=['GET'])
@auth.login_requied(1)
def getInventoryCount():
    res = dh.getInventoryRecord()
    if not isinstance(res, list):
        return error_handler(res, 404)
    count = {}
    try:
        for p in res:
            if p.prod_id != '':
                if p.prod_id in count.keys():
                    count[p.prod_id] = count[p.prod_id] + p.num
                else:
                    count[p.prod_id] = p.num
    except Exception:
        error_handler("Database Error", 404)
    return jsonify(
        {k: {"count": count[k]} for k in count})

# end--------------------库存相关API--------------------

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
        sale_list.append({'discount': json['discount']})
        sale_detail_list.extend(json['detail'])
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
    orderby_date = request.args.get('orderby_date', None)
    if not json or 'start' not in json:
        return error_handler("Need json data or Wrong format", 404)
    res = dh.getSaleRecord(json['start'], json.get('end', None), orderby_date)
    if not isinstance(res, list):
        return error_handler(res, 404)
    result = []
    for r in res:
        item = None
        for it in result:
            if it['id'] == r[0]:
                item = it
                break
        if not item:
            item = {'id': r[0],
                    'date': str(r[1]),
                    'discount': float(r[2]),
                    'detail': [
                {
                    'prod_id': r[5], 'num': r[3], 'price': float(r[4])
                }
            ]}
        else:
            item['detail'].append(
                {'prod_id': r[5], 'num': r[3], 'price': float(r[4])})
        result.append(item)
    return jsonify({'sale_list': result})

# end--------------------交易记录相关API--------------------

if __name__ == '__main__':
    app.run(host=ch.server.ip, port=ch.server.port, debug=ch.debug)
