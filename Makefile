RPI_HOST           = nti-rpi.duckdns.org

deploy:
	scp target/rpi-rfid-server.war  $(RPI_HOST):/var/lib/tomcat8/webapps
