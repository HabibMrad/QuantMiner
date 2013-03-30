/*                                             
 *Copyright 2007, 2011 CCLS Columbia University (USA), LIFO University of Orl��ans (France), BRGM (France)
 *
 *Authors: Cyril Nortet, Xiangrong Kong, Ansaf Salleb-Aouissi, Christel Vrain, Daniel Cassard
 *
 *This file is part of QuantMiner.
 *
 *QuantMiner is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *QuantMiner is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License along with QuantMiner.  If not, see <http://www.gnu.org/licenses/>.
 */
	package src.graphicalInterface;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

import src.database.*;
import src.graphicalInterface.TableEvolvedCells.*;
import src.solver.*;
import src.tools.*;

import java.awt.*;

public class PanelPreLoadDB extends DatabasePanelAssistant {//step 1 
    
    DatabaseAdmin m_gestionnaireBD = null;
    DefaultListModel m_modeleColonnesInitiales = null;
    
    
    public PanelPreLoadDB(ResolutionContext contexteResolution) {
        super(contexteResolution);
        
        String sInfosBase = null;
        // A TableColumn represents all the attributes of a column in a JTable, 
        // such as width, resizibility, minimum and maximum width
        TableColumn colonneTableau = null;  
        String [] sComboBoxOptions = new String [] { "Categorical", "Numerical" };  //ComboBox Options
        
        m_gestionnaireBD = m_contexteResolution.m_gestionnaireBD;
        
        if (m_gestionnaireBD == null)
            return;
        
        m_gestionnaireBD.LibererDonneesEnMemoire();  //release data column
        
        initComponents();
        
        //Returns the TableColumnModel that contains all column information of this table. 
        //Returns the TableColumn object for the column at columnIndex.         
        colonneTableau = jTableAttributs.getColumnModel().getColumn(1); 
        colonneTableau.setCellEditor( new CelluleComboBoxEditor( sComboBoxOptions ) );
        colonneTableau.setCellRenderer( new CelluleComboBoxRenderer( sComboBoxOptions ) );
        
        colonneTableau = jTableAttributs.getColumnModel().getColumn(2);
        colonneTableau.setMaxWidth(140);
        colonneTableau.setMinWidth(140);
        colonneTableau.setPreferredWidth(140);
        
        sInfosBase = "Database " + m_gestionnaireBD.ObtenirNomBaseDeDonnees();
        sInfosBase += ",   " + String.valueOf( m_gestionnaireBD.ObtenirNombreLignes() ) + " records.";
        jLabelInfosBase.setText(sInfosBase);
        
        InitialiserContenuPanneau(); //initialize the content of the panel of first step
        
        super.DefinirEtape(1, "Attributes selection from the database", ENV.REPERTOIRE_AIDE+"pre_loading.htm"); //define step
        super.DefinirPanneauPrecedent(MainWindow.PANNEAU_AUCUN);  //since this is the first step's panel, no previous panel exist
        super.DefinirPanneauSuivant(MainWindow.PANNEAU_PRE_EXTRACION); //next panel is the second step panel
        super.initBaseComponents();
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jLabelInfosBase = new javax.swing.JLabel(); //JLabel: A display area for a short text string or an image, or both
        //A JScrollPane manages a viewport, optional vertical and horizontal scroll bars, and optional row and column heading viewports.
        jScrollPaneAttributs = new javax.swing.JScrollPane();
        jTableAttributs = new javax.swing.JTable(); //JTable display and edit regular two-dimensional tables of cells

        setLayout(null);

        jLabelInfosBase.setFont(new java.awt.Font("Dialog", 1, 14)); //Where is the declaration of jLabelInfosBase. UserCTR
        jLabelInfosBase.setForeground(new java.awt.Color(0, 0, 153)); //what is jjLabelInfosBase for
        jLabelInfosBase.setText("infos base");
        add(jLabelInfosBase);                                        //where is add()??
        jLabelInfosBase.setBounds(70, 60, 450, 19);                  //x,y,width,height

        /*
         * DefaultTableModel is a model implementation that uses a Vector of Vectors of Objects to store the cell values. 
         * As well as copying the data from an application into the DefaultTableModel, it is also possible to wrap the 
         * data in the methods of the TableModel interface so that the data can be passed to the JTable directly
         */
        jTableAttributs.setModel(new javax.swing.table.DefaultTableModel( //This is amazing!!! java inner class 
            new Object [][] {

            },
            new String [] {
                "Attributes", "Type", "Consider"  //title of each column
            }
        ) {
            Class[] types = new Class [] {       //set column type as String, String, boolean
                java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {  //first column cannot edit while the rest two can
                false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        
        jTableAttributs.setRowHeight(22);
        jScrollPaneAttributs.setViewportView(jTableAttributs); //panel ->table->data model

        add(jScrollPaneAttributs);
        jScrollPaneAttributs.setBounds(70, 90, 380, 200);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelInfosBase;
    private javax.swing.JScrollPane jScrollPaneAttributs;
    private javax.swing.JTable jTableAttributs;
    // End of variables declaration//GEN-END:variables
    
    
    
    
    // Outrepassement de la m�thode m�re pour l'ajustement des champs :
    void ArrangerDisposition() {
        int iDeltaPosX = 0;
        int iDeltaPosY = 0;
        
        super.ArrangerDisposition();
        
        iDeltaPosX = jLabelInfosBase.getX() - super.m_zoneControles.x;
        iDeltaPosY = jLabelInfosBase.getY() - super.m_zoneControles.y;
        
        jLabelInfosBase.setBounds(
        super.m_zoneControles.x,
        jLabelInfosBase.getY()-iDeltaPosY,
        super.m_zoneControles.width,
        jLabelInfosBase.getHeight() );
        
        jScrollPaneAttributs.setBounds(
        super.m_zoneControles.x,
        jScrollPaneAttributs.getY()-iDeltaPosY,
        super.m_zoneControles.width,
        super.m_zoneControles.height+super.m_zoneControles.y-(jScrollPaneAttributs.getY()-iDeltaPosY));
    }
    
    
    
    /**
     * Initialize panel contents
     */
    private void InitialiserContenuPanneau() { //initialize panel content 
        DefaultTableModel tableModel = null;
        int iNombreLignes = 0;
        int iIndiceLigne = 0;
        int iNombreColonnes = 0;
        int iIndiceColonne = 0;
        String sNomColonne = null;
        
        iNombreColonnes = m_gestionnaireBD.ObtenirNombreColonnesBDInitiale(); //get the initial number of columns
        
        if (iNombreColonnes==0)
            return;
        
        tableModel = (DefaultTableModel)(jTableAttributs.getModel());
        
        // start to delete all existed lines one by one:
        iNombreLignes = tableModel.getRowCount();
        for (iIndiceLigne = iNombreLignes-1; iIndiceLigne >= 0; iIndiceLigne--)
            tableModel.removeRow(iIndiceLigne);
        
        
        // Remplissage de la table avec la liste des noms de colonnes disponibles au total dans la BD :
        for (iIndiceColonne=0; iIndiceColonne < iNombreColonnes; iIndiceColonne++) {
            Object [] ligneDonnees = new Object [3];
            sNomColonne = m_gestionnaireBD.ObtenirNomColonneBDInitiale(iIndiceColonne); //Set column name
            
            //Attribute name
            ligneDonnees[0] = new String(sNomColonne);
            
            //Attribute type
            if (m_gestionnaireBD.ObtenirTypeColonne(sNomColonne) == DatabaseAdmin.TYPE_VALEURS_COLONNE_REEL) //Set column type
                ligneDonnees[1] = "Numerical";  //column type is numerical
            else
                ligneDonnees[1] = "Categorical"; //column type is categorical
           
           //Consider
           //consider this column or not, during initialization, it is true, we set all as selected
           //due to the call of PrendreEnCompteToutesLesColonnes() in FenetrePrincipale
            ligneDonnees[2] = new Boolean( m_gestionnaireBD.EstPriseEnCompteColonne(sNomColonne) ); 
            
            tableModel.addRow( ligneDonnees );
        }
    }
    
    
    
    
    // Outrepassement de la m�thode m�re pour mettre � jour les structures de donn�es
    // suivant ce qui a �t� entr� dans les champs de contr�le :
    public boolean SychroniserDonneesInternesSelonAffichage() {
        DefaultTableModel tableModel = null;
        Vector lignesTableauVector = null;
        Vector elementsLigneVector = null;
        Enumeration lignesTableauEnum = null;
        Enumeration elementsLigneEnum = null;
        String sNomColonneDonnees = null;
        String sTypeDonnees = null;
        Boolean priseEnCompteColonne = null;
        int iTypeColonneDonnees = 0;
        boolean bPrendreEnCompte = false;
        
        
        // Prise en compte des informations entr�es :
        tableModel = (DefaultTableModel)(jTableAttributs.getModel());
        //Returns the Vector of Vectors that contains the table's data values.
        lignesTableauVector = tableModel.getDataVector();
        
        // Line enumeration:
        lignesTableauEnum = lignesTableauVector.elements(); //Returns an enumeration of the components of this vector
        while (lignesTableauEnum.hasMoreElements()) {
        	//Returns an enumeration of the components of this vector
            elementsLigneVector = (Vector)lignesTableauEnum.nextElement();
            
            // Interpr�tation de chacun des �l�ments de la ligne en cours :
            try {
                sNomColonneDonnees = (String)elementsLigneVector.elementAt(0);
                sTypeDonnees = (String)elementsLigneVector.elementAt(1);
                priseEnCompteColonne = (Boolean)elementsLigneVector.elementAt(2);
                
                bPrendreEnCompte = false;
                iTypeColonneDonnees = DatabaseAdmin.TYPE_VALEURS_COLONNE_ERREUR;
                
                if (sTypeDonnees.equals("Categorical"))
                    iTypeColonneDonnees = DatabaseAdmin.TYPE_VALEURS_COLONNE_ITEM;
                else if (sTypeDonnees.equals("Numerical"))
                    iTypeColonneDonnees = DatabaseAdmin.TYPE_VALEURS_COLONNE_REEL;
                
                bPrendreEnCompte = priseEnCompteColonne.booleanValue() && (iTypeColonneDonnees != DatabaseAdmin.TYPE_VALEURS_COLONNE_ERREUR);
                
                // On indique au gestionnaire de la BD si on prend en compte l'attribut lors
                // de l'extraction des r�gles, et si tel est le cas on pr�cise son type :
                m_gestionnaireBD.DefinirPriseEnCompteColonne(sNomColonneDonnees, iTypeColonneDonnees, bPrendreEnCompte);
            }
            catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        
        return true;
    }
    
    
    /**The process before going to next step. Here will read data
     */
    protected boolean TraitementsSpecifiquesAvantSuivant() {
    	//if data sync failed
        if (!SychroniserDonneesInternesSelonAffichage())
            return false;
        
        //if the number checked in step 1 is zero
        if (m_gestionnaireBD.ObtenirNombreColonnesPrisesEnCompte() == 0) {
            // Afficher message erreur !!!
            return false;
        }
        
        //Load data
        m_gestionnaireBD.ChargerDonneesPrisesEnCompte();
        
        m_contexteResolution.GenererStructuresDonneesSelonBDPriseEnCompte();
        
        return true;
    }
    
    
}