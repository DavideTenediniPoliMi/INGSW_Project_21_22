package it.polimi.ingsw.network.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.network.parameters.ResponseParameters;
import it.polimi.ingsw.view.cli.CLI;

public class MessageConsumer implements Runnable{
    private final MessageQueue<String> queue;
    private volatile boolean runFlag;
    private final CLI cli;

    public MessageConsumer(MessageQueue<String> queue, CLI cli) {
        this.queue = queue;
        this.cli = cli;
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
            boolean reqInteraction = cli.nextState(jsonObject);
            System.out.println("before: " + reqInteraction);
            new ResponseParameters().deserialize(jsonObject);
            System.out.println("after dese");

            if(reqInteraction) {
                cli.handleInteraction(); //Handle interaction in new view
            } else {
                if(MatchInfo.getInstance().getCurrentPlayerID() > -1)
                    cli.displayState();
            }
        }
    }

    public void stop() {
        runFlag = false;
        queue.notifyAllForEmpty();
    }
}
