import ConfigParser


class ServerConfig:

    def __init__(self, ip, port):
        self._ip = ip
        self._port = int(port)

    @property
    def ip(self):
        return self._ip

    @property
    def port(self):
        return self._port


class DBConfig:

    def __init__(self, host, port, login, password, name):
        self._h = host
        self._po = port
        self._l = login
        self._pa = password
        self._n = name

    @property
    def host(self):
        return self._h

    @property
    def port(self):
        return self._po

    @property
    def login_name(self):
        return self._l

    @property
    def password(self):
        return self._pa

    @property
    def name(self):
        return self._n


class ConfigHelper:

    def __init__(self, filename=None):
        if not filename:
            filename = "config"
        cp = ConfigParser.SafeConfigParser()
        cp.read(filename)
        try:
            self._server = ServerConfig(cp.get("server", "server_ip"),
                                        cp.get("server", "server_port"))
            self._db = DBConfig(cp.get("db", "db_host"),
                                cp.get("db", "db_port"),
                                cp.get("db", "db_login_name"),
                                cp.get("db", "db_password"),
                                cp.get("db", "db_name"))
            self._debug = cp.getboolean("debug", "debug")
        except Exception as e:
            print e.message
            print "There may be some error in config file."
            print "Some variables may use defualt value."

    @property
    def server(self):
        return self._server

    @property
    def db(self):
        return self._db

    @property
    def debug(self):
        return self._debug

    @property
    def db_URI(self):
        return "mysql+mysqlconnector://" + self._db.login_name +\
            ":" + self._db.password + "@" + self._db.host + \
            ":" + self._db.port + "/" + self._db.name
