all: CentralServer.class ClienteMulti.class DistritoServer.class

CentralServer.class: CentralServer.java
	javac CentralServer.java

ClienteMulti.class: ClienteMulti.java
	javac ClienteMulti.java

DistritoServer.class: DistritoServer.java
	javac DistritoServer.java

Cliente:
	java ClienteMulti

ServidorCentral:
	java CentralServer

Distrito:
	java DistritoServer

clean :
	rm -f *.class


