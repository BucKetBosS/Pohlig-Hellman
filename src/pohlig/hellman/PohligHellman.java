package pohlig.hellman;

import static java.lang.Math.sqrt;
import java.util.Scanner;

public class PohligHellman {

    static int noofPrimes = 0; //no of individual prime numbers in primefactor of p-1

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        
        //input variables
        int p = 0;
        int a = 0, b = 0; //a=alpha; b=beta

        int[] primefactors = new int[p / 2]; //primefactors of p-1 
        
        System.out.println("Enter input in the form\nbeta=alpha^x(mod p)");
        
        System.out.println("Enter value of P: ");
        p = scan.nextInt();
        if (!checkifPrime(p)) { //checking if p is prime
            System.out.println("Number entered is not prime");
            return;
        }
        System.out.println("Enter value of alpha: ");
        a = scan.nextInt();
        System.out.println("Enter value of beta: ");
        b = scan.nextInt();
        
        //getting primefactors of p-1
        primefactors = primeFactors(p-1);

        int[] final_X = calculateX(primefactors, p, a, b); //calculate X mod m values

        //using chinese remainder theorem find X
        CRT crt = new CRT();
        int[] result = crt.computeCRT(final_X);

        //print results
        System.out.println("By CRT: x=" + result[0] + "(mod" + result[1] + ")");
        System.out.println(b + "=" + a + "^" + result[0] + "(mod" + result[1] + ")");

        return;
    }

    static int[] calculateX(int[] primefactors, int p, int alpha, int beta) {

        int i = 1;        //has value of primefactors of p-1 that are currently used
        int power = 0;    //number of times i has occured -1
        int q = 1;        //q=i^power
        int a, b;         //a=(p-1)/1; b=(p-1)/i
        int lhs_beta;     //beta value in lhs as it changes based on power
        int lhs, rhs;     //normal lhs and rhs values lhs=(lhs_beta^a)%p; rhs=(alpha^b)%p

        int[] final_X = new int[noofPrimes * 2];    //final computed X values for each primefactor
        int l = -2;                                 //index for final_X; 1st index has x value, 2nd index has 
                                                    //respective mod value

        int[] X = new int[primefactors.length];     //temp array for holding uncomputed X values

        for (int j = 0; primefactors[j] != 0; j++) {

            if (i != primefactors[j]) {
                l += 2;
                i = primefactors[j];
                power = 0;
            } else 
                power++;
           
            q = q * i;
            if (power == 0) {
                q = i;
                lhs_beta = beta;
            } else 
                lhs_beta = beta * (inverse_mod(alpha, X[j - 1], p)) % p;
            
            a = (p - 1) / q;
            b = (p - 1) / i;

            lhs = exponent_mod(lhs_beta, a, p);
            rhs = exponent_mod(alpha, b, p);

            int k = 0;
            while (lhs != exponent_mod(rhs, k, p)) {
                k++;
            }
            X[j] = k;
            final_X[l] += ((Math.pow(primefactors[j], power)) * k);
            final_X[l + 1] = (int) ((Math.pow(i, power + 1)));
        }
        return final_X;
    }

    static int exponent_mod(int a, int b, int m) {

        //calculate a^b (mod m)
        int a1 = a % m;
        int p = 1;

        for (int i = 1; i <= b; i++) {
            p *= a1;
            p = (p % m);
        }
        return p;
    }

    static int inverse_mod(int alpha, int x, int p) {

        for (int i = 2; i < p; i++) {
            if ((alpha * i) % p == 1) {
                return i;
            }
        }

        return 0;
    }

    static int[] primeFactors(int n) {

        int[] primefactors = new int[n / 2];
        int index = 0;

        if (n % 2 == 0) {
            noofPrimes++;
        }
        while (n % 2 == 0) {
            primefactors[index++] = 2;
            n = n / 2;
        }

        for (int i = 3; i <= sqrt(n); i = i + 2) {
            if (n % i == 0) {
                noofPrimes++;
            }
            while (n % i == 0) {
                primefactors[index++] = i;
                n = n / i;
            }
            
        }

        if (n > 2) {
            primefactors[index++] = n;
            noofPrimes++;
        }

        return primefactors;
    }

    static boolean checkifPrime(int n) {
        int i = 2;
        while (i <= sqrt(n)) {
            if (n % i == 0) { //if there is a factor for p, then it is not a prime
                return false;
            }
            i++;
        }
        return true;
    }
}
