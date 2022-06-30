package it.polimi.ingsw.network.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.network.parameters.ResponseParameters;
import it.polimi.ingsw.view.cli.CLI;

/**
 * Class the implements the Consumer in the Producer-Consumer pattern. It ensures that the CLI is fed messages in the
 * exact same order they arrived.
 */
public class MessageConsumer implements Runnable{
    private final MessageQueue<String> queue;
    private volatile boolean runFlag;
    private final CLI view;

    public MessageConsumer(MessageQueue<String> queue, CLI view) {
        this.queue = queue;
        this.view = view;
        runFlag = true;
    }

    @Override
    public void run() {
        consume();
    }

    /**
     * Consumes messages in the queue and feeds them to the CLI. If it consumes all the messages in the queue it'll block
     * and wait for a new messages to be added to the queue. The messages will be executed in the same order they appeared
     * in the queue (FIFO).
     */
    private void consume() {
        while (runFlag) {
            String message;
            JsonObject jsonObject;
            if(queue.isEmpty()) {
                try {
                    queue.waitOnEmpty();
                } catch (InterruptedException e) {
                    break;
                }
            }
            if(!runFlag)
                break;

            message = queue.remove();
            jsonObject = (JsonObject) JsonParser.parseString(message);
            boolean reqInteraction = view.nextState(jsonObject);
            new ResponseParameters().deserialize(jsonObject);

            if(reqInteraction && !view.isSkipping() && !view.shouldSkip()) {
                view.handleInteraction(); //Handle interaction in new view
            } else {
                if(MatchInfo.getInstance().getCurrentPlayerID() > -1)
                    view.displayState();
            }
        }
    }

    /**
     * Signals to the consumer to stop consuming messages.
     */
    public void stop() {
        runFlag = false;
        queue.notifyAllForEmpty();
    }
}
