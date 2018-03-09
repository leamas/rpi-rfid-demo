rpi-rfid-demp README
====================

Overview
--------

This is a simple demo reading events from a RFID reader and presenting them
in a browser. The demo is constructed from a backend and a frontend. The
communication in between is based on HTML5 server-side events.

The frontend is a simple page which just presents data from the backend using
an EventSource javascript object. The page is packed to a webapp using maven.

The backend is divided into a python low-level part and a java servlet.

The python part reads the hardware and presents data on a TCP socket. The
socket is hardcoded on address localhost:1111. Events received on this
socket are formatted according the the EventSource specs. The python code is
distributed as a pypi package.

The java servlet reads events from the python server socket and sends them to
connecting clients. It is packed as a webapp using maven.

Running
-------

- Clone the github repo onto the RPi machine.
- Install tomcat8, jdk8 and maven.
- install the backend python server using the python-server/README.md.
- Connect the hardware and start the rfidserver.service systemd unit

- Build the backend service war servlet using maven.
- Deploy the created war file to tomcat8 on the /rfid context path.

- Build the frontend and deploy the created war file on tomcat using the
  /rfid-ui context path.
- Access the UI page bu pointing your browser to something like::

    http://myhost.mydomain:8080/rfid-ui

State
-----

Basically, a mess. It's just a demno.

License
-------
Copyright (c) 2018 Alec Leamas, all rights reserved. This work is distributed
under the MIT license

