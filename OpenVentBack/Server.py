import socket
import threading
import requests
import json
from random import  randrange

URL = 'http://api.theopenvent.com/exampledata/v2/data'

def simulater():
    req = requests.get(URL).json()
    req['0']['processed']['ExpiredCO2']= randrange(100)
    req['0']['processed']['ExpiredO2'] = randrange(100)
    req['0']['processed']['MVe'] = randrange(5000)
    req['0']['processed']['frequency'] = randrange(60)
    req['0']['processed']['volumePerMinute'] = randrange(10000)
    req['0']['processed']['volumePerMovement'] = randrange(1000)
    return req

class UpdateThread(threading.Thread):

    def __init__(self, name='UpdateThread'):
        self._stopevent = threading.Event()
        self._sleepperiod = 0.5
        self._recievers = []

        threading.Thread.__init__(self, name=name)

    def run(self):
        print("%s starts" % (self.getName(),))

        while not self._stopevent.is_set():
            for c in self._recievers:
                try:
                    req = "%s\n" % json.dumps(simulater())
                    c.send(req.encode())
                except socket.error:
                    self._recievers.remove(c)
            self._stopevent.wait(self._sleepperiod)

        print("%s ends" % (self.getName(),))

    def join(self, timeout=None):
        self._stopevent.set()
        threading.Thread.join(self, timeout)

    def add(self,_conn=None):
        if _conn is not None:
            self._recievers.append(_conn)

if __name__ == '__main__':
    NUMBER_OF_DEVICES = 5
    TCP_PORT = 5005
    BUFFER_SIZE = 1024


    t = UpdateThread()

    t.start()

    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((socket.gethostname(), TCP_PORT))
    s.listen(NUMBER_OF_DEVICES)

    while True:
        conn, addr = s.accept()
        print('Connection address:', addr)
        t.add(conn)
    conn.close()
    t.join()



