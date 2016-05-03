
package pohlig.hellman;

public class CRT {

    public int[] computeCRT(int[] final_X) {

        //x=c mod m
        int[] c = new int[(final_X.length) / 2];
        int[] m = new int[(final_X.length) / 2];
        
        int j = 0;
        for (int i = 0; i < final_X.length; i++) {
            c[j] = final_X[i];
            i++;
            m[j] = final_X[i];
            j++;
        }

        int M = 1;          //product of mods
        for (int i = 0; i < m.length; i++) {
            M *= m[i];
        }

        int[] multInv = new int[c.length];

        for (int i = 0; i < multInv.length; i++) {
            multInv[i] = euclidean(M / m[i], m[i])[0];
        }

        int x = 0;
        for (int i = 0; i < m.length; i++) {
            x += (M / m[i]) * c[i] * multInv[i];
        }

        x = leastPosEquiv(x, M);

        int[] result = new int[2];
        result[0] = x;
        result[1] = M;

        return result;

    }

    public static int[] euclidean(int a, int b) {
        if (b > a) {

            int[] coeffs = euclidean(b, a);
            int[] output = {coeffs[1], coeffs[0]};
            return output;
        }

        int q = a / b;

        int r = a - q * b;

        if (r == 0) {
            int[] output = {0, 1};
            return output;
        }

        int[] next = euclidean(b, r);

        int[] output = {next[1], next[0] - q * next[1]};
        return output;
    }

    public static int leastPosEquiv(int a, int m) {

        if (m < 0) {
            return leastPosEquiv(a, -1 * m);
        }

        if (a >= 0 && a < m) {
            return a;
        }

        if (a < 0) {
            return -1 * leastPosEquiv(-1 * a, m) + m;
        }
        int q = a / m;
        return a - q * m;
    }
}
