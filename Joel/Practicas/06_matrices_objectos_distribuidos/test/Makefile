all: test

%.o: %.c
	gcc $< -c

test: $(patsubst %.c, %.o, $(wildcard *.c))
	gcc $^ -o $@ -Wall

run: test
	./test

clean:
	rm -f *.o test