rpi-rfid-demp README
====================

Overview
--------

This is a simple demo reading events from a RFID reader and presenting them
in a browser. The demo is constructed from a backend and a frontend. The
communication in between is based on HTML5 server-side events.

The frontend is a simple page which just presents data from the backend using
an EventSource javascript object. The page is packed to a webapp using maven.
The backend is a plain Servlet build on top of Pi4j-RC522. The servlet
polls the RFID reader and sends data on available tag if available. The
whole thing is a maven artifact.

Running
-------

- Clone the github repo onto the RPi machine.
- Install tomcat8, jdk8 and maven.

- Build the thing using maven.
- Deploy the created war file to tomcat8 on the /rfid context path.
- Build the frontend and deploy the created war file on tomcat using the
  /rfid context path.
- Access the UI page bu pointing your browser to something like::

    http://myhost.mydomain:8080/rfid/ui

State
-----

Basically, a mess. It's just a demno.

License
-------
Copyright (c) 2018 Alec Leamas, all rights reserved. This work is distributed
under the MIT license

