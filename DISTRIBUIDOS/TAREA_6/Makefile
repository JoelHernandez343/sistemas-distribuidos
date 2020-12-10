JC=javac
JV=java
S=ServerRMI
C=ClientRMI

all: $(S).class $(C).class

InterfaceRMI.class: InterfaceRMI.java
	$(JC) $<

$(S).class: $(S).java  InterfaceRMI.class
	$(JC) $<

$(C).class: $(C).java InterfaceRMI.class
	$(JC) $<

run_client_4: $(C).class
	$(JV) $(C) 4 104.214.24.63:1099 104.214.32.59:1099 13.66.23.184:1099 13.66.1.138:1099

run_client_500: $(C).class
	$(JV) $(C) 500 104.214.24.63:1099 104.214.32.59:1099 13.66.23.184:1099 13.66.1.138:1099

run_remote_server_4: $(S).class
	$(JV) $(S) 4 1099

run_remote_server_500: $(S).class
	$(JV) $(S) 500 1099

clean:
	rm -f *.class