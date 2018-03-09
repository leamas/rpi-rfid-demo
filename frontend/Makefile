TARGET_HOST      = nti-rpi.duckdns.org
CONTEXT          = rfid-ui
MAVEN_DEBUG_OPTS = \
    -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n
MAVEN_RUN_OPTS   =  -Dexec.mainClass=eu.ntig.Application


all:
	mvn compile

war:
	mvn compile war:war

run-debug:
	cmd="MAVEN_OPTS=$(MAVEN_DEBUG_OPTS) mvn exec:java $(MAVEN_RUN_OPTS)"; \
	sudo bash -c "$$cmd"

run:
	mvn exec:java $(MAVEN_RUN_OPTS)

deploy:
	scp target/*war \
	    tomcat8@$(TARGET_HOST):/var/lib/tomcat8/webapps/$(CONTEXT).war

format-pom:
	XMLLINT_INDENT='    ' xmllint --format pom.xml > foo.xml \
	    && cp foo.xml pom.xml && rm foo.xml

