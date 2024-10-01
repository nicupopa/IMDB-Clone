package org.example;

public class Exceptions {
    static class InformationIncompleteException extends Exception {
        public InformationIncompleteException() {
            super("Information is incomplete");
        }
    }

    static class InvalidCommandException extends Exception {
        public InvalidCommandException() {
            super("Invalid Command");
        }
    }
}
