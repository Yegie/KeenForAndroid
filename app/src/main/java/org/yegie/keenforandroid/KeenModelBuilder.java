package org.yegie.keenforandroid;

import android.nfc.Tag;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;



/**
 * Created by Sergey on 5/25/2016.
 */
public class KeenModelBuilder {

    /*
 * Maximum size of any clue block. Very large ones are annoying in UI
 * terms (if they're multiplicative you end up with too many digits to
 * fit in the square) and also in solver terms (too many possibilities
 * to iterate over).
 */
//    private static final int MAXBLK = 5;
//
//    private static final int EASY = 1;
//    private static final int NORMAL = 2;
//    private static final int HARD = 3;
//    private static final int EXTREME = 4;
//    private static final int UNREASONABLE = 5;



//    enum Hints {F_ADD,F_SUB,F_MUL,F_DIV,B_ADD,B_SUB,B_MUL,B_DIV}
//    public class HintSet extends HashSet<Hints> {}
//
//    public void genGame(int width, int difficulty, boolean multOnly, Random rs) {
//
//        int w = width, a = w * w;
//        int[] grid, soln;
//        int[] order, revorder;
//        DSF dsf = new DSF();
//        HintSet[] hintType;
//        boolean[] singletons;
//        long[] cluevals;
//        Hints[] clues;
//        int i,j,k,n,x,y,ret;
//        int diff = difficulty;
//        String desc;
//
//        //remove size 3x3 with diff higher than normal
//        if (w == 3 && diff > NORMAL)
//            diff = 2;
//
//        order = new int[a];
//        revorder = new int[a];
//        singletons = new boolean[a];
//
//        hintType = new HintSet[a];
//        for(i=0; i<hintType.length; ++i)
//            hintType[i]=new HintSet();
//
//        //create a blank disjoint set of size a
//        dsf.init(a);
//        clues = new Hints[a];
//        cluevals = new long[a];
//        soln = new int[a];
//
//        //rely on a break statement to quit this :/
//        while(true){
//
//
//            grid = genLatinSquare(w, rs);
//
//            for (i = 0; i < a; i++)
//                order[i] = i;
//
//            shuffle(order, rs);
//
//            System.out.print(order);
//
//            for (i = 0; i < a; i++)
//                revorder[order[i]] = i;
//
//            for (i = 0; i < a; i++)
//                singletons[i] = true;
//
//            for (i = 0; i < a; i++) {
//                if (singletons[i]) {
//                    int best = -1;
//
//                    x = i % w;
//                    y = i / w;
//
//                    if (x > 0 && singletons[i-1] &&
//                            (best == -1 || revorder[i-1] < revorder[best]))
//                        best = i-1;
//
//                    if (x+1 < w && singletons[i+1] &&
//                            (best == -1 || revorder[i+1] < revorder[best]))
//                        best = i+1;
//
//                    if (y > 0 && singletons[i-w] &&
//                            (best == -1 || revorder[i-w] < revorder[best]))
//                        best = i-w;
//
//                    if (y+1 < w && singletons[i+w] &&
//                            (best == -1 || revorder[i+w] < revorder[best]))
//                        best = i+w;
//
//		/*
//		 * When we find a potential domino, we place it with
//		 * probability 3/4, which seems to strike a decent
//		 * balance between plenty of dominoes and leaving
//		 * enough singletons to make interesting larger
//		 * shapes.
//		 */
//                    if (best >= 0 && rs.nextDouble()<0.75) {
//                        singletons[i] = singletons[best] = false;
//                        dsf.merge(i, best);
//                    }
//                }
//            }
//
//            /* Fold in singletons. */
//            for (i = 0; i < a; i++) {
//                if (singletons[i]) {
//                    int best = -1;
//
//                    x = i % w;
//                    y = i / w;
//
//                    if (x > 0 && dsf.size(i-1) < MAXBLK &&
//                            (best == -1 || revorder[i-1] < revorder[best]))
//                        best = i-1;
//
//                    if (x+1 < w && dsf.size(i+1) < MAXBLK &&
//                            (best == -1 || revorder[i+1] < revorder[best]))
//                        best = i+1;
//
//                    if (y > 0 && dsf.size(i-w) < MAXBLK &&
//                            (best == -1 || revorder[i-w] < revorder[best]))
//                        best = i-w;
//
//                    if (y+1 < w && dsf.size(i+w) < MAXBLK &&
//                            (best == -1 || revorder[i+w] < revorder[best]))
//                        best = i+w;
//
//                    if (best >= 0) {
//                        singletons[i] = singletons[best] = false;
//                        dsf.merge(i, best);
//                    }
//                }
//            }
//
//            for (i = 0; i < a; i++) {
//                if (singletons[i]) {
//                    break;
//                }
//            }
//
//            if (i < a)
//                continue;
//
//            //start to put clues into blocks
//
//            for(i = 0; i<a; ++i)
//            {
//                hintType[i].clear();
//
//                j = dsf.find(i);
//                k = dsf.size(j);
//
//                if(multOnly)//if mult only option was selected
//                {
//                    hintType[j].add(Hints.F_MUL);
//                }
//                else if(j == i && k>2)//if size of current block is >2
//                {
//                    hintType[j].add(Hints.F_MUL);
//                    hintType[j].add(Hints.F_ADD);
//                }
//                else if(j != i && k == 2)//if size of current block is exactly 2
//                {
//                    /* Fetch the two numbers and sort them into order. */
//                    int p = grid[j];
//                    int q = grid[i], v;
//                    if (p < q) {
//                        int t = p; p = q; q = t;
//                    }
//
//
//                    //trying to avoid too easy of an addition clue
//                    v = p+q;
//                    if(v > 4 && v < 2*w-2)
//                        hintType[j].add(Hints.F_ADD);
//                    else
//                        hintType[j].add(Hints.B_ADD);
//
//                    //above normal diff we prefer multi as long as there is more
//                    //than one possible set of multiples
//                    v = p*q;
//                    n = 0;
//                    for(int z = 1; z <= w; ++z)
//                        if(v % z == 0 && v/z <= w && v/z != z)
//                            n++;
//                    if (n <= 2 && diff > NORMAL)
//                        hintType[j].add(Hints.B_MUL);
//                    else
//                        hintType[j].add(Hints.F_MUL);
//
//                    //ignore subtraction clues where the diff is w-1
//                    v = p - q;
//                    if (v < w-1)
//                        hintType[j].add(Hints.F_SUB);
//
//                    //division:
//                    //1: quotient must me an integer
//                    //2: quotient must be less than w/2 (or its too easy)
//                    if (p % q == 0 && 2 * (p / q) <= w)
//                        hintType[j].add(Hints.F_DIV);
//                }
//
//                //now we choose a clue for each block based on the allowed clues
//                shuffle(order, rs);
//                for (i = 0; i < a; ++i)
//                    clues[i] = null;
//                while(true)
//                {
//                    boolean done_something = false;
//
//                    for(k = 0; k < 4; ++k)
//                    {
//                        Hints clue, good, bad;
//                        switch (k) {
//                            case 0:
//                                clue = Hints.F_DIV;
//                                good = Hints.F_DIV;
//                                bad  = Hints.B_DIV;
//                                break;
//                            case 1:
//                                clue = Hints.F_SUB;
//                                good = Hints.F_SUB;
//                                bad  = Hints.B_SUB;
//                                break;
//                            case 2:
//                                clue = Hints.F_MUL;
//                                good = Hints.F_MUL;
//                                bad  = Hints.B_MUL;
//                                break;
//                            default:
//                                clue = Hints.F_ADD;
//                                good = Hints.F_ADD;
//                                bad  = Hints.B_ADD;
//                                break;
//                        }
//
//                        for (i = 0; i < a; i++) {
//                            j = order[i];
//                            if (hintType[j].contains(good)) {
//                                clues[j] = clue;
//                                hintType[j].clear();
//                                break;
//                            }
//                        }
//                        if (i == a) {
//		    /* didn't find a nice one, use a nasty one */
//                            for (i = 0; i < a; i++) {
//                                j = order[i];
//                                if (hintType[j].contains(bad)) {
//                                    clues[j] = clue;
//                                    hintType[j].clear();
//                                    break;
//                                }
//                            }
//                        }
//                        if (i < a)
//                            done_something = true;
//                    }
//
//                    if (!done_something)
//                        break;
//                }
//
//
//                //calculate clue values for a given type
//                for(i = 0; i<a; ++i)
//                {
//                    j = dsf.find(i);
//                    if(j == i)
//                    {
//                        cluevals[j] = grid[i];
//                    } else
//                    {
//                        switch (clues[j]) {
//                            case F_ADD:
//                                cluevals[j] += grid[i];
//                                break;
//                            case F_MUL:
//                                cluevals[j] *= grid[i];
//                                break;
//                            case F_SUB:
//                                cluevals[j] = Math.abs(cluevals[j] - grid[i]);
//                                break;
//                            case F_DIV:
//                            {
//                                long d1 = cluevals[j];
//                                int d2 = grid[i];
//                                if (d1 == 0 || d2 == 0)
//                                    cluevals[j] = 0;
//                                else
//                                    cluevals[j] = d2/d1 + d1/d2;/* one is 0 :-) */
//                            }
//                            break;
//                        }
//                    }
//                }
//
//            }
//
//            //CONTINUE FROM HERE
//            // .
//            // .
//            // .
//            // .
//            // .
//            // .
//            // .
//            // .
//            // .
//
//        }
//
//    }
//
//    private void shuffle(int[] order, Random rs) {
//
//        int a = order.length;
//
//        for(int i = 0; i < a; ++i)
//        {
//            int tmp = order[i];
//            int index = (int) rs.nextDouble()*a;
//            order[i] = order[index];
//            order[index] = tmp;
//        }
//    }
//
//    private int[] genLatinSquare(int w, Random rs) {
//
//        int a = w*w,x,y;
//
//        int[] out = new int[a];
//        int[] start = new int[w];
//
//        boolean[][] cols,rows;
//
//        cols = new boolean[w][w];
//        rows = new boolean[w][w];
//
//        long iteration=0;
//
//        ILOOP:
//        for(int i = 0; i<a; ++i)
//        {
//
//            x = i % w;
//            y = i / w;
//
//            int loop=0;
//
//            while(true) {
//
//                int random = rs.nextInt(w);
//
//                if (!cols[x][random] && !rows[y][random])
//                {
//                    cols[x][random] = true;
//                    rows[y][random] = true;
//
//                    out[i] = random+1;
//                    break;
//                }
//
//                //makeshift solution but it does work, maybe fix at some point
//                if(++loop > w*20) {
//                    Log.d("LATIN","Painted ourselves into the corner, iteration="+iteration);
//                    for(int j = 0; j<w; ++j){
//                        rows[y][j] = false;
//                        int r=out[j+y*w];
//                        if(r>0) {
//                            cols[j][r - 1] = false;
//                        }
//                    }
//                    i=y*w - 1;
//                    continue ILOOP;
//                }
//
//                if(++iteration % 100000 == 0) {
//                    Log.d("LATIN", "...iteration=" + iteration + " x=" + x + " y=" + y);
//                    for(int yy=0; yy<w; ++yy) {
//                        String line="";
//                        for(int xx=0; xx<w; ++xx) {
//                            line+=String.format(Locale.US,"%2d ",out[xx+yy*w]);
//                        }
//                        Log.d("LATIN",line);
//                    }
//                }
//            }
//        }
//
//
//        return out;
//
//    }

