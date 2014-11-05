package test;

/**
 * Created by chrno on 11/2/14.
 */
/* From: "A SURVEY OF COMPUTATIONAL PHYSICS"
   by RH Landau, MJ Paez, and CC BORDEIANU
   Copyright Princeton University Press, Princeton, 2008.
   Electronic Materials copyright: R Landau, Oregon State Univ, 2008;
   MJ Paez, Univ Antioquia, 2008; and CC BORDEIANU, Univ Bucharest, 2008.
   Support by National Science Foundation
   */
// CWT.java  Continuous Wavelet Transform.
import java.io.*;

public class CWT  {

    public static void main(String[] argv) throws IOException, FileNotFoundException {
        int i, j, k, o, t, n=120, m=100;
        double c[][]= new double[m][n];  double input[] = new double[1024], omega1 = 1.;
        double tau, dtau, omega, domega, x, WTreal, WTimag, max, omega2= 5.,tau1= -81.92;
        double PsiReal[]= new double[16834], PsiImag[]= new double[16834];

        dtau = 1./m;
        System.out.println("Transform in CWT.dat, signal in CWTin.dat");
        PrintWriter w = new PrintWriter (new FileOutputStream("CWT.dat"), true);
        PrintWriter q = new PrintWriter (new FileOutputStream("CWTin.dat"), true);
        for( t=0; t < 750; t++ ) {
            if( t > 0    && t <= 250 ) input[t] =  5*Math.sin(6.28*t);
            if( t >= 250 && t <= 750 ) input[t] = 10*Math.sin(2.*6.28*t);
            q.println(""+input[t]+"");
        }
        // Psi(t) = Psi((t-tau)/s) = Psi((t-tau)*omega), tau2 = tau1 + n*dtau
        domega = Math.pow(omega2/omega1, 1.0/m);                               // Scaling
        omega = omega1;
        for ( i = 0; i < m; i++ )  {                          // Compute daughter wavelet
            tau = tau1;
            for (o=0; o<16834; o++)  {                // For signals up to 2^13 = 8192 long
                PsiReal[o] = WaveletReal( tau*omega );
                PsiImag[o] = WaveletImag( tau*omega );
                tau = tau + dtau;                                              // Translation
            }
            for ( j=0; j<n; j++ ) {                                          // Compute CWT
                WTreal = 0.;
                WTimag = 0.;
                for (o=0;o<input.length;o++)  {
                    WTreal += input[o]*PsiReal[8192-(j*input.length)/n+o];
                    WTimag += input[o]*PsiImag[8192-(j*input.length)/n+o];
                }
                c[i][j] = Math.sqrt(WTreal*WTreal+WTimag*WTimag);
            }
            omega = omega*domega;                                                // Scaling
        }
        max = 0.0001;
        for (i = 0; i<m; i++) {
            for ( j=0; j<n; j++ ) {                                          // Renormalize
                if ( c[i][j] > max )   max = c[i][j];
                w.println(" "+c[i][j]/max+" ");
            }
            w.println("");
        }
    }

    public static double WaveletReal( double t) {                  // Re Morlet wavelet
        double sigma = 4.0;
        return Math.cos(2.0*Math.PI*t)* Math.exp(-t*t/(2.*sigma*sigma))
                / (sigma*Math.sqrt(2.*Math.PI));
    }

    public static double WaveletImag( double t ) {                 // Im Morlet wavelet
        double sigma = 4.0;
        return Math.sin(2.0*Math.PI*t)* Math.exp(-1.*t*t/(2.*sigma*sigma))
                / (sigma*Math.sqrt(2.0*Math.PI));
    }
}
