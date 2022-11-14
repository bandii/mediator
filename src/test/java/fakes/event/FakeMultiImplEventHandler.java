package fakes.event;

import fakes.IFakeAdvancedBusinessLogic;
import fakes.IFakeBusinessLogic;
import hu.ajprods.IEventHandler;

import java.util.ArrayList;
import java.util.List;

public class FakeMultiImplEventHandler
        implements IFakeBusinessLogic<String>,
                   IEventHandler<FakeAEvent>,
                   IFakeAdvancedBusinessLogic<String> {

    public List<FakeAEvent> eventsHandled = new ArrayList<>();

    @Override
    public void handle(FakeAEvent command) {
        System.out.println(command.message + " got handled");

        eventsHandled.add(command);
    }

    @Override
    public String doPrint(String param) {
        return null;
    }

    @Override
    public boolean shouldPrint(String param) {
        return false;
    }
}
