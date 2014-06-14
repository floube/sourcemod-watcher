package eu.styriagames;

import java.io.File;

public class FileChange {
    public File reference;
    public long lastModified;
    
    public FileChange(File file) {
        reference = file;
        lastModified = file.lastModified();
    }
}
