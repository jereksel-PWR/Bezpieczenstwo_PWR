#include <ao/ao.h>
#include <mpg123.h>
#include <iostream>

#define BITS 8

//Travis hax
#if MPG123_API_VERSION < 40
int mpg123_encsize(int encoding) {
    return 0;
}
#endif

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

//Kanged from https://hzqtc.github.io/2012/05/play-mp3-with-libmpg123-and-libao.html
int main(int argc, char *argv[]) {
    mpg123_handle *mh;
    unsigned char *buffer;
    size_t buffer_size;
    size_t done;
    int err;

    int driver;
    ao_device *dev;

    ao_sample_format format;
    int channels, encoding;
    long rate;

    if (argc < 2)
        exit(0);

    /* initializations */
    ao_initialize();
    driver = ao_default_driver_id();
    mpg123_init();
    mh = mpg123_new(NULL, &err);
    buffer_size = mpg123_outblock(mh);
    buffer = (unsigned char *) malloc(buffer_size * sizeof(unsigned char));

    mpg123_open_feed(mh);
    std::string *music = fileToString(argv[1]);

    mpg123_feed(mh, (const unsigned char *) music->data(), music->size());

    mpg123_getformat(mh, &rate, &channels, &encoding);

    /* set the output format and open the output device */
    format.bits = mpg123_encsize(encoding) * BITS;
    format.rate = rate;
    format.channels = channels;
    format.byte_format = AO_FMT_NATIVE;
    format.matrix = 0;
    dev = ao_open_live(driver, &format, NULL);

    /* decode and play */
    while (mpg123_read(mh, buffer, buffer_size, &done) == MPG123_OK)
        ao_play(dev, (char *) buffer, (uint_32) done);

    /* clean up */
    free(buffer);
    ao_close(dev);
    mpg123_close(mh);
    mpg123_delete(mh);
    mpg123_exit();
    ao_shutdown();

    return 0;
}
