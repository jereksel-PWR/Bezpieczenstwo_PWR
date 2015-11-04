#include <string>
#include <keystore.hpp>

int main(int argc, char **argv) {

    if (argc < 2) {
        return -1;
    }

    keystore *myKeystore = keystore::loadFromFile(argv[0]);

    if (std::string(argv[1]) == "list") {



    } else if (std::string(argv[1]) == "add") {


    } else if (std::string(argv[1]) == "remove") {


    }


}