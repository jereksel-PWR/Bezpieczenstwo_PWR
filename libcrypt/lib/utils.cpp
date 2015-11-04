#include <stdio.h>
#include <malloc.h>
#include "utils.hpp"

//Works nice with \0 in files
std::string *fileToString(char *file_location) {

    char *buffer = NULL;
    long length;

    FILE *file = fopen(file_location, "rb");

    fseek(file, 0, SEEK_END);
    length = ftell(file);
    fseek(file, 0, SEEK_SET);
    buffer = (char *) malloc(length * sizeof(char));
    if (buffer)
        fread(buffer, 1, length, file);
    fclose(file);

    return new std::string(buffer, length);

}
