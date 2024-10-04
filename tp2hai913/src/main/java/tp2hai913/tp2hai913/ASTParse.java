package tp2hai913.tp2hai913;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Files;
import java.nio.file.Paths;


public class ASTParse {
	
	private static int totalClasses = 0;
    private static int totalMethods = 0;
    private static int totalLinesOfCode = 0;
    private static int totalAttributes = 0;
    private static List<TypeDeclaration> classDeclarations = new ArrayList<>();
    private static Set<String> packages = new HashSet<>(); // Utiliser un Set pour stocker les packages uniques
    private static List<MethodDeclaration> allMethods = new ArrayList<>();
    private static List<MethodDeclaration> methodList = new ArrayList<>();
    private static Map<MethodDeclaration, Integer> methodLinesMap = new HashMap<>();
    private static int maxParameters = 0;
    private static List<CallRelation> callRelations = new ArrayList<>(); // Stocke les relations d'appel
    
    
	public static void analyzeProject(String directoryPath, int valeurX, String targetClassName, String outputDotFilePath) throws IOException {
	
	
	//DirectoryPath = "/home/aklex/eclipse-workspace/tp2hai913/src/main/java/tp2hai913/tp2hai913/classAanalyser";
	  
		
	// Obtenir tous les fichiers Java dans le dossier
    File directory = new File(directoryPath);
    File[] files = directory.listFiles();
   
    
    
    // Parcourir chaque fichier pour extraire les statistiques
    for (File file : files) {
        String sourceCode = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        analyzeSourceCode(sourceCode);
    }

    // Affichage des résultats
    System.out.println("Nombre de classes : " + totalClasses);
    System.out.println("Nombre de méthodes : " + totalMethods);
    System.out.println("Nombre total de lignes de code : " + totalLinesOfCode);
    System.out.println("Nombre moyen de méthodes par classe : " + (totalMethods / (float) totalClasses));
    System.out.println("Nombre moyen d'attributs par classe : " + (totalAttributes / (float) totalClasses));
    System.out.println("Nombre moyen line code par classe : " + (totalLinesOfCode / (float) totalClasses));
    System.out.println("Nombre de package : " + packages.size());
 
    
    		// Afficher les 10% classes avec le plus de méthodes
    		List<TypeDeclaration> top10PourcentMehtode = classDeclarations.stream()
            .sorted((a, b) -> Integer.compare(b.getMethods().length, a.getMethods().length)) // Trier par nombre de méthodes
            .limit(Math.max(1, classDeclarations.size() / 10)) // Prendre les 10% (au moins 1)
            .collect(Collectors.toList());
  
    		System.out.println("Les 10% des classes avec le plus de méthodes:");
            for (TypeDeclaration typeDecl : top10PourcentMehtode) {
                System.out.println("Classe : " + typeDecl.getName() + ", Méthodes : " + typeDecl.getMethods().length);
            }
            
            
            
         // Afficher les 10% classes avec le plus d'attributs
    		List<TypeDeclaration> top10PourcentAttrubut = classDeclarations.stream()
            .sorted((a, b) -> Integer.compare(b.getFields().length, a.getFields().length)) // Trier par nombre de méthodes
            .limit(Math.max(1, classDeclarations.size() / 10)) // Prendre les 10% (au moins 1)
            .collect(Collectors.toList());
    		
    		
    		System.out.println("Les 10% des classes avec le plus de d'attributs:");
            for (TypeDeclaration typeDecl : top10PourcentAttrubut) {
                System.out.println("Classe : " + typeDecl.getName() + ", attributs : " + typeDecl.getFields().length);
            }
            
            
            
            // classe commune
            List<TypeDeclaration> classeCommune = top10PourcentMehtode.stream()
            		.filter(top10PourcentAttrubut::contains)
            		.collect(Collectors.toList());
            
            System.out.println("classe commune:");
            for (TypeDeclaration typeDecl : classeCommune) {
                System.out.println("Classe : " + typeDecl.getName() + "methode : " + typeDecl.getMethods().length + ", attributs : " + typeDecl.getFields().length);
            }
           
            if(classeCommune.isEmpty()) {
            	System.out.println("aucune classe commune");
            }
            
            System.out.println("-----------------------------------");
            
            System.out.println("classe avec le plus de methode X:");
            for (TypeDeclaration typeDecl : classDeclarations) {
            	if(typeDecl.getMethods().length > valeurX) {		
                System.out.println("Classe : " + typeDecl.getName() + ", methodes : " + typeDecl.getMethods().length);
            }
            }
            
            
            System.out.println("---------------------------------");
            // Afficher les 10% des méthodes avec le plus grand nombre de lignes de code
            List<MethodDeclaration> top10PercentMethodsByLines = methodList.stream()
                    .sorted((a, b) -> Integer.compare(methodLinesMap.get(b), methodLinesMap.get(a)))
                    .limit(methodList.size() / 10)
                    .collect(Collectors.toList());
            System.out.println("Les 10% des méthodes avec le plus grand nombre de lignes de code:");
            for (MethodDeclaration method : top10PercentMethodsByLines) {
                System.out.println("Méthode : " + method.getName() + ", Lignes : " + methodLinesMap.get(method));
            }
            
           
            System.out.println("------------------------");
            // Afficher le nombre maximal de paramètres
            System.out.println("Nombre maximal de paramètres : " + maxParameters);
        
            
    
        }
	
            
	// Méthodes pour accéder aux résultats de l'analyse
    public static int getTotalClasses() { return totalClasses; }
    public static int getTotalMethods() { return totalMethods; }
    public static int getTotalLinesOfCode() { return totalLinesOfCode; }
    public static int getTotalAttributes() { return totalAttributes; }
    public static int getTotalPackages() { return packages.size(); }
    public static int getMaxParameters() { return maxParameters; }
            
            
	
