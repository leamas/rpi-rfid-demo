rfidserver README
=================

This is a simple server which exposes events from an RFID reader on a
TCP socket.

The RFID events are read using the pi-rc522 pypi package.

The socket is hardcoded to port 1111 on localhost. A client connecting
to this socket will receive events formatted to the HTML5 EventSource
specification, part of Server Side Events.

This is very rough code, more like a demo. Expect to patch it if used.

Installation::

    $ sudo pip3 install --prefix=/usr/local .
