import threading
import requests
import socket
import json
import sys
import urllib3

URL = 'https://api.theopenvent.com/exampledata/v2/data'
urllib3.disable_warnings()
data = requests.get(URL, verify=False).json()  # TODO fix ssl


class DataFetcher(threading.Thread):
    def __init__(self, name='DataFetcher', url='https://api.theopenvent.com/exampledata/v2/data'):
        self._stopevent = threading.Event()
        self._sleepperiod = 0.5
        self._url = url
        threading.Thread.__init__(self, name=name)

    def run(self):
        while True:
            global data
            data = requests.get(self._url, verify=False).json()                        # TODO fix ssl
            self._stopevent.wait(self._sleepperiod)


class UpdateThread(threading.Thread):

    def __init__(self, name='UpdateThread', nr=None):
        self._stopevent = threading.Event()
        self._sleepperiod = 0.5
        self._recievers = []
        self._nr = nr

        threading.Thread.__init__(self, name=name)

    def run(self):
        print("%s starts" % (self.getName(),))

        while not self._stopevent.is_set():
            for c in self._recievers:
                try:
                    req = "%s\n" % json.dumps(data[str(self._nr)])
                    c.send(req.encode())
                except socket.error:
                    print("Removed from %s" % (self.getName()))
                    self._recievers.remove(c)
            self._stopevent.wait(self._sleepperiod)

        print("%s ends" % (self.getName(),))

    def join(self, timeout=None):
        self._stopevent.set()
        threading.Thread.join(self, timeout)

    def add(self, _conn=None):
        if _conn is not None:
            self._recievers.append(_conn)


class ListenThread(threading.Thread):

    def __init__(self, name='ListenThread', _conn=None, _threads=None):
        self._stopevent = threading.Event()
        self._sleepperiod = 0.5
        self._conn = _conn
        self._threads = _threads

        threading.Thread.__init__(self, name=name)

    def run(self):
        while not self._stopevent.is_set():
            try:
                msg = self._conn.recv(1024)
                msg = msg.decode()
                if msg in self._threads:
                    self._threads[msg].add(self._conn)
            except socket.error:
                break

        print("Connection %s Lost" % (self.getName(),))
        self._conn.close()
        sys.exit()

    def join(self, timeout=None):
        self._stopevent.set()
        threading.Thread.join(self, timeout)


class AlarmHandler(threading.Thread):
    def __init__(self, name='AlarmHandler', threads=None):
        self._stopevent = threading.Event()
        self._sleepperiod = 1
        self._alarmsList = {}
        self._alarmsTriggered = {}
        for key, value in threads.items():
            self._alarmsList[key] = []
        threading.Thread.__init__(self, name=name)

    def checkvalues(self):
        for key, vent in data.items():
            co2 = vent['processed']['ExpiredCO2']
            if co2 > 5.2 and key not in self._alarmsTriggered:
                print("Alarm ", str(vent['device_id']))
                self._alarmsTriggered[key] = True
                return str(vent['device_id'])
            elif co2 <= 4.8 and key in self._alarmsTriggered:
                del self._alarmsTriggered[key]

        return None

    def run(self):
        while True:
            alarmid = self.checkvalues()
            if alarmid is not None:
                for si in self._alarmsList[alarmid]:
                    try:
                        si.send(("%s\n" % alarmid).encode())
                    except socket.error:
                        print("Removed Alarm from %s" % (self.getName()))
                        self._alarmsList[alarmid].remove(si)
            self._stopevent.wait(self._sleepperiod)

    def addalarm(self, _id, _conn):
        if _conn not in self._alarmsList[_id]:
            self._alarmsList[_id].append(_conn)

class AlarmListener(threading.Thread):
    def __init__(self, name='AlarmListener', _conn=None):
        self._stopevent = threading.Event()
        self._sleepperiod = 0.5
        self._conn = _conn

        threading.Thread.__init__(self, name=name)

    def run(self):
        while not self._stopevent.is_set():
            try:
                msg = self._conn.recv(1024)
                print(msg.decode())
                msg = msg.decode().strip().split(":")
                msg[1] = msg[1].split(",")
                if msg[0] == "start":
                    print("new alarmmsg",msg)
                    if msg[1] is not None:
                        for mi in msg[1]:
                            if mi in self._threads:
                                self._handler.addalarm(_id=mi, _conn=self._conn)
                elif msg[0] == "pause":
                    print("neuer Datenbankeintrag")
                    # entfrenen aus handler
                elif msg[0] == "resume":
                    print("wieder zurück aus pause")
            except socket.error as serr:
                print(serr)

        print("Connection %s Lost" % (self.getName(),))
        self._conn.close()
        sys.exit()

    def join(self, timeout=None):
        self._stopevent.set()
        threading.Thread.join(self, timeout)

class AlarmHead(threading.Thread):
    def __init__(self, name='AlarmHead', threads=None, serveraddress=None):
        self._threads = threads
        self._serveraddress = serveraddress
        self._handler = AlarmHandler(threads=threads)
        self._handler.daemon = True
        self._handler.start()

        threading.Thread.__init__(self, name=name)

    def run(self):

        a = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        a.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)  # TODO Allow multiple users to connect at the same time
        a.bind((self._serveraddress, 5010))
        a.listen(5)
        print("Waiting for Connections")
        while True:
            aconn, aaddr = a.accept()
            print("Connection on Alarm:", aaddr)

