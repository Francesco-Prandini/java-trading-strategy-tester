

package tesi.tester;

import tesi.tester.*;

import javax.swing.JFrame;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import java.awt.event.*;


import java.io.File;

import java.math.BigDecimal;


/**
 *
 * @author Francesco
 *
 * La classe MainJFrame rappresenta il frame principale dell'applicazione, dal quale si può
 * configurare e iniziare la simulazione.
 */
public class MainJFrame extends JFrame {
    private static String classesFolder="lib/tradingstrategies";
    private static final String DATABASE_NAME="test.db";
    private static final String TABLE_NAME="historicalQuotes";

    private JPanel mainPanel;

    private JCheckBox[] classesCheckBox;

    private JTextField databaseNameField=new JTextField("test.db");
    private JTextField tableNameField=new JTextField("historicalQuotes");
    private JTextField initialCashField=new JTextField("1000");
    private JTextField daysToSkipField=new JTextField("1");
    private JTextField configFileField=new JTextField("pStrategy.config");

    private JLabel databaseNameLabel=new JLabel("Nome Database:");
    private JLabel tableNameLabel=new JLabel("Nome Tabella");
    private JLabel initialCashLabel=new JLabel("Budget Iniziale:");
    private JLabel daysToSkipLabel=new JLabel("Giorni da saltare:");
    private JLabel configFileLabel=new JLabel("File di configurazione:");

    public MainJFrame(){
        super();
        mainPanel=createJPanel();
        add(mainPanel);
        pack();
        setVisible(true);
    }
    public String[] refreshClasses(){
        int i,j=0,count=0;
        String[] s1=new File(classesFolder).list();
        String[] s2;
        for(i=0;i<s1.length;i++){
            if(!s1[i].startsWith(".")) count++;
        }
        s2=new String[count];
        
        for(i=0;i<s1.length;i++){
            if(!s1[i].startsWith(".")){
                s2[j]=s1[i];
                j++;
            }
        }
        return s2;
    }

    public JPanel createJPanel(){
        int i,j;
        String[] classesFound=refreshClasses();
        JPanel main =new JPanel();
        main.setLayout(new FlowLayout());
        JPanel checkBoxPanel=new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel,BoxLayout.Y_AXIS));
        JPanel settingsPanel=new JPanel();
        classesCheckBox=new JCheckBox[classesFound.length];


        settingsPanel.setLayout(new BoxLayout(settingsPanel,BoxLayout.Y_AXIS));
        JPanel subPanel1=new JPanel();
        subPanel1.setLayout(new FlowLayout());
        JPanel subPanel2=new JPanel();
        subPanel2.setLayout(new FlowLayout());
        JPanel subPanel3=new JPanel();
        subPanel3.setLayout(new FlowLayout());
        JPanel subPanel4=new JPanel();
        subPanel4.setLayout(new FlowLayout());
        JPanel subPanel5=new JPanel();
        subPanel5.setLayout(new FlowLayout());

        subPanel1.add(initialCashLabel);
        subPanel1.add(initialCashField);
        subPanel2.add(daysToSkipLabel);
        subPanel2.add(daysToSkipField);
        subPanel3.add(configFileLabel);
        subPanel3.add(configFileField);
        subPanel4.add(databaseNameLabel);
        subPanel4.add(databaseNameField);
        subPanel5.add(tableNameLabel);
        subPanel5.add(tableNameField);

        initialCashField.setColumns(5);
        daysToSkipField.setColumns(5);
        configFileField.setColumns(5);

        for(i=0;i<classesFound.length;i++){
            if(!classesFound[i].startsWith(".")){
                classesCheckBox[i]=new JCheckBox(classesFound[i]);
                checkBoxPanel.add(classesCheckBox[i]);
            }
        }
        checkBoxPanel.add(new JSeparator());

        JButton refresh=new JButton("Refresh");
        refresh.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JPanel m=createJPanel();
                remove(mainPanel);
                add(m);
                mainPanel=m;

                //m.paint(getGraphics());
                m.setVisible(false);
                m.setVisible(true);
            }
        });
        checkBoxPanel.add(refresh);


        settingsPanel.add(subPanel4);
        settingsPanel.add(subPanel5);
        settingsPanel.add(subPanel1);
        settingsPanel.add(subPanel2);
        settingsPanel.add(subPanel3);

        settingsPanel.add(new JSeparator());

        JButton startSimulation=new JButton("Inizia simulazione");
        startSimulation.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int i,j;

                for(i=0;i<classesCheckBox.length;i++){
                    if(classesCheckBox[i].isSelected()){
                         j=classesCheckBox[i].getText().indexOf(".class");
                        String className=classesCheckBox[i].getText().substring(0,j); //elimino l'estensione .class dal nome della classe.
                        StrategyTester t=new StrategyTester(databaseNameField.getText(),tableNameField.getText(),className,
                                new BigDecimal(Double.parseDouble(initialCashField.getText())),
                                Integer.parseInt(daysToSkipField.getText()),configFileField.getText());
                        t.start();
                    }
                }
            }
        });
        settingsPanel.add(startSimulation);




        main.add(checkBoxPanel);
        main.add(settingsPanel);

        return main;
    }

    public static void main(String[] args){
        MainJFrame mf=new MainJFrame();
    }

}
