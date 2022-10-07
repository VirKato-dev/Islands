package my.virkato.islands;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private Button b_new, b_clean;
    private TableLayout table;
    private int containerSize;

    private byte[][] matrix;
    private int matrixWidth, matrixHeight;

    private ArrayList<Island> islands = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        table = findViewById(R.id.table);

        b_new = findViewById(R.id.b_new);
        b_new.setOnClickListener(v -> {
            init();
        });

        b_clean = findViewById(R.id.b_clean);
        b_clean.setOnClickListener(v -> {
            clean(10);
        });

        init();
    }


    private void init() {
        // размер таблички
        containerSize = getResources().getDisplayMetrics().widthPixels;
        table.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, containerSize));

        createMatrix(30, 30);
        showMatrix(table);

        searchIslands(islands);
        showMatrix(table);
    }

    /***
     * Удалить устрова с размером меньше указанного
     * @param minSize минимальный размер острова
     */
    private void clean(int minSize) {
        Iterator<Island> it = islands.iterator();
        while (it.hasNext()) {
            Island island;
            if ((island = it.next()).getSize() < minSize) {
                island.remove();
                it.remove();
            }
        }
        showMatrix(table);
    }

    /***
     * Создать карту островов
     * @param width ширина карты
     * @param height высота карты
     */
    private void createMatrix(int width, int height) {
        int x, y;
        matrixWidth = width;
        matrixHeight = height;
        matrix = new byte[height][width];
        Random rand = new Random();

        for (int i = 0; i < (height * width / 2); i++) {
            int dy = 0, dx = 0;
            y = rand.nextInt(height);
            x = rand.nextInt(width);
            if (x > 1 && y > 1 && x < width - 2 && y < height - 2) {
                if (matrix[y - 2][x] == -1) dy = -1;
                else if (matrix[y + 2][x] == -1) dy = 1;
                else if (matrix[y][x - 2] == -1) dx = -1;
                else if (matrix[y][x + 2] == -1) dx = 1;
            }
            matrix[y + dy][x + dx] = -1;
        }
    }

    /***
     * Добавить ячейки в таблицу
     * @param container таблица
     */
    private void showMatrix(TableLayout container) {
        container.removeAllViews();
        int cellSize = containerSize / matrixWidth;
        for (int h = 0; h < matrixHeight; h++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            container.addView(row);
            for (int w = 0; w < matrixWidth; w++) {
                LinearLayout cell = (LinearLayout) getLayoutInflater().inflate(R.layout.cell, null);
                TextView text = cell.findViewById(R.id.text);
                text.setLayoutParams(new LinearLayout.LayoutParams(cellSize, cellSize));
                String label = "";
                if (matrix[h][w] > 0) {
                    label = String.valueOf(matrix[h][w]);
                }
                text.setText(label);
                if (matrix[h][w] != 0) {
                    cell.setBackgroundColor(Color.parseColor("#44ff44"));
                }
                row.addView(cell);
            }
        }
    }

    /***
     * Сформировать список островов
     * @param list список островов
     */
    private void searchIslands(List<Island> list) {
        list.clear();
        for (int y = 0; y < matrixHeight; y++) {
            for (int x = 0; x < matrixWidth; x++) {
                Island island = new Island(matrix, matrixWidth, matrixHeight, list.size() + 1);
                island.addCell(y, x);
                if (island.getSize() > 0) {
                    list.add(island);
                }
            }
        }
    }
}