    public KeenModel build(int size, int diff, int multOnlt, long seed)
    {

        String levelAsString = getLevelFromC(size,diff,multOnlt,seed);
        String ZoneData = "";

        Log.d("GEN",levelAsString);

        KeenModel.GridCell[][] cells = new KeenModel.GridCell[size][size];
        HashSet<Integer> diffZones = new HashSet<>();

        for(int i = 0; i < levelAsString.length(); i+=3)
        {
            int dsfCount = Integer.parseInt(levelAsString.substring(i,i+2));
            diffZones.add(dsfCount);

            if(levelAsString.charAt(i+2)==';')
            {

                ZoneData = levelAsString.substring(0,i+3);
                levelAsString = levelAsString.substring(i+3);
                break;
            }
        }

        int zoneCount = diffZones.size();
        diffZones.clear();

        KeenModel.Zone[] zones = new KeenModel.Zone[zoneCount];

        for(int i = 0; i<zoneCount; i++)
        {
            char sym = levelAsString.charAt(i*6);
            int val = Integer.parseInt(levelAsString.substring(i*6+1,i*6+5));
            switch(sym)
            {
                case 'a':
                    zones[i] = new KeenModel.Zone(KeenModel.Zone.Type.ADD,val,i);
                    break;
                case 'm':
                    zones[i] = new KeenModel.Zone(KeenModel.Zone.Type.TIMES,val,i);
                    break;
                case 's':
                    zones[i] = new KeenModel.Zone(KeenModel.Zone.Type.MINUS,val,i);
                    break;
                default://divide
                    zones[i] = new KeenModel.Zone(KeenModel.Zone.Type.DIVIDE,val,i);
                    break;
            }
        }

        levelAsString = levelAsString.substring(zoneCount*6);

        class zonePairing
        {
            int raw;
            int real;

            public zonePairing(int a, int b)
            {
                raw = a; real = b;
            }
        }

        ArrayList<zonePairing> zonePairingList = new ArrayList<>();
        zonePairingList.add(new zonePairing(0,0));

        int nextZoneIndex = 1;


        for(int i = 0; i<size*size; ++i)
        {

            int val = Integer.parseInt(levelAsString.substring(i,i+1));
            int zone = Integer.parseInt(ZoneData.substring(0,2));
            ZoneData = ZoneData.substring(3);

            boolean exists = false;
            int zoneIndex = 0;

            for(int j = 0; j<zonePairingList.size(); ++j)
            {

                if(zone == zonePairingList.get(j).raw)
                {

                    exists = true;
                    zoneIndex = zonePairingList.get(j).real;
                    break;
                }

            }

            if(!exists)
            {
                zonePairingList.add(new zonePairing(zone,nextZoneIndex));
                zoneIndex = nextZoneIndex;
                nextZoneIndex++;
            }

            int x = i/size;
            int y = i%size;

            cells[x][y] = new KeenModel.GridCell(size,val,zones[zoneIndex]);

        }

        return new KeenModel(size,zones,cells);

    }

