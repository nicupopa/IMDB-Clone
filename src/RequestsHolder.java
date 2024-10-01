package org.example;

import java.util.ArrayList;
import java.util.List;

public class RequestsHolder {
    static List<Request> requests = new ArrayList<>();

    static void addRequest(Request request) {
        requests.add(request);
    }

    static void remRequest(Request request) {
        requests.remove(request);
    }
}

