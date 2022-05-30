package it.polimi.ingsw.network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

/**
 * Class implementing an interruptible input scan task. Implements callable so it can be executed with multithreading
 * whilst being able to return a String.
 */
public class InterruptibleScanner implements Callable<String> {
    private final BufferedReader br;

    public InterruptibleScanner() {
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public String call() throws InterruptedException, IOException {
        /*
         * Waits until a line can be read from System.in
         * Thread.sleep to avoid busy-waiting
         */
        while(!br.ready())
            Thread.sleep(200);

        return br.readLine();
    }
}
