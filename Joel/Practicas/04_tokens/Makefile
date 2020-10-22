JC=javac
JV=java
MAIN=Token

all: $(MAIN).class

ConsoleColors.class: ConsoleColors.java
	$(JC) $<

$(MAIN).class: $(MAIN).java ConsoleColors.class
	$(JC) $<

run0: $(MAIN).class
	$(JV) $(MAIN) 0 13.90.155.83

run1: $(MAIN).class
	$(JV) $(MAIN) 1 40.76.92.254

run2: $(MAIN).class
	$(JV) $(MAIN) 2 52.170.198.10

run3: $(MAIN).class
	$(JV) $(MAIN) 3 13.68.251.175

clean:
	rm -f *.class