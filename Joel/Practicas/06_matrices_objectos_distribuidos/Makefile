JC=javac
JV=java
S=ServerRMI
C=ClientRMI

all: $(S).class $(C).class

InterfaceRMI.class: InterfaceRMI.java
	$(JC) $<

ConsoleColors.class: ConsoleColors.java
	$(JC) $<

$(S).class: $(S).java ConsoleColors.class InterfaceRMI.class
	$(JC) $<

$(C).class: $(C).java ConsoleColors.class InterfaceRMI.class
	$(JC) $<

run_client_4: $(C).class
	$(JV) $(C) 4 13.68.181.122:1099 13.82.215.91:1099 13.92.225.140:1099 13.82.45.250:1099

run_client_500: $(C).class
	$(JV) $(C) 500 13.68.181.122:1099 13.82.215.91:1099 13.92.225.140:1099 13.82.45.250:1099

run_remote_server_4: $(S).class
	$(JV) $(S) 4 1099

run_remote_server_500: $(S).class
	$(JV) $(S) 500 1099

clean:
	rm -f *.class
	