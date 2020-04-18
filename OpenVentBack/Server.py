import socket
import threading
import requests, certifi
import json
import sys, urllib3

URL = 'https://api.theopenvent.com/exampledata/v2/data'
urllib3.disable_warnings()
data = requests.get(URL,verify=False).json()                                      # TODO fix ssl


def addThreads():
    result={}
    req= data
    for key, value in req.items():
        t = UpdateThread(name="UpdateThread %s"% (key,), nr=key)
        t.start()
        result[str(req[key]['device_id'])]=t

    return result

class DataFetcher(threading.Thread):
    def __init__(self, name='DataFetcher'):
        self._stopevent = threading.Event()
        self._sleepperiod = 0.5
        threading.Thread.__init__(self, name=name)

    def run(self):
        while True:
            global data
            data = requests.get(URL,verify=False).json()                        # TODO fix ssl
            self._stopevent.wait(self._sleepperiod)

class UpdateThread(threading.Thread):

    def __init__(self, name='UpdateThread',nr=None):
        self._stopevent = threading.Event()
        self._sleepperiod = 0.5
        self._recievers = []
        self._nr=nr

        threading.Thread.__init__(self, name=name)

    def run(self):
        print("%s starts" % (self.getName(),))

        while not self._stopevent.is_set():
            for c in self._recievers:
                try:
                    req = "%s\n" % json.dumps(data[str(self._nr)])
                    c.send(req.encode())
                except socket.error:
                    print("Removed from %s"%(self.getName()))
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
            except socket.error as serr:
                break

        print("Connection %s Lost" % (self.getName(),))
        self._conn.close()
        sys.exit()

    def join(self, timeout=None):
        self._stopevent.set()
        threading.Thread.join(self, timeout)

class AlarmHandler(threading.Thread):
    def __init__(self, name='AlarmHandler'):
        threading.Thread.__init__(self, name=name)

    def checkValues(self):
        return True

    def run(self):
        alarmID = self.checkValues()
        if alarmID != -1:
            print("Alarm")


class AlarmListener(threading.Thread):
    def __init__(self, name='AlarmListener'):

        threading.Thread.__init__(self, name=name)

    def run(self):
        a = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        a.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)   # TODO Alllow multiple users to connect at the same time
        a.bind((serveraddress, ALARM_PORT))
        a.listen(NUMBER_OF_DEVICES)
        a.accept()
        while True:
            aconn, aaddr = a.accept()
            try:
                msg = aconn.recv(1024)
                msg = msg.decode().split(str=",")
                if msg is not None:
                    self._threads[msg].add(self._conn)
            except socket.error as serr:
                print(serr)



if __name__ == '__main__':
    NUMBER_OF_DEVICES = 5
    MSG_PORT = 5005
    ALARM_PORT = 5010
    BUFFER_SIZE = 1024
    count = 1
    serveraddress = socket.gethostname()
    threads = addThreads()

    fetcher = DataFetcher()
    fetcher.daemon = True
    fetcher.start()

    """alarm = AlarmListener()
    alarm.daemon = True
    alarm.start()"""

    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.setsockopt(socket.SOL_SOCKET,socket.SO_REUSEADDR, 1)
    s.bind((serveraddress, MSG_PORT))
    s.listen(NUMBER_OF_DEVICES)





    while True:
        conn, addr = s.accept()
        print('Connection address:', addr)
        t = ListenThread(name=count, _conn=conn, _threads=threads)
        count += 1
        t.start()
