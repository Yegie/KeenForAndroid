//
// Created by Sergey on 8/18/2016.
//

#ifndef KEEN_FOR_ANDROID_KEEN_H
#define KEEN_FOR_ANDROID_KEEN_H

#include "puzzles.h"

struct game_params {
    int w, diff, multiplication_only;
};

char *new_game_desc(const game_params *params, random_state *rs,
                           char **aux, int interactive);

#endif //KEEN_FOR_ANDROID_KEEN_H
