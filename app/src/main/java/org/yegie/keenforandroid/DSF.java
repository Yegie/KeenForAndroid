package org.yegie.keenforandroid;

/**
 * This class is not used in the current version of the program,
 * however if we wanted to get rid of the NDK it would be needed so I left it in.
 *
 * Created by Sergey on 7/30/2016.
 */
public class DSF {

    private int[] parent, rank;

    public DSF()
    {
        //doot doot
    }

    public void init(int size) {
        rank = new int[size];
        parent = new int[size];
        for (int i = 0; i < size; i++)
            parent[i] = i;
    }

    public int find(int i) {
        if (parent[i] != i) {
            parent[i] = find(parent[i]);
        }

        return parent[i];
    }

    public int size(int i)
    {
        int root_of_i = find(i);

        int size=0;

        for(int j = 0; j<parent.length; ++j) {
            if(parent[j]==root_of_i)
                ++size;
        }

        return size;
    }

    public void merge(int x, int y) {
        int index_x_root = find(x);
        int index_y_root = find(y);
        if (index_x_root != index_y_root) {
            if (rank[index_x_root] < rank[index_y_root])
                parent[index_x_root] = index_y_root;
            else if (rank[index_x_root] > rank[index_y_root])
                parent[index_y_root] = index_x_root;
            else {
                parent[index_y_root] = index_x_root;
                rank[index_x_root]++;
            }
        }
    }
}
