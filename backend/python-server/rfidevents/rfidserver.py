#!/usr/bin/env python3

""" Set up a socket which, when connected, sends detected RDID events. """

import socket
import sys
import threading
import time

from pirc522 import rfid
import RPi.GPIO

INTERFACE = "localhost"
PORT = 1111


def socket_write(sock, string):
    """ Write string to socket."""
    sock.send(string.encode())


def show_rfid(sock, rdr, uid):
    """Display data for detected chip."""
    msg = "data: UID: %d %d %d %d\n\n" % (uid[0], uid[1], uid[2], uid[3])
    socket_write(sock, msg)
    if not rdr.select_tag(uid):
        # Auth for block 10 (block 2 of sector 2) using default
        # shipping key A
        if not rdr.card_auth(rdr.auth_a,
                             10,
                             [0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF],
                             uid):
            # This will print something like
            # (False, [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0])
            socket_write(sock, "data: block 10: %s\n\n" % str(rdr.read(10)))
            # Always stop crypto1 when done working
            rdr.stop_crypto()


def rfid_listener(rdr, sock):
    """ Listen for rfid messages and print them on socket."""
    try:
        socket_write(sock, ": Starting rfid watcher\n\n")
        while True:
            (error, data) = rdr.request()
            if not error:
                socket_write(sock, ": Tag detected\n\n")
                (error, uid) = rdr.anticoll()
                if not error:
                    show_rfid(sock, rdr, uid)
            time.sleep(0.5)
    except KeyboardInterrupt:
        rdr.cleanup()
        sock.close()
        print(": Thread interrupted\n")
        sys.exit(1)


def main():
    """Indeed: main function."""
    RPi.GPIO.setwarnings(False)
    rdr = rfid.RFID()
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        s.bind((INTERFACE, PORT))
        s.listen(0)  # max backlog of connections
        while True:
            client_sock, address = s.accept()
            print('Connection from {}:{}'.format(address[0], address[1]))
            client_thread = threading.Thread(
                target=rfid_listener,
                args=(rdr, client_sock)
            )
            client_thread.start()


if __name__ == "__main__":
    main()
