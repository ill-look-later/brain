#include <stdio.h>

int main() {
	char cells[30000] = {0};
	char* ptr = cells;

	*ptr += 8;

	while(*ptr) {

	ptr += 1;

	*ptr += 4;

	while(*ptr) {

	ptr += 1;

	*ptr += 2;

	ptr += 1;

	*ptr += 3;

	ptr += 1;

	*ptr += 3;

	ptr += 1;

	*ptr += 1;

	ptr += -4;

	*ptr += -1;
	}

	ptr += 1;

	*ptr += 1;

	ptr += 1;

	*ptr += 1;

	ptr += 1;

	*ptr += -1;

	ptr += 2;

	*ptr += 1;

	while(*ptr) {

	ptr += -1;
	}

	ptr += -1;

	*ptr += -1;
	}

	ptr += 2;

	printf("%c", *ptr);

	ptr += 1;

	*ptr += -3;

	printf("%c", *ptr);

	*ptr += 7;

	printf("%c", *ptr);

	printf("%c", *ptr);

	*ptr += 3;

	printf("%c", *ptr);

	ptr += 2;

	printf("%c", *ptr);

	ptr += -1;

	*ptr += -1;

	printf("%c", *ptr);

	ptr += -1;

	printf("%c", *ptr);

	*ptr += 3;

	printf("%c", *ptr);

	*ptr += -6;

	printf("%c", *ptr);

	*ptr += -8;

	printf("%c", *ptr);

	ptr += 2;

	*ptr += 1;

	printf("%c", *ptr);

	ptr += 1;

	*ptr += 2;

	printf("%c", *ptr);
	return 0;
}