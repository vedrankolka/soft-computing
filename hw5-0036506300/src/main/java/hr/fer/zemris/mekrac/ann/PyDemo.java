package hr.fer.zemris.mekrac.ann;

import java.io.*;

public class PyDemo {

    public static void main(String[] args) throws IOException {
        Process pythonProcess = Runtime.getRuntime().exec("/usr/bin/python3 src/main/python/ann.py");
        // pythons stdOut, meaning our stdIn from him
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(pythonProcess.getInputStream()));
        // pythons stdIn, meaning out stdOut to him
        BufferedWriter stdOut = new BufferedWriter(new OutputStreamWriter(pythonProcess.getOutputStream()));
        //BufferedReader stdErr = new BufferedReader(new InputStreamReader(pythonProcess.getErrorStream()));
        // expected "python script is running...
        String line = stdIn.readLine();
        System.out.println(line);
        // write to python script
        stdOut.write("Hello!\n");
        stdOut.flush();
        // expect to get Hello back
        line = stdIn.readLine();
        System.out.println(line);
    }
}
