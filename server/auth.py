from functools import wraps
from flask import make_response, request, jsonify


def error_handler(msg, status=401):
    print msg
    res = make_response(jsonify({'error': msg}), status)
    res.headers['WWW-Authenticate'] = 'Basic realm=""'
    return res

class AuthHelper:

    def __init__(self, dbhelper):
        self.dh = dbhelper

    def check_status(self, auth, priority):
        msg = None
        if not auth:
            msg = "Need to login"
            return msg
        user = self.dh.getUser(auth.username)
        if not user:
            msg = "User doesn't exist or Internel error"
        elif user.password != auth.password:
            msg = "Incorrect Password"
        elif user.priority > priority:
            msg = "Need more authority"
        return msg

    def login_requied(self, level):
        def decorator(f):
            @wraps(f)
            def decorated(*args, **kw):
                auth = request.authorization
                msg = self.check_status(auth, level)
                if msg:
                    return error_handler(msg)
                return f(*args, **kw)
            return decorated
        return decorator

    def login_check(self):
        auth = request.authorization
        if not auth:
            return None
        user = self.dh.getUser(auth.username)
        if user and user.password == auth.password:
            return user.priority
