JC=javac
JV=java
MAIN=MatrixMultiplication
MAIN2=MatrixMultiplication2

all: $(MAIN).class $(MAIN2).class

ConsoleColors.class: ConsoleColors.java
	$(JC) $<

$(MAIN).class: $(MAIN).java ConsoleColors.class
	$(JC) $<

$(MAIN2).class: $(MAIN2).java ConsoleColors.class
	$(JC) $<

run100: $(MAIN).class $(MAIN2).class
	$(JV) $(MAIN) 100 && \
	$(JV) $(MAIN2) 100

run200: $(MAIN).class $(MAIN2).class
	$(JV) $(MAIN) 200 && \
	$(JV) $(MAIN2) 200

run300: $(MAIN).class $(MAIN2).class
	$(JV) $(MAIN) 300 && \
	$(JV) $(MAIN2) 300

run500: $(MAIN).class $(MAIN2).class
	$(JV) $(MAIN) 500 && \
	$(JV) $(MAIN2) 500

run1000: $(MAIN).class $(MAIN2).class
	$(JV) $(MAIN) 1000 && \
	$(JV) $(MAIN2) 1000

clean:
	rm -f *.class