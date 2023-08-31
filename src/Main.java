import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Random rnd = new Random();

        final int WALL = 3;
        final int PLAYER = 2;
        final int BOMB = 9;
        final int ITEM = 4;
        final int POWER = 5;
        int size = 7;

        int[][] map = new int[size][size];
        int[][] maker = new int[size][size];

        int pY = 0;
        int pX = 0;
        int maxBombNum = 1;
        int[][] bombList = null;
        int[] bombCnt = {0};
        int bombPower = 1;
        boolean alive = true;

        while (true) {
            System.out.println("[1]맵제작 [2]플레이 >>> ");
            int sel = scan.nextInt();
            scan.nextLine();

            if(sel==1) {	// 맵제작
                map = new int[size][size];
                maker = new int[size][size];

                pY = 0;
                pX = 0;
                maxBombNum = 1;
                bombList = null;
                bombCnt[0] = 0;
                bombPower = 1;
                alive = true;

                while (true) {
                    for(int i=0;i<size;i++) {
                        for(int j=0;j<size;j++) {
                            String mark = "□ ";
                            if(map[i][j]!=0 && maker[i][j]==PLAYER) {
                                mark = "* ";
                            }else if (maker[i][j]==PLAYER) {
                                mark = "P ";
                            }else if (map[i][j]==WALL) {
                                mark = "■ ";
                            }else if (map[i][j]==BOMB) {
                                mark = "● ";
                            }else if (map[i][j]==ITEM) {
                                mark = "★ ";
                            }
                            System.out.print(mark);
                        }
                        System.out.println();
                    }

                    System.out.print("[w]상 [a]좌 [s]하 [d]우 [i]벽설치 [o]제거 [q]종료 >>> ");
                    String select = scan.nextLine();

                    if (select.equals("i")) {
                        map[pY][pX] = WALL;
                    }else if (select.equals("o")) {
                        map[pY][pX] = 0;
                    }else if (select.equals("q")) {
                        break;
                    }else {
                        int dx = 0;
                        int dy = 0;

                        if(select.equals("w")) {
                            dy = -1;
                        }else if (select.equals("s")) {
                            dy = 1;
                        }else if (select.equals("a")) {
                            dx = -1;
                        }else if (select.equals("d")) {
                            dx = 1;
                        }

                        int newX = pX + dx;
                        int newY = pY + dy;

                        if(newX<0 || newX>=size || newY<0 || newY>=size) {

                        }
                        else {
                            maker[pY][pX] = 0;
                            pY = newY;
                            pX = newX;
                            maker[pY][pX] = PLAYER;
                        }
                    }
                }




            }else if (sel==2) {	// 플레이
                alive = true;
                bombList = new int[maxBombNum][2];
                maker = new int[size][size];
                while (true) {
                    int rX = rnd.nextInt(size);
                    int rY = rnd.nextInt(size);
                    if(map[rY][rX]==0) {
                        map[rY][rX] = PLAYER;
                        pX = rX;
                        pY = rY;
                        break;
                    }
                }

                while (alive) {
                    System.out.println("최대 폭탄 : " + maxBombNum);
                    System.out.println("설치된 폭탄 : " + bombCnt[0]);
                    System.out.println("폭탄 파워 : " + bombPower);
                    for(int i=0;i<bombCnt[0];i++) {
                        System.out.println(Arrays.toString(bombList[i]));
                    }
                    if(bombCnt[0]>0) {
                        for(int i=0;i<bombCnt[0];i++) {
                            map[bombList[i][1]][bombList[i][0]] = BOMB;
                        }
                    }

                    for(int i=0;i<size;i++) {
                        for(int j=0;j<size;j++) {
                            String mark = "□ ";
                            if (map[i][j]==PLAYER) {
                                mark = "P ";
                            }else if (map[i][j]==WALL) {
                                mark = "■ ";
                            }else if (map[i][j]==BOMB) {
                                mark = "● ";
                            }else if (map[i][j]==ITEM) {
                                mark = "★ ";
                            }else if (map[i][j]==POWER) {
                                mark = "♠ ";
                            }

                            System.out.print(mark);
                        }
                        System.out.println();
                    }

                    System.out.print("[w]상 [a]좌 [s]하 [d]우 [i]폭탄설치 [o]폭파 [q]종료 >>> ");
                    String select = scan.nextLine();

                    if (select.equals("q")) {
                        break;
                    }else if (select.equals("i")) {	// 설치
                        if(bombCnt[0]<maxBombNum) {
                            bombList[bombCnt[0]][0] = pX;
                            bombList[bombCnt[0]][1] = pY;
                            bombCnt[0]++;
                        }else {
                            System.out.println("갯수 초과");
                        }
                    }else if (select.equals("o")) {	// 폭파
                        if(bombCnt[0]>0) {
                            exp(map, bombList, bombCnt, bombPower, maxBombNum, rnd, bombList[0][0], bombList[0][1]);
                        }

                        if(map[pY][pX]==0) {
                            alive = false;
                        }
                    }else {
                        int dx = 0;
                        int dy = 0;

                        if(select.equals("w")) {
                            dy = -1;
                        }else if (select.equals("s")) {
                            dy = 1;
                        }else if (select.equals("a")) {
                            dx = -1;
                        }else if (select.equals("d")) {
                            dx = 1;
                        }

                        int newX = pX + dx;
                        int newY = pY + dy;

                        if(newX<0 || newX>=size || newY<0 || newY>=size || map[newY][newX]==WALL) {

                        }else if (map[newY][newX]==BOMB) {
                            int idx = -1;
                            for(int i=0;i<bombList.length;i++) {
                                if(newY==bombList[i][1] && newX==bombList[i][0]) {
                                    idx = i;
                                }
                            }
                            if(idx!=-1) {
                                map[newY][newX] = 0;
                                for(int i=1;i<size;i++) {
                                    int xx = newX+(i*dx);
                                    int yy = newY+(i*dy);
                                    if(xx<0 || xx>=size || yy<0 || yy>=size || map[yy][xx]==WALL || map[yy][xx]==BOMB) {
                                        bombList[idx][0] = xx-dx;
                                        bombList[idx][1] = yy-dy;
                                        break;
                                    }
                                }
                            }
                        }else {
                            map[pY][pX] = 0;
                            if(map[newY][newX]==ITEM) {
                                maxBombNum++;
                                int[][] temp = bombList;
                                bombList = new int[maxBombNum][2];
                                if(temp!=null) {
                                    for(int i=0;i<maxBombNum-1;i++) {
                                        bombList[i] = temp[i];
                                    }
                                }
                            }
                            else if (map[newY][newX]==POWER) {
                                bombPower++;
                            }
                            pY = newY;
                            pX = newX;
                            map[pY][pX] = PLAYER;
                        }
                    }
                }
            }
        }
    }

    public static void exp(int map[][], int[][] bombList, int[] bombCnt, int bombPower, int maxBombNum, Random rnd, int x, int y) {
        final int WALL = 3;
        final int PLAYER = 2;
        final int BOMB = 9;
        final int ITEM = 4;
        final int POWER = 5;
        int size = 7;

        if(bombCnt[0]>0) {
            int bX = x;
            int bY = y;
            map[bY][bX] = 0;
            int idx = -1;
            for(int i=0;i<maxBombNum;i++){
                if(bombList[i][0]==x && bombList[i][1]==y) {
                    idx = i;
                }
            }

            for(int i=idx;i<maxBombNum-1;i++) {
                bombList[i][0] = bombList[i+1][0];
                bombList[i][1] = bombList[i+1][1];
            }
            bombList[maxBombNum-1][0] = 0;
            bombList[maxBombNum-1][1] = 0;
            bombCnt[0]--;

            for(int i=0;i<=bombPower;i++) {
                if(bX+i>=size) {
                    break;
                }else if (map[bY][bX+i]==WALL) {
                    map[bY][bX+i]=0;
                    int r = rnd.nextInt(3);
                    if(r==1) {
                        map[bY][bX+i]=ITEM;
                    }else if (r==2) {
                        map[bY][bX+i]=POWER;
                    }
                    break;
                }else if (map[bY][bX+i]==BOMB) {
                    exp(map, bombList, bombCnt, bombPower, maxBombNum, rnd, bX+i, bY);
                }else {
                    map[bY][bX+i]=0;
                }
            }
            for(int i=1;i<=bombPower;i++) {
                if(bX-i<0) {
                    break;
                }else if (map[bY][bX-i]==WALL) {
                    map[bY][bX-i]=0;
                    int r = rnd.nextInt(3);
                    if(r==1) {
                        map[bY][bX-i]=ITEM;
                    }else if (r==2) {
                        map[bY][bX-i]=POWER;
                    }
                    break;
                }else if (map[bY][bX-i]==BOMB) {
                    exp(map, bombList, bombCnt, bombPower, maxBombNum, rnd, bX-i, bY);
                }else {
                    map[bY][bX-i]=0;
                }
            }for(int i=1;i<=bombPower;i++) {
                if(bY-i<0) {
                    break;
                }else if (map[bY-i][bX]==WALL) {
                    map[bY-i][bX]=0;
                    int r = rnd.nextInt(3);
                    if(r==1) {
                        map[bY-i][bX]=ITEM;
                    }else if (r==2) {
                        map[bY-i][bX]=POWER;
                    }
                    break;
                }else if (map[bY-i][bX]==BOMB) {
                    exp(map, bombList, bombCnt, bombPower, maxBombNum, rnd, bX, bY-i);
                }else {
                    map[bY-i][bX]=0;
                }
            }for(int i=1;i<=bombPower;i++) {
                if(bY+i>=size) {
                    break;
                }else if (map[bY+i][bX]==WALL) {
                    map[bY+i][bX]=0;
                    int r = rnd.nextInt(3);
                    if(r==1) {
                        map[bY+i][bX]=ITEM;
                    }else if (r==2) {
                        map[bY+i][bX]=POWER;
                    }
                    break;
                }else if (map[bY+i][bX]==BOMB) {
                    exp(map, bombList, bombCnt, bombPower, maxBombNum, rnd, bX, bY+i);
                }else {
                    map[bY+i][bX]=0;
                }
            }
        }
    }
}