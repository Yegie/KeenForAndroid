#include <jni.h>
#include <stdio.h>

#include "keen.h"

JNIEXPORT jstring JNICALL
Java_org_yegie_keenforandroid_KeenModelBuilder_getLevelFromC(JNIEnv *env, jobject instance, jint size, jint diff, jint multOnly, jlong seed){




    struct game_params params;

    params.w = size;
    params.diff = diff;
    params.multiplication_only = multOnly;

    // The seed is used as a set of bytes, so passing the content
    // of the memory occupied by the jlong we have.
    //
    long lseed=seed;
    struct random_state* rs = random_new((char*)&lseed,sizeof(long));

    char* aux;
    int interactive = 0;

    char* level = new_game_desc(&params, rs, &aux, interactive);

    char* combined = snewn((strlen(level)+strlen(aux)+2),char);
    strcpy(combined,level);
    strcat(combined,";");
    strcat(combined,aux);

    jstring retval=(*env)->NewStringUTF(env, combined);

    random_free(rs);
    sfree(level);
    sfree(combined);
    sfree(aux);

    return retval;

}

void fatal(char *fmt, ...)
{
    va_list ap;

    fprintf(stderr, "fatal error: ");

    va_start(ap, fmt);
    vfprintf(stderr, fmt, ap);
    va_end(ap);

    fprintf(stderr, "\n");
    exit(1);
}

static void memswap(void *av, void *bv, int size)
{
    char tmpbuf[512];
    char *a = av, *b = bv;

    while (size > 0) {
        int thislen = min(size, sizeof(tmpbuf));
        memcpy(tmpbuf, a, thislen);
        memcpy(a, b, thislen);
        memcpy(b, tmpbuf, thislen);
        a += thislen;
        b += thislen;
        size -= thislen;
    }
}

void shuffle(void *array, int nelts, int eltsize, random_state *rs)
{
    char *carray = (char *)array;
    int i;

    for (i = nelts; i-- > 1 ;) {
        int j = random_upto(rs, i+1);
        if (j != i)
            memswap(carray + eltsize * i, carray + eltsize * j, eltsize);
    }
}
