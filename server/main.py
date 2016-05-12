from auth import AuthHelper
from db import app, db_helper, config_helper as ch

auth = AuthHelper(db_helper)


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


@app.route('/Mart/api/v1.0/read-record')
def read_record():
    pass


@app.route('/Mart/api/v1.0/write-record')
def write_record():
    pass


@app.route('/Mart/api/v1.0/read-record/<id>')
def read_record_with_id(id):
    pass


@app.route('/Mart/api/v1.0/write-record/<id>')
def write_record_with_id(id):
    pass

if __name__ == '__main__':
    app.run(host=ch.server.ip, port=ch.server.port, debug=ch.debug)
