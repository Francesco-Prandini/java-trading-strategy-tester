
package tesi;

/**
 *
 * @author Francesco
 */
public class TesiMain {
    public static void main(String[] args) {
        if((args[0].compareTo("-tester"))==0){
            tesi.tester.MainJFrame.main(args);
        }else{
            tesi.datalogger.Datalogger.main(args);
        }
    }

}
