## Bin2Box

### Usage (GUI - for all users)

* Open "Bin2Box.jar" file with Java (download and install if you do not have);
* Click in "File" > "Open file" to open one sbg, or "File" > "Open folder" to add more sbg files;
* Click in "Convert" > "to Monaural..." or "to Harmonic Box X..." to start of conversion;
* Click in "Clean" > "Clean list" to clean list and convert more sbg files.

### Usage (Command Line - for advanced users)

Open terminal (or command prompt on Windows) and navigate to the Bin2Box folder.

For OSX/Linux users: you can simplify the process below replacing the "java -jar Bin2Box.jar" by a bash script (e.g: bin2box). Is very easy to do, see:

```
#!/bin/bash

java -jar /path/for/Bin2Box/Bin2Box.jar "$@"
```

Move the "Bin2Box.jar" file to a secure folder, and the script "bin2box" to any folder "bin" referenced in $PATH (e.g: /usr/bin).

* To convert sbgs to Harmonic Box, simply use:

```
java -jar Bin2Box.jar <path_of_sbg_files>
```

* To convert sbgs to Monaural beat, use **--monaural** option:

```
java -jar Bin2Box.jar --monaural <path_of_sbg_files>
```

* To specify another directory to save converted sbg files, use **--output=DIR** option:

```
java -jar Bin2Box.jar --output=<path_of_dir_to_save_sbg_files> <path_of_sbg_files>
```

* To view program version, use **--version** option:

```
java -jar Bin2Box.jar --version
```

* And to view help message, use **--help** option:

```
java -jar Bin2Box.jar --help
```
