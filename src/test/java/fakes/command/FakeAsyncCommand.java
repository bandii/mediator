package fakes.command;

import hu.ajprods.ICommand;

import java.util.concurrent.Future;

public class FakeAsyncCommand
        implements ICommand<Future<String>> {

    public String message;

    public FakeAsyncCommand(String message) {
        this.message = message;
    }
}
