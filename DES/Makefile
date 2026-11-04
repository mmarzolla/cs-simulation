SRC = $(wildcard *.java)
CLASS = $(SRC:.java=.class)
ALL: $(CLASS)

.PHONY: clean

MM1.class: Simulation.class

SimTest.class: Simulation.class

%.class: %.java
	javac $<

clean:
	\rm -f *.class *~ *.csv
