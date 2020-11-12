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
	$(JV) $(C) 4 127.0.0.1:1099 127.0.0.1:1100 127.0.0.1:1101 127.0.0.1:1102

run_client_500: $(C).class
	$(JV) $(C) 500 127.0.0.1:1099 127.0.0.1:1100 127.0.0.1:1101 127.0.0.1:1102

clean:
	rm -f *.class