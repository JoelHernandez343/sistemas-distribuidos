JC=javac
JV=java
MAIN=Example
JAR=gson-2.8.6.jar

all: $(MAIN).class

$(MAIN).class: $(MAIN).java $(JAR)
	$(JC) $< -cp $(JAR)

run: $(MAIN).class
	$(JV) -cp $(JAR):. $(MAIN) 

clean:
	rm -f *.class
