package com.company;

public class Cell {
    private boolean isBomb = false;
    private boolean isOpen = false;
    private boolean isFlag = false;
    private int number = 0;

    public void open() {
        isOpen = true;
    }

    public void makeBomb() {
        isBomb = true;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void toggleFlag() {
       isFlag = !isFlag;
    }

    public boolean isFlag() {
        return isFlag;
    }

    public boolean isEmpty() {
        return number == 0;
    }

    public void updateNumber(int number) {
        this.number = number;
    }

    public String getFileName() {
        if (isOpen) {
            return isBomb ? "bomb.png" : "num" + number + ".png";
        }

        return isFlag ? "flag_closed.png" : "cell-closed.png";
    }
}
