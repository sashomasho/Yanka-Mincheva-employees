package com.sirma.io;

import java.util.List;

public interface FileIO {

    List<String> read(String file);

    void write(String fileContent, String file);

}
