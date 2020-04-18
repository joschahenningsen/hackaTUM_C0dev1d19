import threading
import requests
import socket
import json
import sys
import urllib3
import psycopg2

URL = 'https://api.theopenvent.com/exampledata/v2/data'
urllib3.disable_warnings()
data = requests.get(URL, verify=False).json()  # TODO fix ssl


print ("Opened database successfully")


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
        self._sleepperiod = 1
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
    def __init__(self, name='AlarmListener', _conn=None,_threads=None,_handler=None):
        self._stopevent = threading.Event()
        self._sleepperiod = 0.5
        self._conn = _conn
        self._threads=_threads
        self._handler=_handler

        threading.Thread.__init__(self, name=name)

    def run(self):
        while not self._stopevent.is_set():
            try:
                msg = self._conn.recv(1024)
                if msg.decode()=='':
                    break
                print("resv")
            except socket.error as serr:
                print(serr)
            msg = msg.decode().strip().split(":")
            if msg[0] == "start":
                msg[1] = msg[1].split(",")
                print("new alarmmsg",msg)
                if msg[1] is not None:
                    for mi in msg[1]:
                        if mi in self._threads:
                            self._handler.addalarm(_id=mi, _conn=self._conn)
            elif msg[0] == "pause":
                print("neuer Datenbankeintrag")
                print(msg[1])
                req=data
                for key,value in req.items():
                    conndb = psycopg2.connect(database="thobian", user="thobian", password="infineon", # piep
                                              host="127.0.0.1", port="5432")
                    temp=value['processed']['triggerSettings']
                    cursor = conndb.cursor()
                    cursor.execute("INSERT INTO screenshots (fio2,ie,mve,peep,rr,vt, humidity, pressure_max, id,vent) VALUES(%d,%d, %d, %d, %d, %d, %d, %d, $token$%s$token$,$token$%s$token$)" % (temp['FiO2'],temp['IE'],temp['MVe'],temp['PEEP'],temp['RR'],temp['VT'], temp['humidity'], temp['pressure_max'],msg[1],key))
                    conndb.commit()
                cursor.close()
            elif msg[0] == "resume":
                conndb = psycopg2.connect(database="thobian", user="thobian", password="infineon",
                                          host="127.0.0.1", port="5432")
                print(msg[1])
                print("wieder zurück aus pause")
                cursor = conndb.cursor()
                print("test1")
                # cursor.execute("SELECT fio2, ie, mve, peep, rr, vt, humidity, pressure_max, vent  from screenshots where id=$token$%s$token$"%(msg[1]))
                print("test2")
                rows = cursor.fetchall()
                print("test3")
                dict2 = {}
                for row in rows:
                    dict2[row[9]]={'FiO2':row[0], 'IE':row[1],'MVe':row[2],'PEEP':row[4],'RR':row[5],'VT':row[6],'humidity':row[7],'pressure_max':row[8]}
                print("testfinal")
                print(("%s\n" % json.dumps(dict2)))
                self._conn.send(("%s\n" % json.dumps(dict2)).encode())
                cursor.execute("DELETE from screenshots where id=$token$%s$token$" % (msg[1]))
                conndb.commit()
                cursor.close()
                conndb.close()


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
            tr = AlarmListener(_conn=aconn,_threads=self._threads,_handler=self._handler)
            tr.daemon = True
            tr.start()

