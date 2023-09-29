package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private static final int WIDTH = 9;
    private static final int HEIGHT = 9;
    private static final int SQUARE_SIZE = 30;
    private static final int WIDTH_PIXELS = WIDTH * SQUARE_SIZE;
    private static final int HEIGHT_PIXELS = HEIGHT * SQUARE_SIZE;
    private static final int BOMB_AMOUNT = 10;

    private View view;
    private Graphics graphics;
    private Cell[][] field = new Cell[WIDTH][HEIGHT];
    private boolean isGameOver = false;
    private boolean isFirstMove = true;

    public void start() {
        view.create(WIDTH_PIXELS, HEIGHT_PIXELS);
        fillField();
        render();
    }

    private void render() {
        BufferedImage image = new BufferedImage(WIDTH_PIXELS, HEIGHT_PIXELS, BufferedImage.TYPE_INT_RGB);
        graphics = image.getGraphics();
        drawField();
        view.setImage(image);
    }

    private void draw(int x, int y, BufferedImage image) {
        graphics.drawImage(image, x * SQUARE_SIZE, y * SQUARE_SIZE, null);
    }

    private void drawField() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                draw(x, y, loadImg(field[x][y].getFileName()));
            }
        }
    }

    private BufferedImage loadImg(String fileName) {
        try {
            return ImageIO.read(new File(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleMouseClick(int mouseX, int mouseY, int mouseButton) {
        if (isGameOver) {
            return;
        }

        int x = mouseX / SQUARE_SIZE;
        int y = mouseY / SQUARE_SIZE;

        if (isFirstMove) {
            isFirstMove = false;
            generateBombs(x, y);
            placeCellNumbers();
        }

        if (mouseButton == 1) {
            if (field[x][y].isFlag()) {
                return;
            }
            open(x, y);
            if (checkIfWin()) {
                flagAllBombs();
                isGameOver = true;
            }
        } else if (mouseButton == 3) {
            field[x][y].toggleFlag();
        }
        render();
    }

    private boolean checkIfWin() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                Cell cell = field[x][y];
                if (!cell.isOpen() && !cell.isBomb()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void autoOpenCells(int cellX, int cellY) {
        for (int x = cellX - 1; x <= cellX + 1; x++) {
            for (int y = cellY - 1; y <= cellY + 1; y++) {
                if (isLocationValid(x, y)) {
                    open(x, y);
                }
            }
        }
    }

    private void open(int x, int y) {
        Cell cell = field[x][y];
        if (cell.isOpen()) {
            return;
        }
        cell.open();
        if (cell.isBomb()) {
            openAllBombs();
            isGameOver = true;
            return;
        }
        if (cell.isEmpty()) {
            autoOpenCells(x, y);
        }
    }

    private void flagAllBombs() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                Cell cell = field[x][y];
                if (cell.isBomb() && !cell.isFlag()) {
                    cell.toggleFlag();
                }
            }
        }
    }

    private void openAllBombs() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (field[x][y].isBomb()) {
                    field[x][y].open();
                }
            }
        }
    }

    private void fillField() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                field[x][y] = new Cell();
            }
        }
    }

    private void generateBombs(int safeX, int safeY) {
        for (int i = 0; i < BOMB_AMOUNT; i++) {
            generateBomb(safeX, safeY);
        }
    }

    private void generateBomb(int safeX, int safeY) {
        List<Cell> list = new ArrayList<>();
        for (int x = safeX - 1; x <= safeX + 1; x++) {
            for (int y = safeY - 1; y <= safeY + 1; y++) {
                list.add(field[x][y]);
            }
        }

        Cell cell;
        do {
            cell = field[getRandomNum(WIDTH)][getRandomNum(HEIGHT)];
        } while (list.contains(cell));
        cell.makeBomb();
    }


    private void placeCellNumbers() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (!field[x][y].isBomb()) {
                    field[x][y].updateNumber(checkForBombsNear(x, y));
                }
            }
        }
    }

    private int checkForBombsNear(int cellX, int cellY) {
        int bombCount = 0;
        for (int x = cellX - 1; x <= cellX + 1; x++) {
            for (int y = cellY - 1; y <= cellY + 1; y++) {
                if (isLocationValid(x, y) && field[x][y].isBomb()) {
                    bombCount++;
                }
            }
        }
        return bombCount;
    }

    private boolean isLocationValid(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    private int getRandomNum(int max) {
        return (int) (Math.random() * max);
    }

    public void setView(View view) {
        this.view = view;
    }
}
