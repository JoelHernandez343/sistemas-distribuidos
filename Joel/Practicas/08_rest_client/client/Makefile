JC=javac
JV=java
MAIN=Client
JAR=gson-2.8.6.jar

all: $(MAIN)

$(MAIN): $(patsubst %.java, %.class, $(wildcard *.java)) $(JAR)

%.class: %.java
	$(JC) $< -cp $(JAR):.

run: $(MAIN) $(JAR)
	$(JV) -cp $(JAR):. $(MAIN) 52.188.174.9

clean:
	rm -f *.class

