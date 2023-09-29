package com.company;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        View view = new View();
        controller.setView(view);
        view.setController(controller);

        controller.start();
    }
}