    static {
        System.loadLibrary("keen-android-jni");
    }

    @SuppressWarnings("JniMissingFunction") //it exists, studio just does not recognize it...
    public native String getLevelFromC(int i, int size, int diff, long seed);


    // This will create a test map of 4x4 with a preset solvable layout.
//    public KeenModel build(int size) {
//
//
//        if(size != 4) {
//            Log.e("KeenModelBuilder", "Size must be 4 for test cases");
//            size = 4;
//        }
//
//        KeenModel.Zone[] zones={
//            new KeenModel.Zone(KeenModel.Zone.Type.DIVIDE,  2),
//            new KeenModel.Zone(KeenModel.Zone.Type.TIMES,   48),
//            new KeenModel.Zone(KeenModel.Zone.Type.MINUS,   1),
//            new KeenModel.Zone(KeenModel.Zone.Type.DIVIDE,  2),
//            new KeenModel.Zone(KeenModel.Zone.Type.MINUS,   2),
//            new KeenModel.Zone(KeenModel.Zone.Type.TIMES,   6),
//            new KeenModel.Zone(KeenModel.Zone.Type.ADD,     5),
//        };
//
//        KeenModel.GridCell[][] cells={
//                {
//                        new KeenModel.GridCell(size,1,zones[0]),
//                        new KeenModel.GridCell(size,2,zones[0]),
//                        new KeenModel.GridCell(size,4,zones[3]),
//                        new KeenModel.GridCell(size,3,zones[5])
//                },
//                {
//                        new KeenModel.GridCell(size,3,zones[1]),
//                        new KeenModel.GridCell(size,4,zones[1]),
//                        new KeenModel.GridCell(size,2,zones[3]),
//                        new KeenModel.GridCell(size,1,zones[5])
//                },
//                {
//                        new KeenModel.GridCell(size,4,zones[1]),
//                        new KeenModel.GridCell(size,1,zones[4]),
//                        new KeenModel.GridCell(size,3,zones[4]),
//                        new KeenModel.GridCell(size,2,zones[5])
//                },
//                {
//                        new KeenModel.GridCell(size,2,zones[2]),
//                        new KeenModel.GridCell(size,3,zones[2]),
//                        new KeenModel.GridCell(size,1,zones[6]),
//                        new KeenModel.GridCell(size,4,zones[6])
//                }
//        };
//
//        return new KeenModel(size,zones,cells);
//
//    }

}
