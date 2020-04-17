import socket
import threading
import time


class UpdateThread(threading.Thread):
    def __init__(self, name='UpdateThread'):
        self._stopevent = threading.Event()
        self._sleepperiod = 1.0
        self._recievers = []

        threading.Thread.__init__(self, name=name)

    def run(self):
        print("%s starts" % (self.getName(),))

        while not self._stopevent.is_set():
            for c in self._recievers:
                try:
                    c.send(b'Hello')
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
    # nametest

    while True:
        conn, addr = s.accept()
        print('Connection address:', addr)
        t.add(conn)
    conn.close()
    t.join()