    public static void reset() {
        totalClasses = 0;
        totalMethods = 0;
        totalLinesOfCode = 0;
        totalAttributes = 0;
        classDeclarations.clear();
        packages.clear();
        allMethods.clear();
        methodList.clear();
        methodLinesMap.clear();
        maxParameters = 0;
    }

    //methode classes avec le plus de méthodes
    public static List<TypeDeclaration> getTop10PercentClassesByMethods() {
        return classDeclarations.stream()
                .sorted((a, b) -> Integer.compare(b.getMethods().length, a.getMethods().length))
                .limit(Math.max(1, classDeclarations.size() / 10))
                .collect(Collectors.toList());
    }

  //methode classes avec le plus de d'attributs
    public static List<TypeDeclaration> getTop10PercentClassesByAttributes() {
        return classDeclarations.stream()
                .sorted((a, b) -> Integer.compare(b.getFields().length, a.getFields().length))
                .limit(Math.max(1, classDeclarations.size() / 10))
                .collect(Collectors.toList());
    }

    
    // classes communes
    public static List<TypeDeclaration> getCommonClasses() {
        List<TypeDeclaration> top10PourcentMehtode = getTop10PercentClassesByMethods();
        List<TypeDeclaration> top10PourcentAttrubut = getTop10PercentClassesByAttributes();
        
        return top10PourcentMehtode.stream()
                .filter(top10PourcentAttrubut::contains)
                .collect(Collectors.toList());
    }

    
    public static int getValueX() {
        // Retourner la valeur X si nécessaire
        return 0; // Remplacez par votre logique
    }
    
    
    public static int getMethodLines(MethodDeclaration method) {
        return methodLinesMap.get(method);
    }
    
    
    public static List<MethodDeclaration> getTop10PercentMethodsByLines() {
        return methodList.stream()
                .sorted((a, b) -> Integer.compare(methodLinesMap.get(b), methodLinesMap.get(a)))
                .limit(methodList.size() / 10)
                .collect(Collectors.toList());
    }

    
    
    public static List<CallRelation> getCallRelations() {
        return callRelations;
    }

 
   
