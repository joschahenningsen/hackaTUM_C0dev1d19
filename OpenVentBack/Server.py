import socket
import threading
import requests
import json
from random import randrange
import sys

URL = 'http://api.theopenvent.com/exampledata/v2/data'



def addThreads():
    result={}
    req=requests.get(URL).json()
    for key, value in req.items():
        t = UpdateThread(name="UpdateThread %s"% (key,), nr=key)
        t.start()
        result[str(req[key]['device_id'])]=t

    return result

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
                    req = "%s\n" % json.dumps(requests.get(URL).json()[str(self._nr)])
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

99
if __name__ == '__main__':
    NUMBER_OF_DEVICES = 5
    TCP_PORT = 5005
    BUFFER_SIZE = 1024
    count = 1
    threads = addThreads()

    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.setsockopt(socket.SOL_SOCKET,socket.SO_REUSEADDR, 1)
    print(socket.gethostname())
    s.bind(('88.130.86.8', TCP_PORT))
    s.listen(NUMBER_OF_DEVICES)

    while True:
        conn, addr = s.accept()
        print('Connection address:', addr)
        t = ListenThread(name=count, _conn=conn, _threads=threads)
        count += 1
        t.start()
