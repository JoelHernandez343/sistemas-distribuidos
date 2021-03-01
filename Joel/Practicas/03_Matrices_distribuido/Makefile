JC=javac
JV=java
MAIN=MatrixMultiplication

all: $(MAIN).class

ConsoleColors.class: ConsoleColors.java
	$(JC) $<

$(MAIN).class: $(MAIN).java ConsoleColors.class
	$(JC) $<

run4: $(MAIN).class
	$(JV) $(MAIN) 0 4 & \
	$(JV) $(MAIN) 1 & \
	$(JV) $(MAIN) 2 & \
	$(JV) $(MAIN) 3 & \
	$(JV) $(MAIN) 4

run1000: $(MAIN).class
	$(JV) $(MAIN) 0 1000 & \
	$(JV) $(MAIN) 1 & \
	$(JV) $(MAIN) 2 & \
	$(JV) $(MAIN) 3 & \
	$(JV) $(MAIN) 4 & \
	wait

clean:
	rm -f *.class