 // Méthode pour afficher les relations d'appel de la classe spécifiée
    public static void printCallRelationsForClass(String className) {
        for (CallRelation relation : callRelations) {
            if (relation.getCaller().contains(className) || relation.getCallee().contains(className)) {
                System.out.println(relation.getCaller() + " appelle " + relation.getCallee());
            }
        }
    }
    
    
    // Méthode pour écrire les relations d'appel dans un fichier .dot
    public static void writeCallRelationsToDot(String outputDotFilePath) throws IOException {
        StringBuilder dotContent = new StringBuilder();
        dotContent.append("digraph G {\n");

        for (CallRelation relation : callRelations) {
            dotContent.append("    ")
                      .append(relation.getCaller())
                      .append(" -> ")
                      .append(relation.getCallee())
                      .append(";\n");
        }

        dotContent.append("}\n");

        // Écrire le contenu dans le fichier .dot
        Files.write(Paths.get(outputDotFilePath), dotContent.toString().getBytes());
        System.out.println("Les relations d'appel ont été écrites dans le fichier : " + outputDotFilePath);
    
    }
    
 // Nouvelle méthode pour générer le fichier PNG
    public static void generatePNG(String outputDotFilePath) throws IOException{
        String outputPngFilePath = outputDotFilePath.replace(".dot", ".png");
        ProcessBuilder processBuilder = new ProcessBuilder("dot", "-Tpng", outputDotFilePath, "-o", outputPngFilePath);

        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor(); // Attendre que le processus se termine
            if (exitCode == 0) {
                System.out.println("Image PNG générée avec succès : " + outputPngFilePath);
            } else {
                System.err.println("Erreur lors de la génération de l'image PNG. Code de sortie : " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

   
    
    
    
// Méthode pour lister tous les fichiers .java dans un répertoire, ici il y a que des .java
public static List<File> listJavaFiles(File dir) {
    List<File> javaFiles = new ArrayList<>();
    File[] files = dir.listFiles();
    if (files != null) {
        for (File file : files) {
            if (file.isDirectory()) {
                javaFiles.addAll(listJavaFiles(file));
            } else if (file.getName().endsWith(".java")) {
                javaFiles.add(file);
            }
        }
    }
    return javaFiles;
}

// Méthode pour analyser le code source d'un fichier Java
public static void analyzeSourceCode(String sourceCode) {
    ASTParser parser = ASTParser.newParser(AST.JLS2); // Utiliser Java 8 pour parser
    parser.setSource(sourceCode.toCharArray());
    parser.setKind(ASTParser.K_COMPILATION_UNIT);

    CompilationUnit cu = (CompilationUnit) parser.createAST(null);
    cu.accept(new ASTVisitor() {
    	
            
    	

    	
    	
    	@Override
        public boolean visit(PackageDeclaration node) {
            // Ajouter le package à l'ensemble des packages uniques
            String packageName = node.getName().toString();
            packages.add(packageName);
            return super.visit(node);
        }
    	
    	
    	
        @Override
        public boolean visit(TypeDeclaration node) {
            totalClasses++;
            classDeclarations.add(node);

            // compter les méthodes et attributs dans chaque classe
            totalMethods += node.getMethods().length;
            totalAttributes += node.getFields().length;

            // Compter les lignes de code par classe
            int startLine = cu.getLineNumber(node.getStartPosition());
            int endLine = cu.getLineNumber(node.getStartPosition() + node.getLength());
            totalLinesOfCode += (endLine - startLine + 1);
            
          
         // Ajouter les méthodes à la liste pour d'autres analyses
            for (MethodDeclaration method : node.getMethods()) {
                methodList.add(method);
                int methodLines = cu.getLineNumber(method.getStartPosition() + method.getLength()) - cu.getLineNumber(method.getStartPosition()) + 1;
                methodLinesMap.put(method, methodLines);
                
                // Mettre à jour le nombre maximal de paramètres
                if (method.parameters().size() > maxParameters) {
                    maxParameters = method.parameters().size();
                
                }
                
                if (method.getBody() != null) {
                    method.getBody().accept(new ASTVisitor() {
                        @Override
                        public boolean visit(MethodInvocation node) {
                            String caller = method.getName().toString();
                            String callee = node.getName().toString();
                            if (caller != null && callee != null) {
                                callRelations.add(new CallRelation(caller, callee));
                            }
                            return super.visit(node);
                        }
                    });
                }
            }
       

        return super.visit(node);
    }
});
}
}


