package org.yegie.keenforandroid;

import android.util.Log;

/**
 * Created by Sergey on 5/25/2016.
 */
public class KeenModelBuilder {

    public KeenModel build(int size) {


        if(size != 4) {
            Log.e("KeenModelBuilder", "Size must be 4 for test cases");
            size = 4;
        }

        KeenModel.Zone[] zones={
            new KeenModel.Zone(KeenModel.Zone.Type.DIVIDE,  2),
            new KeenModel.Zone(KeenModel.Zone.Type.TIMES,   48),
            new KeenModel.Zone(KeenModel.Zone.Type.MINUS,   1),
            new KeenModel.Zone(KeenModel.Zone.Type.DIVIDE,  2),
            new KeenModel.Zone(KeenModel.Zone.Type.MINUS,   2),
            new KeenModel.Zone(KeenModel.Zone.Type.TIMES,   6),
            new KeenModel.Zone(KeenModel.Zone.Type.ADD,     5),
        };

        KeenModel.GridCell[][] cells={
                {
                        new KeenModel.GridCell(size,1,zones[0]),
                        new KeenModel.GridCell(size,2,zones[0]),
                        new KeenModel.GridCell(size,4,zones[3]),
                        new KeenModel.GridCell(size,3,zones[5])
                },
                {
                        new KeenModel.GridCell(size,3,zones[1]),
                        new KeenModel.GridCell(size,4,zones[1]),
                        new KeenModel.GridCell(size,2,zones[3]),
                        new KeenModel.GridCell(size,3,zones[5])
                },
                {
                        new KeenModel.GridCell(size,4,zones[1]),
                        new KeenModel.GridCell(size,1,zones[4]),
                        new KeenModel.GridCell(size,3,zones[4]),
                        new KeenModel.GridCell(size,2,zones[5])
                },
                {
                        new KeenModel.GridCell(size,2,zones[2]),
                        new KeenModel.GridCell(size,3,zones[2]),
                        new KeenModel.GridCell(size,1,zones[6]),
                        new KeenModel.GridCell(size,4,zones[6])
                }
        };

        return new KeenModel(size,zones,cells);

    }
}
