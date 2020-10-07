JC=javac
JV=java
MAIN=CalcOfPi

all: $(MAIN).class

$(MAIN).class: $(MAIN).java ConsoleColors.class
	$(JC) $(MAIN).java

ConsoleColors.class: ConsoleColors.java
	$(JC) ConsoleColors.java

run: $(MAIN).class
	$(JV) $(MAIN) 0 & \
	$(JV) $(MAIN) 1 & \
	$(JV) $(MAIN) 2 & \
	$(JV) $(MAIN) 3 & \
	wait

clean:
	rm -f *.class