import socket
import time
import sys

from _thread import *

host = ''
port = 2626

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

try:
	s.bind((host,port))
except socket.error as e:
	print(str(e))


def th_client(data, addr):
	print("received : "+data.decode('utf-8')+" from "+addr[0]+':'+str(addr[1]))
	s.sendto(str.encode('Welcome, type your info\n'), addr)

	
	#while True:
	#	data = conn.recv(2048)
	#	reply = 'Server output: '+ data.decode('utf-8')
	#	if not data:
	#		break
	#	conn.sendall(str.encode(reply))
	#conn.close()

while True:
	data, addr = s.recvfrom(1024)
	
	#print('connected to: ')
	addv = ('192.168.2.17', 2626)
	start_new_thread(th_client, (data, addr))