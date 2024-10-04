package tp2hai913.tp2hai913;

import java.awt.FlowLayout;

import javax.swing.*;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import tp2hai913.tp2hai913.ASTParse;


public class SimpleSwingApp {
    
	 private static String outputfile;
	 
    public static void main(String[] args) {
        JFrame frame = new JFrame("Analyseur de Code");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new FlowLayout());

        JLabel labelDirectory = new JLabel("Entrez le chemin du répertoire :");
        JTextField textFieldDirectory = new JTextField(20);
        
        JLabel labelX = new JLabel("Entrez le nombre de méthodes X :");
        JTextField textFieldX = new JTextField(5);
      
        JLabel labelGraph = new JLabel("Entrez le chemin vers le fichier à analyser :");
        JTextField textFieldGraph = new JTextField(20);
        
        JButton analyzeButton = new JButton("Analyser");
        JButton graphButton = new JButton("Afficher Graphe d'appel");
        graphButton.setEnabled(false);
        JTextArea resultArea = new JTextArea(15, 40);
        resultArea.setEditable(false);
        
        analyzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String directoryPath = textFieldDirectory.getText();
                String xValueText = textFieldX.getText();
                outputfile = directoryPath + "/output.dot"; // Définit le chemin du fichier .dot

                
                try {
                    // Réinitialiser les statistiques avant chaque analyse
                    resetStatistics();
                    // Convertir la valeur X
                    int valeurX = Integer.parseInt(xValueText); // Convertir la valeur X en entier
                    // Appeler la méthode d'analyse
                    ASTParse.analyzeProject(directoryPath, valeurX, "*", outputfile); 
                    // Afficher les résultats
                    displayResults(resultArea, valeurX); // Passer la valeur X à displayResults
                    // Activer le bouton de graphe après l'analyse réussie
                    graphButton.setEnabled(true);
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(frame, "Erreur lors de l'analyse : " + ioException.getMessage());
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(frame, "Veuillez entrer un nombre valide pour X.");
                }
            }
        });
        
        
        graphButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String graphFilePath = textFieldGraph.getText(); // Récupérer le chemin du fichier à analyser

                try {
                    // Générer le fichier DOT pour les relations d'appel
                    ASTParse.writeCallRelationsToDot(outputfile);
                    // Générer le fichier PNG à partir du fichier DOT
                    ASTParse.generatePNG(outputfile);
                    JOptionPane.showMessageDialog(frame, "Graphe d'appel généré avec succès.");
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(frame, "Erreur lors de la génération du graphe : " + ioException.getMessage());
                }
            }
        });
   

        frame.add(labelDirectory);
        frame.add(textFieldDirectory);
        frame.add(labelX);
        frame.add(textFieldX); // Ajouter le champ de saisie pour X
        frame.add(analyzeButton);
        frame.add(new JScrollPane(resultArea)); // Pour faire défiler le texte
        frame.setVisible(true);
        frame.add(graphButton); // Ajouter le bouton dans la fenêtre principale
        frame.add(labelGraph);
        frame.add(textFieldGraph);
       
    }

    private static void resetStatistics() {
        // Réinitialiser les statistiques de votre classe ASTParse
        ASTParse.reset();
    }

    private static void displayResults(JTextArea resultArea, int valeurX) {
        // Récupérer les résultats de l'analyse
        StringBuilder results = new StringBuilder();
        
        results.append("Nombre de classes : ").append(ASTParse.getTotalClasses()).append("\n")
               .append("Nombre de méthodes : ").append(ASTParse.getTotalMethods()).append("\n")
               .append("Nombre total de lignes de code : ").append(ASTParse.getTotalLinesOfCode()).append("\n")
               .append("Nombre moyen de méthodes par classe : ").append(ASTParse.getTotalMethods() / (float) ASTParse.getTotalClasses()).append("\n")
               .append("Nombre moyen d'attributs par classe : ").append(ASTParse.getTotalAttributes() / (float) ASTParse.getTotalClasses()).append("\n")
               .append("Nombre moyen de lignes de code par classe : ").append(ASTParse.getTotalLinesOfCode() / (float) ASTParse.getTotalClasses()).append("\n")
               .append("Nombre de packages : ").append(ASTParse.getTotalPackages()).append("\n")
               .append("Nombre maximal de paramètres : ").append(ASTParse.getMaxParameters()).append("\n")
               .append("\nClasses avec le plus de méthodes :\n");

        // Afficher les classes avec le plus de méthodes
        List<TypeDeclaration> top10PercentMethods = ASTParse.getTop10PercentClassesByMethods();
        for (TypeDeclaration typeDecl : top10PercentMethods) {
            results.append("Classe : ").append(typeDecl.getName()).append(", Méthodes : ").append(typeDecl.getMethods().length).append("\n");
        }

        results.append("\nClasses avec le plus d'attributs :\n");
        
        // Afficher les classes avec le plus d'attributs
        List<TypeDeclaration> top10PercentAttributes = ASTParse.getTop10PercentClassesByAttributes();
        for (TypeDeclaration typeDecl : top10PercentAttributes) {
            results.append("Classe : ").append(typeDecl.getName()).append(", Attributs : ").append(typeDecl.getFields().length).append("\n");
        }

        // Afficher les classes communes
        List<TypeDeclaration> commonClasses = ASTParse.getCommonClasses();
        results.append("\nClasses communes avec le plus de méthodes et attributs :\n");
        for (TypeDeclaration typeDecl : commonClasses) {
            results.append("Classe : ").append(typeDecl.getName()).append(", Méthodes : ").append(typeDecl.getMethods().length).append(", Attributs : ").append(typeDecl.getFields().length).append("\n");
        }

        // Classes avec le plus de méthodes selon valeur X
        results.append("\nClasses avec plus de ").append(valeurX).append(" méthodes :\n");
        for (TypeDeclaration typeDecl : top10PercentMethods) {
            if (typeDecl.getMethods().length > valeurX) {
                results.append("Classe : ").append(typeDecl.getName()).append(", Méthodes : ").append(typeDecl.getMethods().length).append("\n");
            }
        }

        // Afficher 10% méthodes avec le plus grand nombre de lignes de code
        results.append("\n10% méthodes avec le plus grand nombre de lignes de code :\n");
        List<MethodDeclaration> top10PercentMethodsByLines = ASTParse.getTop10PercentMethodsByLines();
        for (MethodDeclaration method : top10PercentMethodsByLines) {
            results.append("Méthode : ").append(method.getName()).append(", Lignes : ").append(ASTParse.getMethodLines(method)).append("\n");
        }
        
       


        // Afficher le nombre maximal de paramètres
        results.append("\nNombre maximal de paramètres parmi toutes les méthodes : ").append(ASTParse.getMaxParameters()).append("\n");

        
        
        resultArea.setText(results.toString());
    }
    
    
}