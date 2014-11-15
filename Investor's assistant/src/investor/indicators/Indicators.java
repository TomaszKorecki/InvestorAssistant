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
    public double[] MA(Index[] data,int n){
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
                avg_close/=n;
                out[i]=avg_close;
            }
        }
        return out;
    }
    //Metoda do wyliczenia odchylenia standardowego (Standard Deviation), do wyswietlenia na osobnym wykresie
    public double[] SD(Index[] data,int n){
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
                out[i]=Math.sqrt(sum/nn);
            }else{
                double sum=0;
                for(int j=i;j<i+n;j++){
                    sum=sum+Math.pow(data[j].getClose_val()-temp[i],2);
                }
                out[i]=Math.sqrt(sum/n);
            }
        }
        return out;
    }
    //Wyliczenie wartości wskaźniga Wstęg Bollingera
    //Macierz 3xN, 1 kolumna to SD*K+MA, kolumna 2 to MA, kolumna 3 to MA-SD*K// Przyjmowane K to zwykle ~ 2
    public double[][] Bollinger(Index[] data,int n,int k){
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
}
