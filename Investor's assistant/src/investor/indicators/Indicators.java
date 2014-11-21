/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package investor.indicators;
import investor.data.Index;
/**
 *
 * @author Daniel
 * W przypadku wskaźników wykorzystujących n cen, ostatnie n wyników będzie zawierać błędy, ze względu
 * na brak danych potrzenych do ich wyliczenia
 */
public class Indicators{
    //Metoda do wyliczenia wskaźnika Moving Average, data - dane dla instrumentu, n - liczba cen branych pod uwage
    public static double[] MA(Index[] data,int n){
        double[] out = new double[data.length];
        for(int i=0;i<data.length;i++){
            if(data.length-n<=i){
                int nn=data.length-i;
                double avg_close=0;
                for(int j=i;j<data.length;j++){
                    avg_close+=data[j].getClose_val();
                }
                avg_close/=nn;
                out[i]=avg_close;
            }else{
                double avg_close=0;
                for(int j=i;j<i+n;j++){
                    avg_close+=data[j].getClose_val();
                }
                avg_close=avg_close/n;
                out[i]=avg_close;
            }
        }
        return out;
    }
    //Metoda do wyliczenia odchylenia standardowego (Standard Deviation), do wyswietlenia na osobnym wykresie
    public static double[] SD(Index[] data,int n){
        double[] out = new double[data.length];
        double[] temp = new double[data.length];
        temp=MA(data,n);
        for(int i=0;i<data.length;i++){
            if(temp.length-n<=i){
                double sum=0;
                int nn=data.length-i;
                for(int j=i;j<data.length;j++){
                    sum=sum+Math.pow(data[j].getClose_val()-temp[i],2);
                }
                out[i]=Math.sqrt(sum/(double)nn);
            }else{
                double sum=0;
                for(int j=i;j<i+n;j++){
                    sum=sum+Math.pow(data[j].getClose_val()-temp[i],2);
                }
                out[i]=Math.sqrt(sum/(double)n);
            }
        }
        return out;
    }
    //Wyliczenie wartości wskaźniga Wstęg Bollingera
    //Macierz 3xN, 1 kolumna to SD*K+MA, kolumna 2 to MA, kolumna 3 to MA-SD*K// Przyjmowane K to zwykle ~ 2
    public static double[][] Bollinger(Index[] data,int n,int k){
        double[][] out = new double[3][data.length];
        out[1]=MA(data,n);
        double[] temp = new double[data.length];
        temp=SD(data,n);
        for(int i=0;i<data.length;i++){
            out[0][i]=out[1][i]+(temp[i]*k);
            out[2][i]=out[1][i]-(temp[i]*k);
        }
        return out;
    }
    //Wskaźnik - kopera, podobne do Wstęgi Bollingera, ale tym razem odległośćod od MA jest procentowa, nie
    //K-krotnością odchylenia standardowego. Nie wymaga także wyświetlania śedniej kroczącej, dlatego
    //zwracana macierz jest 2xn,kolumna o indeksie 0 to górna linia, 1 dolna
    //Wartość p zwykle ustalana na poziomie 2,5-10%
    public static double[][] Koperta(Index[] data,int n,int p){
        double[][] out = new double[2][data.length];
        double[] temp = new double[data.length];
        temp=MA(data,n);
         for(int i=0;i<data.length;i++){
            out[0][i]=temp[i]+((double)p/100)*temp[i];
            out[1][i]=temp[i]-((double)p/100)*temp[i];
        }
        out[0]=Reverse(out[0]);
        out[1]=Reverse(out[1]);
        return out;
    }
    //Wykładnicza średnia ruchoma (EMA)
    //Podobne do średniej, jednak każda kolejna wartość brana pod uwagę do śedniej posada wykładniczo mniejszą
    //wagę (współczynnik jest równy (1-alfa)^n gdzie n to kolejna cena brana pod uwage przy wyliczaniu aktualenej ceny
    public static double[] EMA(Index[] data,int n,double alfa){
        double[] out = new double[data.length];
        for(int i=0;i<data.length;i++){
            int expo=0;
            double avg_close=0;
            double div=0;
            if(data.length-n<=i){
                 for(int j=i;j<data.length;j++){
                    avg_close+=data[j].getClose_val()*Math.pow((1-alfa),expo);
                    div+=Math.pow((1-alfa), expo);
                    expo++;
                }
                out[i]=avg_close/div;
                System.out.println();
            }else{
                for(int j=i;j<i+n;j++){
                    avg_close+=data[j].getClose_val()*Math.pow((1-alfa),expo);
                    div+=Math.pow((1-alfa), expo);
                    expo++;
                }
                out[i]=avg_close/div;
            }
        }
        return out;
    }
    
    public static double[] Reverse(double[] Arr){
       double[] temp = new double[Arr.length];
       for(int i=0;i<Arr.length;i++){
           temp[i]=Arr[Arr.length-1-i];
       }
       return temp;
    }
}
