package it.polimi.ingsw.network.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.network.parameters.ResponseParameters;
import it.polimi.ingsw.view.cli.CLI;

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

    public void stop() {
        runFlag = false;
        queue.notifyAllForEmpty();
    }
}
