import psycopg2
from Threads import *

# conndb = psycopg2.connect(database="thobian", user="thobian", password="infineon", host="127.0.0.1", port="5432")

print ("Opened database successfully")



def addthreads():
    result = {}
    req = data
    for key, value in req.items():
        tr = UpdateThread(name="UpdateThread %s" % (key,), nr=key)
        tr.daemon = True
        tr.start()
        result[str(req[key]['device_id'])] = tr

    return result

def startthreads():
    fetcher = DataFetcher()
    fetcher.daemon = True
    fetcher.start()

    alarm = AlarmHead(threads=threads, serveraddress=serveraddress)
    alarm.daemon = True
    alarm.start()

if __name__ == '__main__':
    BUFFER_SIZE = 1024
    count = 1
    serveraddress = socket.gethostname()
    threads = addthreads()

    startthreads()

    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    s.bind((serveraddress, 5005))
    s.listen(5)

    while True:
        conn, addr = s.accept()
        print('Connection address:', addr)
        t = ListenThread(name=count, _conn=conn, _threads=threads)
        count += 1
        t.start()
