package my.virkato.islands;

/***
 * Информация об острове
 */
public class Island {
    private int startX = -1, startY = -1;
    private int left = 1024,
            right = -1,
            up = 1024,
            down = -1;
    private int size = 0;
    private byte[][] m;
    private int width, height;
    private int num;
    private int[][] dir = new int[][]{
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1}, {0, 1},
            {1, -1}, {1, 0}, {1, 1}
    };

    /***
     * Создать информацию о новом острове
     * @param m карта островов
     * @param width ширина карты
     * @param height высота карты
     * @param num порядковый номер
     */
    public Island(byte[][] m, int width, int height, int num) {
        this.m = m;
        this.width = width;
        this.height = height;
        this.num = num;
    }

    /***
     * Прямоугольная область занятая островом
     * @return int[] {xLeft, yUp, xRight, yDown}
     */
    public int[] getRect() {
        return new int[]{left, up, right, down};
    }

    public int getSize() {
        return size;
    }

    /***
     * Добавить все клетки острова начиная с указанной рекурсивно
     * @param y координата клетки
     * @param x координата клетки
     */
    public void addCell(int y, int x) {
        if (m[y][x] == -1) {
            fixCell(y, x);
            for (int[] d : dir) {
                if ((y + d[0] >= 0) && (y + d[0] < height) &&
                        (x + d[1] >= 0) && (x + d[1] < width)) {
                    addCell(y + d[0], x + d[1]);
                }
            }
        }
    }

    /***
     * Зафиксировать клетку как принадлежащую острову
     * @param y координата клетки
     * @param x координата клетки
     */
    private void fixCell(int y, int x) {
        if (startY < 0) startY = y;
        if (startX < 0) startX = x;
        size++;
        m[y][x] = (byte) num;
        left = Math.min(x, left);
        up = Math.min(y, up);
        right = Math.max(x, right);
        down = Math.max(y, down);
    }

    /***
     * Удалить все клетки сотрова начиная со стартовой
     */
    public void remove() {
        removeCell(startY, startX);
    }

    /***
     * Удалить клетки острова рекурсивно
     * @param y координата клетки
     * @param x координата клетки
     */
    private void removeCell(int y, int x) {
        if (m[y][x] == num) {
            m[y][x] = 0;
            for (int[] d : dir) {
                if ((y + d[0] >= 0) && (y + d[0] < height) &&
                        (x + d[1] >= 0) && (x + d[1] < width)) {
                    removeCell(y + d[0], x + d[1]);
                }
            }
        }
    }
}
